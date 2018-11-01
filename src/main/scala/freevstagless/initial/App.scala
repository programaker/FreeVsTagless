package freevstagless.initial

import java.util.UUID

import freevstagless.User

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object App {
  import scala.concurrent.ExecutionContext.Implicits.global
  val userRepository = new InMemoryUserRepository
  val loyaltyPoints = new LoyaltyPoints(userRepository)

  def main(args: Array[String]): Unit = {
    val userId = UUID.randomUUID

    def giveTenPoints: Future[Unit] = loyaltyPoints.addPoints(userId, 10)
      .map {
        case Right(_) => s"addPoints: 10 points given to User($userId)"
        case Left(e) => s"addPoints: $e"
      }
      .map(println)

    val user = for {
      _ <- giveTenPoints
      _ <- userRepository.addUser(User(userId, "lero.lero@hotmail.com", 0)).map{ _ => println("User created") }
      _ <- giveTenPoints
      u <- userRepository.findUser(userId)
    } yield u

    Await.result(user, Duration.Inf).foreach(println)
  }
}
