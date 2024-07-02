name := "KafkaToMinIO"

version := "0.1"

scalaVersion := "2.13.10"

fork := true

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-bundle" % "1.11.874",

  "org.apache.kafka" %% "kafka" % "3.4.0",
  "org.apache.spark" %% "spark-sql" % "3.3.2",
  "org.apache.spark" %% "spark-streaming" % "3.3.2",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.3.2",
  "org.apache.spark" %% "spark-core" % "3.3.2",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.3.2",
  "org.apache.hadoop" % "hadoop-aws" % "3.3.2",
  "com.typesafe" % "config" % "1.4.2"
)



assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

lazy val root= (project in file("."))
  .settings(
    javaOptions ++= Seq(
      "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
    )
  )