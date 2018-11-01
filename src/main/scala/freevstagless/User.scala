package freevstagless

import java.util.UUID

/** The shared domain object */
case class User(id: UUID, email: String, loyaltyPoints: Int)
