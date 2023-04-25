FROM maven as builder
WORKDIR /src/
ADD . /src/
RUN mvn package -pl webflux-servlet -am -Dmaven.test.skip=true


FROM java:8
MAINTAINER changan <18846439952@163.com>
WORKDIR /src/
ARG JVM_CONFIG="-Xmx125M -Xms125M -Xss125M"
COPY --from=builder /src/webflux-servlet/target/webflux-servlet.jar .
EXPOSE 8080
CMD java $JVM_CONFIG -jar /src/webflux-servlet.jar