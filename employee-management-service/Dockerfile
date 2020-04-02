#FROM openjdk:8-jdk-alpine
#ENV APP_HOME=/usr/app/
#WORKDIR $APP_HOME
#COPY ./build/libs/* /netent/app/api.jar
#CMD ["java","-jar","api.jar"]

FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]