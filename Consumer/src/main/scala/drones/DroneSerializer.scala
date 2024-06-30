package drones

import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._

object DroneSerializer {
  implicit val userEncoder: Encoder[DroneData] = deriveEncoder[DroneData]
  implicit val userDecoder: Decoder[DroneData] = deriveDecoder[DroneData]

  def toJson(user: DroneData): String = user.asJson.noSpaces
  def fromJson(jsonString: String): Either[Error, DroneData] = decode[DroneData](jsonString)
}
