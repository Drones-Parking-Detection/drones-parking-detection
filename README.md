# PROJECT DRONES PARKING DETECTION

<!-- TABLE OF CONTENTS -->

# 📗 Table of Contents
- [🚀 Subject](#subject)
- [🛠 Dependencies](#dependencies)
- [⚙️ Setup](#setup)
- [💻 Usage](#usage)
- [👥 Authors](#authors)

## 🚀 Subject <a name="subject"></a>
This project consists of multiple drones flying over a region (city, country) in order to supervise how cars are parked.
It calculates a percentage that represents how well a car is parked on the parking slot. It can also determine if a slot is empty or not.

Those data are either used by the local authorities if a car is badly parked or by a company.
Indeed, alerts are sent to the police quickly so that they can take care of it as fast as possible. 
On the other side, all the drones data are used to make statistics of the region's parking slots availability. 

## 🛠 Dependencies <a name="dependencies"></a>
- Kafka
- Spark
- Grafana
- Flask
- Docker
- Postgresql
- MinIO

## ⚙️ Setup <a name="setup"></a>
In order to setup the app, the user can modify three environnement variables:
- `PARKING_ALERT_THRESHOLD_MIN` 
- `PARKING_ALERT_THRESHOLD_MAX`
- `FREE_PARKING_THRESHOLD`

If `PARKING_ALERT_THRESHOLD_MIN < percentage < PARKING_ALERT_THRESHOLD_MAX`, then the car is **badly parked**.
If `percentage < FREE_PARKING_THRESHOLD`, then parking lot is **not occupied**.
    

## 💻 Usage <a name="usage"></a>
Here are the steps you'll need to follow in order to run the app. \
Some services will need a login and a password, which are the following:
- User: UserParking
- Password: PassWordParking

```sh
$ mkdir -p minio/data/my-bucket/
$ make
$ make kafka
$ make check_kafka #(wait until the process is finished)
$ make services
```
Wait for all the containers to be running. \
Go to your browser and open 
- http://localhost:5001 -> webapp for alerts
- http://localhost:9090/browser/my-bucket -> data lake info in "my-bucket"
- http://localhost:3000/d/fdqgrch23zhtse/dashboardparking?orgId=1 -> grafana dashboard
```sh
$ make send_data #(generates the drones data, wait until the process is finished before the next command)
```
You can see in the webapp (http://localhost:5001), the alerts are sent \
You can see in the data lake (http://localhost:9090)
```sh
$ make stats #(batch processing to generate some stats, wait until the process is finished before the next command)
```
The statistics are in the grafana (http://localhost:3000/d/fdqgrch23zhtse/dashboardparking?orgId=1)

## 👥 Authors <a name="authors"></a>
- Dorian Penso
- Léa Margery
- Maxime Buisson
- Sacha Hibon
