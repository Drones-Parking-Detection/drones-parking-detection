import drones.Drones
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.serialization.Serdes

import java.util.Properties
object KafkaProducer extends App {

  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "drone-kafka-application")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "broker:9092")
  props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.stringSerde.getClass)
  props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.stringSerde.getClass)

  val producer = new KafkaProducer[Int, Drones.droneDataType](props)
  val topic = "drones-data"

  val res = new ProducerRecord[Int, Drones.droneDataType](topic, 1, Drones.randomDroneData())
  producer.send(res)

  producer.close()
}