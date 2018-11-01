package freevstagless.freemonads

import java.util.UUID

import cats.free.Free
import cats.~>
import freevstagless.User

import scala.concurrent.{ExecutionContext, Future}

/* When describing a solution to a problem using the Free Monad,
 * we first need to define a set of basic operations, which are
 * represented as data types (they form an ADT) */
sealed trait UserRepositoryOps[T]
case class AddUser(u: User) extends UserRepositoryOps[Unit]
case class FindUser(id: UUID) extends UserRepositoryOps[Option[User]]
case class UpdateUser(u: User) extends UserRepositoryOps[Unit]

object UserRepository {
  /* Smart constructors for the Free Monad operations */
  def addUser(u: User): UserRepository[Unit] = Free.liftF(AddUser(u))
  def findUser(id: UUID): UserRepository[Option[User]] = Free.liftF(FindUser(id))
  def updateUser(u: User): UserRepository[Unit] = Free.liftF(UpdateUser(u))

  /* An interpreter for the Repository that uses Future */
  def futureInterpreter()(implicit ec: ExecutionContext): UserRepositoryOps ~> Future = new (UserRepositoryOps ~> Future) {
    import scala.collection.mutable.{Map => MutMap}
    private val usersById = MutMap[UUID,User]()

    override def apply[A](ops: UserRepositoryOps[A]): Future[A] = ops match {
      case AddUser(u) => Future {
        usersById(u.id) = u
      }

      case FindUser(id) => Future {
        usersById.get(id)
      }

      case UpdateUser(u) => Future {
        usersById.get(u.id).foreach{ _ => usersById(u.id) = u }
      }
    }
  }
}
