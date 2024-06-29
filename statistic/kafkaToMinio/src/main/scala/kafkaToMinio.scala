import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import java.sql.Timestamp

case class DroneData(id: Int, time: Timestamp, coordinates: (Float, Float), percentage: Int)

object kafkaToMinio {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("KafkaToMinio")
      .config("spark.hadoop.fs.s3a.endpoint", "http://s3:9000")
      .config("spark.hadoop.fs.s3a.access.key", "UserParking")
      .config("spark.hadoop.fs.s3a.secret.key", "PassWordParking")
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate()

    import spark.implicits._

    println("Creating DataFrame...")
    val data = Seq(("Alice", 29), ("Bob", 31), ("Cathy", 25))
    val df = data.toDF("name", "age")

    println("Writing DataFrame to MinIO...")
    df.write
      .format("csv")
      .save("s3a://my-bucket/my-data/")

    println("Data written to MinIO successfully!")
    spark.stop()

    // println("Consume data from Kafka...")
    //
    // val kafkaStream = spark.readStream
    //   .format("kafka")
    //   .option("kafka.bootstrap.servers", "kafka-container:9092")
    //   .option("subscribe", "drones-data")
    //   .option("startingOffsets", "earliest")
    //   .load()
    //
    // // Define schema
    // val schema = StructType(Seq(
    //   StructField("id", IntegerType),
    //   StructField("time", TimestampType, nullable = false),
    //   StructField("coordinates", StructType(Seq(
    //     StructField("_1", FloatType),
    //     StructField("_2", FloatType)
    //   )), nullable = false),
    //   StructField("percentage", IntegerType)
    // ))
    //
    // // Convert JSON string to DataFrame
    // val droneData = kafkaStream.selectExpr("CAST(value AS STRING)").as[String]
    //   .select(from_json($"value", schema).as("data"))
    //   .select("data.*")
    //
    // println("Writing DataFrame to MinIO...")

    // val query = droneData.writeStream
    //   .outputMode("append")
    //   .format("parquet")
    //   .option("checkpointLocation", "/tmp/checkpoints")
    //   .option("path", "s3a://my-bucket/streaming-data/")
    //   .trigger(Trigger.ProcessingTime("20 seconds"))
    //   .start()
    //
    // query.awaitTermination()
  }
}

