import drones.DroneSerializer.toJson
import drones.Drones
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{IntegerSerializer, StringSerializer}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.serialization.Serdes

import java.util.Properties
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
object KafkaProducer extends App {

  val nbDrones = 2
  val nbData = 10

  val topic = "drones-data"

  val futures = (1 to nbDrones)
    .map{id => Future{
      val producer = new Producer(id, topic)
      producer.sendData(nbData)
    }}

  Await.result(Future.sequence(futures), Duration.Inf)
  println("All producers have finished sending messages.")



}