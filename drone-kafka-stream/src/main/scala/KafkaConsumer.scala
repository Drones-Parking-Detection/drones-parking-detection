
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.{IntegerDeserializer, StringDeserializer}

import java.time.{Duration, Instant}
import java.util.Properties
import scala.collection.JavaConverters._




object KafkaConsumer extends App{
  val props = new Properties()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092") // Update with your Kafka broker address
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "alert-consumer-group")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[IntegerDeserializer].getName)
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)

  val consumer: KafkaConsumer[Int, String] = new KafkaConsumer[Int, String](props)
  consumer.subscribe(List("alert-data").asJava)

//  val records: ConsumerRecords[Int, String] = consumer.poll(Duration.ofMillis(100))
//  records.asScala.foreach { record =>
//    println(s"${record.key()} and: ${record.value()}")
//    while (true) {
//      val records: ConsumerRecords[Int, String] = consumer.poll(Duration.ofMillis(100))
//      records.asScala.foreach { record =>
//        println(s"Key: ${record.key()}, Value: ${record.value()}")
////        val alertData = ujson.read(record.value())
//        val alertData = Obj(
//          "id" -> 1,
//          "timestamp" -> Instant.now().toString,
//          "coordinates" -> Arr(40.712776, -74.005974), // New York City coordinates
//          "percentage" -> 75,
//          "address" -> "123 Main St, New York, NY 10001"
//        )
//        val response = requests.post(
//          url = "http://localhost:5000/add_alert",
//          data = ujson.write(alertData),
////            data = ujson.write([40.712776, -74.005974]),
//          headers = Map("Content-Type" -> "application/json")
//        )
//
//        if (response.statusCode == 200) {
//          println("Alert sent to server successfully!")
//        } else {
//          println(s"Failed to send alert to server: ${response.statusCode} ${response.text()}")
//        }
//      }
//    }
//  }
}
