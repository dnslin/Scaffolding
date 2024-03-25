FROM eclipse-temurin:21.0.2_13-jre-alpine

RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone
COPY target/Scaffolding.jar /app/app.jar

ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=prod","app/app.jar"]