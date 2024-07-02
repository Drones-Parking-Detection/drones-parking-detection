fork := true

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.1.1",
  "org.apache.spark" %% "spark-sql" % "3.1.1",
  "org.apache.hadoop" % "hadoop-aws" % "3.2.0"
)


lazy val spark-app= (project in file("."))
  .settings(
    javaOptions ++= Seq( // Spark-specific JVM options
      "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
    )
  )