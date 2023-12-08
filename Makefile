install:
	./mvnw clean install

run:
	./mvnw spring-boot:run

build-and-run:
	@./mvnw clean package -Dmaven.test.failure.ignore=true
	@java -jar target/ChatApp-0.0.1-SNAPSHOT.jar

compose-up:
	@docker compose up -d

compose-down:
	@docker compose down
	@docker rm -f chat-app-server
	@docker rmi -f chat-app-server
