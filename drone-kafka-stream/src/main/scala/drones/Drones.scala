package drones

import java.time.LocalDateTime
import scala.annotation.tailrec

case class DroneData(id: Int, time: LocalDateTime, coordinates:(Float, Float), percentage: Int)

object Drones {
  private val rand = new scala.util.Random

//  Format them keeping only 4 decimals
  private def formatCoordinates(float: Float): Float = {
    BigDecimal(float).setScale(4, BigDecimal.RoundingMode.HALF_UP).toFloat
  }

// Generate random geographical coordinates
  private def randomCoordinates(): (Float, Float) = {
    val latitude = formatCoordinates(rand.nextFloat() * (49.5f - 44.0f) + 44.0f)
    val longitude = formatCoordinates(rand.nextFloat() * (6.0f - (-0.0f)) + (-0.0f))
    (latitude, longitude)
  }

// Generate one data
  def randomDroneData(id: Int) : DroneData = {
//    actual timestamp
    val time = LocalDateTime.now()
    val coordinates = randomCoordinates()
//    percentage of being parked correctly
    val percentage = rand.nextInt(100)
    DroneData(id, time, coordinates, percentage)
  }
}


