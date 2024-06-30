FROM sbtscala/scala-sbt:eclipse-temurin-alpine-21.0.2_13_1.10.0_2.13.14

COPY . .

RUN sbt assembly

CMD ["java", "-jar", "target/scala-2.13/KafkaStream-assembly-0.1.0-SNAPSHOT.jar"]



#FROM bitnami/spark:3.1.2
#
#USER root
#
## Installer les dépendances nécessaires, y compris sbt
#RUN apt-get update && \
#    apt-get install -y python3-pip wget curl gnupg2 vim && \
#    pip3 install boto3 && \
#    wget https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.2.0/hadoop-aws-3.2.0.jar && \
#    wget https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.11.874/aws-java-sdk-bundle-1.11.874.jar && \
#    mv hadoop-aws-3.2.0.jar aws-java-sdk-bundle-1.11.874.jar /opt/bitnami/spark/jars/ && \
#    curl -L -o sbt-1.5.5.tgz https://github.com/sbt/sbt/releases/download/v1.5.5/sbt-1.5.5.tgz && \
#    tar -xzf sbt-1.5.5.tgz -C /opt/ && \
#    ln -s /opt/sbt/bin/sbt /usr/local/bin/sbt && \
#    rm sbt-1.5.5.tgz
#
#RUN wget https://jdbc.postgresql.org/download/postgresql-42.2.20.jar -P /opt/bitnami/spark/jars/
#
#USER 1001
#
#
#RUN mkdir -p /opt/spark-apps
#WORKDIR /opt/spark-apps
#
#COPY . /opt/spark-apps
#
#RUN sbt package
