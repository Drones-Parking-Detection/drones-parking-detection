import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import java.sql.Timestamp
import java.time.LocalDate

case class DroneData(id: Int, time: Timestamp, coordinates: (Float, Float), percentage: Int)

object Statistic {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Statistic")
      .master("local[*]")
      .config("spark.hadoop.fs.s3a.endpoint", "http://s3:9000")
      .config("spark.hadoop.fs.s3a.access.key", "UserParking")
      .config("spark.hadoop.fs.s3a.secret.key", "PassWordParking")
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate()

    import spark.implicits._

     // Read data from MinIO
    val rawDF = spark
      .read
      .parquet("s3a://my-bucket/streaming-data/")

    rawDF.printSchema()
    rawDF.show(false)

    // Shame of data
    val schema = StructType(Array(
      StructField("id", IntegerType, true),
      StructField("time", TimestampType, true),
      StructField("coordinates", ArrayType(FloatType, true)),
      StructField("percentage", IntegerType, true)
    ))

    val parsedDF = rawDF.selectExpr("CAST(value AS STRING)")
      .select(from_json($"value", schema).as("data"))
      .select("data.*")

    parsedDF.show()

    val dfWithCoordinates = parsedDF
      .withColumn("latitude", $"coordinates".getItem(0))
      .withColumn("longitude", $"coordinates".getItem(1))
      .drop("coordinates")

    dfWithCoordinates.show(false)

    def getRegion(latitude: Float, longitude: Float): String = {
      (latitude, longitude) match {
        case (lat, lon) if lat >= 46.23 && lon >= 2.21 => "NE"
        case (lat, lon) if lat >= 46.23 && lon < 2.21 => "NO"
        case (lat, lon) if lat < 46.23 && lon >= 2.21 => "SE"
        case (lat, lon) if lat < 46.23 && lon < 2.21 => "SO"
        case _ => "UNKNOWN"
      }
    }


    val getRegionUDF = udf(getRegion _)

    val dfWithRegion = dfWithCoordinates.withColumn("region", getRegionUDF($"latitude", $"longitude"))

    dfWithRegion.show(false)

    val today = LocalDate.now
    val lastYearStart = today
    val lastYearEnd = today.minusDays(1)

    val dfLastYear = dfWithRegion.filter(
      $"time".between(
        java.sql.Timestamp.valueOf(today.atStartOfDay),
        java.sql.Timestamp.valueOf(today.atTime(23, 59, 59))
      )
    )

    val threshold: Int = sys.env.get("FREE_PARKING_THRESHOLD") match {
      case Some(value) => value.toInt
      case None => 10
    }

    val dfStatus = dfLastYear.withColumn("is_free", $"percentage" < threshold)

    val stats = dfStatus.groupBy("region")
      .agg(
        count("id").alias("total_places"),
        avg($"is_free".cast("integer")).alias("percentage_free_places")
      )

    stats.show()

    // drop table before adding for testing
    // val dropTableQuery = "DROP TABLE IF EXISTS region_parking_stats_last_year"
    // spark.sqlContext.sql(dropTableQuery)

    stats.write
      .format("jdbc")
      .option("url", "jdbc:postgresql://postgres-container:5432/DBParking")
      .option("dbtable", "drone_data_stats")
      .option("user", "UserParking")
      .option("password", "PassWordParking")
      .mode("overwrite")
      .save()

    spark.stop()
  }
}
