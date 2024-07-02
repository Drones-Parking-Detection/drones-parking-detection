FROM sbtscala/scala-sbt:eclipse-temurin-alpine-21.0.2_13_1.10.0_2.13.14

COPY . .

RUN sbt assembly

CMD ["java", "-jar", "target/scala-2.13/ConsumerAlerts-assembly-0.1.0-SNAPSHOT.jar"]