import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import java.sql.Timestamp

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger

case class DroneData(id: Int, time: Timestamp, coordinates: (Float, Float), percentage: Int)

object kafkaToMinio {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
          .appName("KafkaToMinio")
          .master("local[*]") // Set master URL here
          .config("spark.hadoop.fs.s3a.endpoint", "http://s3:9000")
          .config("spark.hadoop.fs.s3a.access.key", "UserParking")
          .config("spark.hadoop.fs.s3a.secret.key", "PassWordParking")
          .config("spark.hadoop.fs.s3a.path.style.access", "true")
          .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
          .getOrCreate()

          import spark.implicits._

          val kafkaBootstrapServers = "broker:9092"
          val inputTopic = "drones-data"

          val kafkaDF = spark.readStream
            .format("kafka")
            .option("kafka.bootstrap.servers", kafkaBootstrapServers)
            .option("subscribe", inputTopic)
            .load()

          val alertsDF = kafkaDF.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
            .as[(String, String)]

          val processedAlertsDF = alertsDF
            .select($"key", $"value")

        val query = processedAlertsDF.writeStream
          .outputMode("append")
          .format("parquet")
          .option("checkpointLocation", "/tmp/checkpoints")
          .option("path", "s3a://my-bucket/streaming-data/")
          .trigger(Trigger.ProcessingTime("1 minute"))
          .start()

        query.awaitTermination()
  }
}

