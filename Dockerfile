# First stage: JDK with GraalVM
FROM container-registry.oracle.com/graalvm/native-image:21 AS build

# Update package lists and Install Maven
RUN microdnf update -y && \
microdnf install -y maven gcc glibc-devel zlib-devel libstdc++-devel gcc-c++ && \
microdnf clean all

WORKDIR /usr/src/app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .

RUN mvn clean package -Pnative native:compile

# Second stage: Lightweight debian-slim image
FROM debian:bookworm-slim

WORKDIR /app

# Copy the native binary from the build stage
COPY --from=build /usr/src/app/target/native-executable-app /app/native-executable-app

# Run the application
CMD ["/app/native-executable-app"]