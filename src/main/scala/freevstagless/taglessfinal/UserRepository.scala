package freevstagless.taglessfinal

import java.util.UUID

import freevstagless.User

import scala.concurrent.{ExecutionContext, Future}

/** Effect-agnostic User repository */
trait UserRepository[F[_]] {
  def addUser(u: User): F[Unit]
  def findUser(id: UUID): F[Option[User]]
  def updateUser(u: User): F[Unit]
}

/** Concrete implementation of UserRepository that uses Future as effect */
class FutureUserRepository(implicit ec: ExecutionContext) extends UserRepository[Future] {
  import scala.collection.mutable.{Map => MutMap}
  private val usersById = MutMap[UUID,User]()

  override def addUser(u: User): Future[Unit] = Future {
    usersById(u.id) = u
  }

  override def findUser(id: UUID): Future[Option[User]] = Future {
    usersById.get(id)
  }

  override def updateUser(u: User): Future[Unit] = Future {
    usersById.get(u.id).foreach{ _ => usersById(u.id) = u }
  }
}