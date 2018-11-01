package freevstagless.freemonads

import java.util.UUID

import freevstagless.User
import cats.implicits._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object App {
  import scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {
    val userId = UUID.randomUUID

    def giveTenPoints: UserRepository[Unit] = LoyaltyPoints.addPoints(userId, 10)
      .map {
        case Right(_) => s"addPoints: 10 points given to User($userId)"
        case Left(e) => s"addPoints: $e"
      }
      .map(println)

    //The "Program" constructed transforming the Free Monad
    //No real operation is being done here! Just data transformations!
    //Note that this "for" is virtually identical to the initial!
    val user = for {
      _ <- giveTenPoints
      _ <- UserRepository.addUser(User(userId, "lero.lero@hotmail.com", 0)).map{ _ => println("User created") }
      _ <- giveTenPoints
      u <- UserRepository.findUser(userId)
    } yield u

    Await.result(
      //Here, we choose the interpreter that will run the Data Structure that represents the "Program"
      user.foldMap(UserRepository.futureInterpreter()),

      Duration.Inf
    ).foreach(println)
  }
}
