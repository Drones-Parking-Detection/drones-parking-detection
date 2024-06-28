import org.apache.spark.sql.SparkSession

object SparkS3Example {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Spark S3 Example")
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
  }
}

