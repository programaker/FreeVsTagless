package freevstagless.initial

import java.util.UUID

import scala.concurrent.{ExecutionContext, Future}

/** Completely coupled with Future; can't use any alternative like Task, ZIO, etc */
class LoyaltyPoints(ur: UserRepository)(implicit ec: ExecutionContext) {
  def addPoints(userId: UUID, pointsToAdd: Int): Future[Either[String,Unit]] = {
    ur.findUser(userId).flatMap {
      case None =>
        Future{ Left("User not found") }

      case Some(user) =>
        ur.updateUser(user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)).map{ _ => Right(()) }
    }
  }
}
