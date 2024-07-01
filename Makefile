all: help
help:
	@echo "Usage:"
	@echo "  make kafka"
	@echo "  make services"
	@echo "  make check_kafka"
	@echo "  make check_logs 'containername'"


kafka:
	@echo "Building Kafka... Run 'make check_kafka to see if it is finished before launching 'make services'"
	docker compose up --build zookeeper kafka topic-creator -d
	@echo "Building Kafka... Run 'make check_kafka to see if it is finished before launching 'make services'"

services:
	docker compose up --build -d

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

# Pass arguments to check_logs
ARGS = $(filter-out $@,$(MAKECMDGOALS))
%:
	@:





