FROM maven:3.6.3-openjdk-11-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM tomcat:9-jdk11-openjdk
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=build /usr/src/app/target/19_jpa_hibernate.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
