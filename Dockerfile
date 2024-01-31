FROM openjdk:17-jdk-slim
COPY target/order-system-0.0.1-SNAPSHOT.jar order-system-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/order-system-0.0.1-SNAPSHOT.jar"]
