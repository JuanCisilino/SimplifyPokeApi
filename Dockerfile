FROM gradle as builds
WORKDIR /app
COPY / /app

FROM gradle
WORKDIR /app
COPY --from=builds /app/target/generate-0.0.1-SNAPSHOT.jar /app/generate-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["kotlin", "-jar", "generate-0.0.1-SNAPSHOT.jar"]