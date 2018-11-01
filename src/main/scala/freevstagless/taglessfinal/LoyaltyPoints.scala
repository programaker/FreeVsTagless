package freevstagless.taglessfinal

import java.util.UUID

import cats.Monad
import cats.implicits._

/* LoyaltyPoints can use any UserRepository, no matter what
 * the returned effect F is, as long as a Monad instance
 * exists for this F */
class LoyaltyPoints[F[_] : Monad](ur: UserRepository[F]) {
  def addPoints(userId: UUID, pointsToAdd: Int): F[Either[String,Unit]] = {
    ur.findUser(userId).flatMap {
      case None =>
        implicitly[Monad[F]].pure(Left("User not found"))

      case Some(user) =>
        ur.updateUser(user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)).map{ _ => Right(()) }
    }
  }
}