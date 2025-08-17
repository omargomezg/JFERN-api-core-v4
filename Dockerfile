FROM gradle:7.6.3-jdk17-jammy AS build
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle build.gradle settings.gradle gradlew ./
COPY --chown=gradle:gradle gradle ./gradle

RUN gradle build --no-daemon --build-cache || return 0

COPY --chown=gradle:gradle . /home/gradle/src
RUN gradle build --no-daemon -x test

FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /home/gradle/src/build/libs/api-core-0.1.4-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]