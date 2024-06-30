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

      // Créer des données de drones statiques pour simuler les données Kafka
      val droneData = Seq(
        DroneData(1, Timestamp.valueOf("2023-06-30 10:00:00"), (48.8566f, 2.3522f), 0),
        DroneData(2, Timestamp.valueOf("2023-06-30 10:00:00"), (48.8566f, 2.3522f), 0),
        DroneData(3, Timestamp.valueOf("2023-06-30 14:00:00"), (48.8566f, 2.3522f), 80),
        DroneData(4, Timestamp.valueOf("2023-06-30 14:00:00"), (48.8566f, 2.3522f), 80),
        DroneData(5, Timestamp.valueOf("2023-06-30 15:00:00"), (48.8566f, 2.3522f), 80),
        DroneData(6, Timestamp.valueOf("2023-06-30 15:00:00"), (48.8566f, 2.3522f), 80),
        DroneData(7, Timestamp.valueOf("2023-06-07 10:00:00"), (48.8566f, 2.3522f), 80),
        DroneData(8, Timestamp.valueOf("2023-06-29 10:02:00"), (48.8568f, 2.3524f), 90),
        DroneData(9, Timestamp.valueOf("2023-06-29 10:02:00"), (48.8568f, 2.3524f), 90),
        DroneData(10, Timestamp.valueOf("2023-06-29 10:02:00"), (48.8568f, 2.3524f), 90),
        DroneData(11, Timestamp.valueOf("2023-05-01 09:00:00"), (45.7640f, 4.8357f), 5),   // Lyon (FR-ARA)
        DroneData(12, Timestamp.valueOf("2023-05-01 09:15:00"), (45.7640f, 4.8357f), 15),  // Lyon (FR-ARA)
        DroneData(13, Timestamp.valueOf("2023-05-01 10:00:00"), (45.7640f, 4.8357f), 20),  // Lyon (FR-ARA)
        DroneData(14, Timestamp.valueOf("2023-07-15 08:30:00"), (48.5734f, 7.7521f), 0),   // Strasbourg (FR-GES)
        DroneData(15, Timestamp.valueOf("2023-07-15 09:00:00"), (48.5734f, 7.7521f), 50),  // Strasbourg (FR-GES)
        DroneData(16, Timestamp.valueOf("2023-07-15 10:00:00"), (48.5734f, 7.7521f), 100), // Strasbourg (FR-GES)
        DroneData(17, Timestamp.valueOf("2023-08-10 07:45:00"), (43.6108f, 3.8772f), 5),   // Montpellier (FR-OCC)
        DroneData(18, Timestamp.valueOf("2023-08-10 08:00:00"), (43.6108f, 3.8772f), 30),  // Montpellier (FR-OCC)
        DroneData(19, Timestamp.valueOf("2023-08-10 09:00:00"), (43.6108f, 3.8772f), 95),  // Montpellier (FR-OCC)
        DroneData(20, Timestamp.valueOf("2023-09-20 10:15:00"), (50.6292f, 3.0573f), 0),   // Lille (FR-HDF)
        DroneData(21, Timestamp.valueOf("2023-09-20 10:30:00"), (50.6292f, 3.0573f), 45),  // Lille (FR-HDF)
        DroneData(22, Timestamp.valueOf("2023-09-20 11:00:00"), (50.6292f, 3.0573f), 75),  // Lille (FR-HDF)
        DroneData(23, Timestamp.valueOf("2023-11-25 16:00:00"), (44.8378f, -0.5792f), 0),  // Bordeaux (FR-NAQ)
        DroneData(24, Timestamp.valueOf("2023-11-25 16:30:00"), (44.8378f, -0.5792f), 60), // Bordeaux (FR-NAQ)
        DroneData(25, Timestamp.valueOf("2023-11-25 17:00:00"), (44.8378f, -0.5792f), 80), // Bordeaux (FR-NAQ)
        DroneData(26, Timestamp.valueOf("2023-10-15 09:45:00"), (48.1173f, -1.6778f), 10), // Rennes (FR-BRE)
        DroneData(27, Timestamp.valueOf("2023-10-15 10:15:00"), (48.1173f, -1.6778f), 25), // Rennes (FR-BRE)
        DroneData(28, Timestamp.valueOf("2023-10-15 11:00:00"), (48.1173f, -1.6778f), 85), // Rennes (FR-BRE)
        DroneData(29, Timestamp.valueOf("2023-12-05 14:00:00"), (43.2965f, 5.3698f), 0),   // Marseille (FR-PAC)
        DroneData(30, Timestamp.valueOf("2023-12-05 14:30:00"), (43.2965f, 5.3698f), 55),  // Marseille (FR-PAC)
        DroneData(31, Timestamp.valueOf("2023-12-05 15:00:00"), (43.2965f, 5.3698f), 90),  // Marseille (FR-PAC)
        DroneData(32, Timestamp.valueOf("2023-03-12 09:00:00"), (47.2184f, -1.5536f), 5),  // Nantes (FR-PDL)
        DroneData(33, Timestamp.valueOf("2023-03-12 09:30:00"), (47.2184f, -1.5536f), 20), // Nantes (FR-PDL)
        DroneData(34, Timestamp.valueOf("2023-03-12 10:00:00"), (47.2184f, -1.5536f), 85), // Nantes (FR-PDL)
        DroneData(35, Timestamp.valueOf("2023-01-01 12:00:00"), (48.8566f, 2.3522f), 5),   // Paris (FR-IDF)
        DroneData(36, Timestamp.valueOf("2023-01-01 12:30:00"), (48.8566f, 2.3522f), 50),  // Paris (FR-IDF)
        DroneData(37, Timestamp.valueOf("2023-01-01 13:00:00"), (48.8566f, 2.3522f), 95),  // Paris (FR-IDF)
        DroneData(38, Timestamp.valueOf("2023-02-20 08:00:00"), (49.1829f, -0.3707f), 0),  // Caen (FR-NOR)
        DroneData(39, Timestamp.valueOf("2023-02-20 08:30:00"), (49.1829f, -0.3707f), 35), // Caen (FR-NOR)
        DroneData(40, Timestamp.valueOf("2023-02-20 09:00:00"), (49.1829f, -0.3707f), 70)  // Caen (FR-NOR)
      ).toDF()


    // Afficher les données pour vérifier
    droneData.show()

    // Écrire les données dans MinIO en format Parquet
    println("Writing DataFrame to MinIO...")
    droneData.write
      .mode("append")
      .parquet("s3a://my-bucket/streaming-data/")

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

