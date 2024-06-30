import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import org.slf4j.LoggerFactory

object KafkaProducer extends App {
  val logger = LoggerFactory.getLogger(KafkaProducer.getClass)

  val nbDrones = 2
  val nbData = 10

  val topic = "drones-data"

  val futures = (1 to nbDrones)
    .map{id => Future{
      val producer = new Producer(id, topic)
      producer.sendData(nbData)
    }}

  Await.result(Future.sequence(futures), Duration.Inf)
  logger.debug("All producers have finished sending messages.")
}