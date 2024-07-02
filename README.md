# PROJECT DRONES PARKING DETECTION

Project Introduction Data Engineering

<!-- TABLE OF CONTENTS -->

# 📗 Table of Contents

- [🛠 Dependencies](#dependencies)
- [💻 Usage](#usage)
- [👥 Authors](#authors)


## 🛠 Dependencies: <a name="dependencies"></a>
    The technologies stack...

## 💻 Usage: <a name="usage"></a>
    - Identification for all services:
        - User: UserParking
        - Password: PassWordParking
    - mkdir -p minio/data/my-bucket/
    - make
    - make kafka
    - make check_kafka (wait until the process is finiched)
    - make services
    - wait for all the containers to be running
    - go to browser and open 
        - localhost:5001 to watch alert
        - http://localhost:9090/browser/my-bucket to watch data lake info in "my-bucket"
        - http://localhost:3000/d/fdqgrch23zhtse/dashboardparking?orgId=1 to watch grafana, 
    - make send_data (send data to kafka)
    - you can see in the data lake, the data is added
    - you can see in the localhost:5001, the alert is send
    - make statistic
    - the statistic is in the grafana

## 👥 Authors: <a name="authors"></a>
    - Dorian Penso
    - Léa Margery
    - Maxime Buisson
    - Sacha Hibon
