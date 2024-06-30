name := "KafkaToMinIO"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.1.2",
  "org.apache.spark" %% "spark-sql" % "3.1.2",
  "org.apache.spark" %% "spark-streaming" % "3.1.2",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.1.2",
  "org.apache.hadoop" % "hadoop-aws" % "3.2.0",
  "com.amazonaws" % "aws-java-sdk-bundle" % "1.11.874"
)
