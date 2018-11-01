package freevstagless.initial

import java.util.UUID

import freevstagless.User

import scala.concurrent.{ExecutionContext, Future}

/** Completely coupled with Future; can't use any alternative like Task, ZIO, etc */
trait UserRepository {
  def addUser(u: User): Future[Unit]
  def findUser(id: UUID): Future[Option[User]]
  def updateUser(u: User): Future[Unit]
}

class InMemoryUserRepository(implicit ec: ExecutionContext) extends UserRepository {
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