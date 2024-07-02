# PROJECT DRONES PARKING DETECTION

Project Introduction Data Engineering

<!-- TABLE OF CONTENTS -->

# 📗 Table of Contents

- [🛠 Dependencies](#dependencies)
- [🛠 Setup](#setup)
- [💻 Usage](#usage)
- [👥 Authors](#authors)


## 🛠 Dependencies: <a name="dependencies"></a>
    - Kafka
    - Spark
    - Grafana
    - Flask
    - Docker
    - Postgresql
    - MinIO

## 🛠! Setup: <a name="setup"></a>
    In order to setup the app, the user can modify three environnement variables:
    - PARKING_ALERT_THRESHOLD_MIN 
    - PARKING_ALERT_THRESHOLD_MAX
    - FREE_PARKING_THRESHOLD

    IF PARKING_ALERT_THRESHOLD_MIN < percentage < PARKING_ALERT_THRESHOLD_MAX, then the car is badly parked.
    IF percentage < FREE_PARKING_THRESHOLD, then parking lot is not occupied.
    

## 💻 Usage: <a name="usage"></a>
    - Identification for all services:
        - User: UserParking
        - Password: PassWordParking
    - mkdir -p minio/data/my-bucket/
    - make
    - make kafka
    - make check_kafka (wait until the process is finished)
    - make services
    - wait for all the containers to be running
    - go to browser and open 
        - http://localhost:5001 -> webapp for alerts
        - http://localhost:9090/browser/my-bucket -> data lake info in "my-bucket"
        - http://localhost:3000/d/fdqgrch23zhtse/dashboardparking?orgId=1 -> grafana dashboard, 
    - make send_data (send data to kafka)
    - you can see in the webapp (http://localhost:5001), the alerts are sent
    - you can see in the data lake (http://localhost:9090)
    - make stats (batch processing to generate some stats)
    - the statistics are in the grafana (http://localhost:3000/d/fdqgrch23zhtse/dashboardparking?orgId=1)

## 👥 Authors: <a name="authors"></a>
    - Dorian Penso
    - Léa Margery
    - Maxime Buisson
    - Sacha Hibon
