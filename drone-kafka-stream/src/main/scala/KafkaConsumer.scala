
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.{IntegerDeserializer, StringDeserializer}

import java.util.Properties
import scala.collection.JavaConverters._

import ujson._


object KafkaConsumer extends App{
  val props = new Properties()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092") // Update with your Kafka broker address
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "alert-consumer-group")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[IntegerDeserializer].getName)
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)

  val consumer: KafkaConsumer[Int, String] = new KafkaConsumer[Int, String](props)
  consumer.subscribe(List("alert-data").asJava)


  while (true) {
    val records: ConsumerRecords[Int, String] = consumer.poll(java.time.Duration.ofMillis(100))
    records.asScala.foreach { record =>
      println(s"Key: ${record.key()}, Value: ${record.value()}")
      val alertDataTmp = ujson.read(record.value())

      val alertData = Obj(
        "id" -> alertDataTmp("id"),
        "timestamp" -> alertDataTmp("time"),
        "coordinates" -> Arr(alertDataTmp("coordinates").arr(0), alertDataTmp("coordinates").arr(1)),
        "percentage" -> alertDataTmp("percentage"),
        "address" -> "France"
      )

      val response = requests.post(
        url = "http://localhost:5000/add_alert",
        data = write(alertData),
        headers = Map("Content-Type" -> "application/json")
      )

      if (response.statusCode == 200) {
        println("Alert sent to server successfully!")
      } else {
        println(s"Failed to send alert to server: ${response.statusCode} ${response.text()}")
      }
    }
  }
}
