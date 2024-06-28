open terminal and do:
    - docker compose build
    - docker compose up
open your search engine and go to http://127.0.0.1:9090
    - user: UserParking
    - password: PassWordParking
    - create a bucket with name: my-bucket
open new terminal and do:
    - docker exec -it spark-container bash
    - sbt package
    - spark-submit --class SparkS3Example --master local[*] target/scala-2.12/sparks3example_2.12-0.1.jar

return to Minio page and refresh bucket, there is your data !!!
je vais ameliorer tout ca apres mais la je dois aller a la salle
