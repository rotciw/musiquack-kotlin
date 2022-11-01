FROM amazoncorretto:17
EXPOSE 8080
COPY ./build/libs/musiquack-kotlin-0.0.1.jar .
CMD ["java", "-jar", "musiquack-kotlin-0.0.1.jar"]