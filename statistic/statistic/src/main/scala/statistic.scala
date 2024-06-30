import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import java.sql.Timestamp
import java.time.LocalDate

case class DroneData(id: Int, time: Timestamp, coordinates: (Float, Float), percentage: Int)

object Statistic {
  def getRegion(latitude: Float, longitude: Float): String = {
      // Dummy implementation: replace with actual logic to determine region
      if (latitude > 45.0) "Region_A" else "Region_B"
    }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Statistic")
      .config("spark.hadoop.fs.s3a.endpoint", "http://s3:9000")
      .config("spark.hadoop.fs.s3a.access.key", "UserParking")
      .config("spark.hadoop.fs.s3a.secret.key", "PassWordParking")
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate()

    import spark.implicits._

    val schema = StructType(Array(
      StructField("id", IntegerType, true),
      StructField("time", TimestampType, true),
      StructField("coordinates", StructType(Array(
        StructField("_1", FloatType, true),
        StructField("_2", FloatType, true)
      )), true),
      StructField("percentage", IntegerType, true)
    ))

    val df = spark.read
      .schema(schema)
      .parquet("s3a://my-bucket/streaming-data/")

    df.show()

    def getRegion(latitude: Float, longitude: Float): String = {
      (latitude, longitude) match {
        case (lat, lon) if lat >= 44.0 && lat <= 46.5 && lon >= 4.0 && lon <= 7.5 => "FR-ARA"  // Auvergne-Rhône-Alpes
        case (lat, lon) if lat >= 46.0 && lat <= 48.5 && lon >= 3.0 && lon <= 7.5 => "FR-BFC"  // Bourgogne-Franche-Comté
        case (lat, lon) if lat >= 47.0 && lat <= 49.0 && lon >= -5.5 && lon <= -1.0 => "FR-BRE" // Bretagne
        case (lat, lon) if lat >= 46.0 && lat <= 48.0 && lon >= 0.0 && lon <= 3.5 => "FR-CVL"  // Centre-Val de Loire
        case (lat, lon) if lat >= 41.5 && lat <= 43.0 && lon >= 8.5 && lon <= 9.5 => "FR-COR"  // Corse
        case (lat, lon) if lat >= 47.5 && lat <= 50.5 && lon >= 4.5 && lon <= 8.0 => "FR-GES"  // Grand Est
        case (lat, lon) if lat >= 49.0 && lat <= 51.0 && lon >= 1.5 && lon <= 4.5 => "FR-HDF"  // Hauts-de-France
        case (lat, lon) if lat >= 48.0 && lat <= 49.0 && lon >= 2.0 && lon <= 3.5 => "FR-IDF"  // Île-de-France
        case (lat, lon) if lat >= 48.5 && lat <= 50.0 && lon >= -1.5 && lon <= 1.5 => "FR-NOR"  // Normandie
        case (lat, lon) if lat >= 43.0 && lat <= 46.5 && lon >= -1.5 && lon <= 1.5 => "FR-NAQ"  // Nouvelle-Aquitaine
        case (lat, lon) if lat >= 42.5 && lat <= 45.5 && lon >= 1.0 && lon <= 4.5 => "FR-OCC"  // Occitanie
        case (lat, lon) if lat >= 46.5 && lat <= 48.5 && lon >= -2.5 && lon <= 0.0 => "FR-PDL"  // Pays de la Loire
        case (lat, lon) if lat >= 43.0 && lat <= 45.0 && lon >= 5.0 && lon <= 7.5 => "FR-PAC"  // Provence-Alpes-Côte d'Azur
        case _ => "UNKNOWN"
      }
    }


    val getRegionUDF = udf(getRegion _)

    // Ajouter la colonne région
    val dfWithRegion = df.withColumn("latitude", $"coordinates._1")
      .withColumn("longitude", $"coordinates._2")
      .drop("coordinates")
      .withColumn("region", getRegionUDF($"latitude", $"longitude"))

    dfWithRegion.show()

    // Filtrer les données pour obtenir celles de l'année dernière jusqu'au jour précis
    val today = LocalDate.now
    val lastYearStart = today.minusYears(1)
    val lastYearEnd = today.minusDays(1)

    val dfLastYear = dfWithRegion.filter(
      $"time".between(
        java.sql.Timestamp.valueOf(lastYearStart.atStartOfDay),
        java.sql.Timestamp.valueOf(lastYearEnd.atTime(23, 59, 59))
      )
    )

    val dfStatus = dfLastYear.withColumn("is_free", $"percentage" < 10)

    val stats = dfStatus.groupBy("region")
      .agg(
        count("id").alias("total_places"),
        avg($"is_free".cast("integer")).alias("percentage_free_places")
      )

    stats.show()

    val dropTableQuery = "DROP TABLE IF EXISTS region_parking_stats_last_year"
    spark.sqlContext.sql(dropTableQuery)

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
