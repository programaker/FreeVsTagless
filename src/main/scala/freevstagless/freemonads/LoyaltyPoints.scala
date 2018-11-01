package freevstagless.freemonads

import java.util.UUID

import cats.free.Free

object LoyaltyPoints {
  def addPoints(userId: UUID, pointsToAdd: Int): UserRepository[Either[String,Unit]] =
    UserRepository.findUser(userId).flatMap {
      case None =>
        Free.pure(Left("User not found"))

      case Some(user) =>
        UserRepository.updateUser(user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)).map{ _ => Right(()) }
    }
}
