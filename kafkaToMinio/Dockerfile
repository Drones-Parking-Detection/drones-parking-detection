FROM bitnami/spark:3.3.2

USER root

RUN apt-get update && \
    apt-get install -y python3-pip wget curl gnupg2 vim && \
    pip3 install boto3 && \
    wget https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.2.0/hadoop-aws-3.2.0.jar && \
    wget https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.11.874/aws-java-sdk-bundle-1.11.874.jar && \
    wget https://repo1.maven.org/maven2/org/apache/spark/spark-sql-kafka-0-10_2.12/3.1.2/spark-sql-kafka-0-10_2.12-3.1.2.jar && \
    mv hadoop-aws-3.2.0.jar aws-java-sdk-bundle-1.11.874.jar /opt/bitnami/spark/jars/ && \
    curl -L -o sbt-1.10.0.tgz https://github.com/sbt/sbt/releases/download/v1.10.0/sbt-1.10.0.tgz && \
    tar -xzf sbt-1.10.0.tgz -C /opt/ && \
    ln -s /opt/sbt/bin/sbt /usr/local/bin/sbt && \
    rm sbt-1.10.0.tgz

RUN mkdir -p /opt/spark-apps
WORKDIR /opt/spark-apps

COPY . /opt/spark-apps

RUN sbt package

ENTRYPOINT ["sbt", "run"]
