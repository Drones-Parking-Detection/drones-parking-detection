package drones

import java.time.LocalDateTime
import scala.annotation.tailrec

object Drones {
  private val rand = new scala.util.Random
  private type droneData = (Int, LocalDateTime, (Float, Float), Int)

  private def formatCoordinates(float: Float, nb_decimals : Int) = {
    BigDecimal(float).setScale(nb_decimals, BigDecimal.RoundingMode.HALF_UP).toFloat
  }


  private def randomCoordinates(): (Float, Float) = {
    val latitude = formatCoordinates(rand.between(-90.0, 90.0).toFloat, 4)
    val longitude = formatCoordinates(rand.between(-180.0, 180.0).toFloat, 4)
    (latitude, longitude)
  }

  private def randomDroneData() : droneData = {
    val time = LocalDateTime.now()
    val id = rand.nextInt(100) // how many drones ?
    val coordinates = randomCoordinates()
    val percentage = rand.nextInt(100)
    (id, time, coordinates, percentage)
  }

  @tailrec
  private def rec_nRandomDataDrones(n: Int, l : List[droneData]) : List[droneData]= {
    n match {
      case 0 => l
      case n => rec_nRandomDataDrones(n-1, randomDroneData()::l)
    }
  }

  def nRandomDataDrones(n : Int):List[droneData] = rec_nRandomDataDrones(n, List())
}