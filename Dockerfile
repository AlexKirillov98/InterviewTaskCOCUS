FROM openjdk:17-alpine
COPY target/InterviewTask-0.0.1-SNAPSHOT.jar InterviewTask-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/InterviewTask-0.0.1-SNAPSHOT.jar"]