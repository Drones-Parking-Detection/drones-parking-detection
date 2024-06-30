import drones.DroneData
import drones.DroneSerializer.{fromJson, toJson}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.serialization.Serdes
import org.apache.kafka.streams.scala.Serdes._

import java.time.LocalDateTime
import java.util.Properties

object KafkaStream extends App {
  val streamsConfig = new Properties()
  streamsConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, "alerts-application")
  streamsConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "broker:9092")
  streamsConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.intSerde.getClass)
  streamsConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.stringSerde.getClass)

  val builder : StreamsBuilder = new StreamsBuilder()

  val inputTopic : String = "drones-data"
  val wronglyParkedTopic : String = "alert-data"

  val inputStream = builder.stream[Int, String](inputTopic)

  val alertProcessedStream = inputStream
    .mapValues(x => fromJson(x).getOrElse(DroneData(-1, LocalDateTime.now(), (-1,-1), -1)))
    .filter((_, data) => data.percentage < 50)
    .mapValues(x => toJson(x))


  alertProcessedStream.to(wronglyParkedTopic)
  val streams = new KafkaStreams(builder.build(),streamsConfig)
  streams.start()
}