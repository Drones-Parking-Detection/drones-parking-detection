all: help
help:
	@echo "step 1 = kafka, step 2 = check_kafka, step3 = services, step 4 = send_data"
	@echo "Usage:"
	@echo "  make kafka"
	@echo "  make services"
	@echo "  make check_kafka"
	@echo "  make check_logs 'containername'"



kafka:
	@echo "Building Kafka... Run 'make check_kafka to see if it is finished before launching 'make services'"
	docker compose up --build zookeeper kafka topic-creator
	@echo "Building Kafka... Run 'make check_kafka to see if it is finished before launching 'make services'"

services:
	docker compose up --build

check_kafka:
	@container_status=$$(docker ps -a --filter "name=topic-creator-1" --format "{{.Status}}"); \
	if echo $$container_status | grep -q "Exited"; then \
		echo "Container 'topic-creator-1' has finished."; \
	else \
		echo "Container 'topic-creator-1' is still running."; \
	fi


check_logs:
	for container in $(ARGS); do \
		@echo "Checking logs for container: $$container"; \
		docker logs -f -t $$container; \

send_data:
	docker exec -it drone-container sbt run


stats:
	docker exec -it spark-statistic-container /opt/bitnami/spark/bin/spark-submit --class Statistic /opt/spark-apps/target/scala-2.12/statistic_2.12-0.1.0-SNAPSHOT.jar


# Pass arguments to check_logs
ARGS = $(filter-out $@,$(MAKECMDGOALS))
%:
	@:
