FROM maven as builder
WORKDIR /src/
ADD . /src/
RUN mvn package -am -Dmaven.test.skip=true
RUN ls


FROM java:8
MAINTAINER changan <18846439952@163.com>
WORKDIR /src/
ARG JVM_CONFIG="-Xmx125M -Xms125M -Xss125M"
COPY --from=builder /src/target/letter-server.jar .
EXPOSE 18080
CMD java $JVM_CONFIG -jar /src/letter-server.jar