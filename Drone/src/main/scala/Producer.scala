import drones.DroneSerializer.toJson
import drones.Drones
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{IntegerSerializer, StringSerializer}

import java.util.Properties
import scala.util.Random

class Producer(id: Int, topic: String) {
  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer].getName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

  val producer = new KafkaProducer[Int, String](props)

  def sendData(n: Int): Unit = {

    (1 to n).map{_ => {
      val rnd = new Random()
        val randomData = Drones.randomDroneData(id)
        val record = new ProducerRecord[Int, String](topic, id, toJson(randomData))
        producer.send(record)
        Thread.sleep(rnd.nextInt(500))
    }}
    producer.close()
  }
}
