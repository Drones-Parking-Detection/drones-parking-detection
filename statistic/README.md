open terminal and do:
    - docker compose build
    - docker compose up
open your search engine and go to http://127.0.0.1:9090
    - user: UserParking
    - password: PassWordParking
    - create a bucket with name: my-bucket
open new terminal and do:
    - docker exec spark-kafkatominio-container /opt/bitnami/spark/bin/spark-submit --class kafkaToMinio --master "local[*]" /opt/spark-apps/target/scala-2.12/kafkatominio_2.12-0.1.jar
    - docker exec -it spark-statistic-container /opt/bitnami/spark/bin/spark-submit --class Statistic --master "local[*]" /opt/spark-apps/target/scala-2.12/spark-apps_2.12-0.1.0-SNAPSHOT.jar


return to Minio page and refresh bucket, there is your data !!!
je vais ameliorer tout ca apres mais la je dois aller a la salle
