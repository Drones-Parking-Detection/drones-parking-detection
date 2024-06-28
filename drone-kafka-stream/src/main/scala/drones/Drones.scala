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
//    val latitude = formatCoordinates(rand.between(-90.0, 90.0).toFloat)
//    val longitude = formatCoordinates(rand.between(-180.0, 180.0).toFloat)
//    (latitude, longitude)
    (-1, 1)
  }

// Generate one data
  def randomDroneData() : DroneData = {
//    actual timestamp
    val time = LocalDateTime.now()
//    random drone id
    val id = rand.nextInt(100) // how many drones ?
    val coordinates = randomCoordinates()
//    percentage of being parked correctly
    val percentage = rand.nextInt(100)
    DroneData(id, time, coordinates, percentage)
  }

  @tailrec
  private def rec_nRandomDataDrones(n: Int, l : List[DroneData]) : List[DroneData]= {
    n match {
      case 0 => l
      case n => rec_nRandomDataDrones(n-1, randomDroneData()::l)
    }
  }

//  Generate n random data
  def nRandomDataDrones(n : Int):List[DroneData] = rec_nRandomDataDrones(n, List())
}


