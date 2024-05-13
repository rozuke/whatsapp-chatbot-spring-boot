FROM ubuntu:latest AS build

WORKDIR /app
COPY . .
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven && \
    mvn clean install

# Stage 2: Production stage
FROM openjdk:17-jdk-slim

RUN apt-get update && \
    apt-get install -y curl python python-dev python3-pip && \
    pip install --upgrade pip

# Download and install Google Cloud SDK (optional if not already installed) \
RUN curl https://dl.google.com/dl/cloudsdk/release/google-cloud-sdk.tar.gz > /tmp/google-cloud-sdk.tar.gz && \
     mkdir -p /usr/local/gcloud && \
     tar -C /usr/local/gcloud -xvf /tmp/google-cloud-sdk.tar.gz && \
     /usr/local/gcloud/google-cloud-sdk/install.sh && \
     rm /tmp/google-cloud-sdk.tar.gz
ENV PATH $PATH:/usr/local/gcloud/google-cloud-sdk/bin
# Copy service account key into the image
COPY key.json /tmp/service-account-key.json

# Set GOOGLE_APPLICATION_CREDENTIALS environment variable
ENV GOOGLE_APPLICATION_CREDENTIALS /tmp/service-account-key.json

COPY --from=build /app/target/chatbot-whatsapp-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]