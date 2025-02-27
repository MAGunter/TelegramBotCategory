# syntax=docker/dockerfile:1

################################################################################
# Stage 1: Build dependencies (to leverage Docker caching)
FROM eclipse-temurin:17-jdk-jammy AS deps

WORKDIR /build

# Copy the Gradle wrapper with executable permissions
COPY --chmod=0755 gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

# Download dependencies as a separate step to leverage Docker caching
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies --no-daemon

################################################################################
# Stage 2: Build the application
FROM deps AS package

WORKDIR /build

# Build the project and create the application JAR
RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build -x test

# Создадим каталог и скопируем JAR-файл
RUN mkdir -p /build/output && cp $(ls build/libs/*.jar | head -n 1) /build/output/app.jar

################################################################################
# Stage 3: Runtime environment
FROM eclipse-temurin:17-jre-jammy AS final

# Create a non-privileged user
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

# Copy the executable JAR from the "package" stage
COPY --from=package /build/output/app.jar app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]
