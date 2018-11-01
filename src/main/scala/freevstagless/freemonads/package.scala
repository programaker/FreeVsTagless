package freevstagless

import cats.free.Free

package object freemonads {
  /** Type Alias for our Free Monad.
    * UserRepositoryOps represents the operations/algebra and
    * T specifies the result of an operation */
  type UserRepository[T] = Free[UserRepositoryOps, T]
}
