ARG spring_baseImage="nexus-optima.corp.andocs.com:8360/base/ubuntu-jdk:24.04.2-21.0.15"
FROM ${spring_baseImage}

RUN mkdir -p /opt/care/cms-adapter/logs

WORKDIR /opt/care/cms-adapter

COPY *jar app.jar

# App ports
EXPOSE 9090 9091

# Let JVM adapt to container memory
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java", "-jar", "app.jar"]
