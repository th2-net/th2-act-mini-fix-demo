# TH2-Act UI framework demo

TH2-Act UI framework demo contains a library of functions to be called from a script to automate windows Mini-FIX application.
TH2-Act UI framework demo calls TH2-Hand to perform corresponding actions in GUI and verifies the results.

### Requirements

* JDK 11+
* Gradle (Optional)
* Docker

### Build

This project is built by Gradle.
You can use Gradle wrapper to build it:
``` shell script
./gradlew build
```
To build a Docker image use Dockerfile,
e.g.
``` shell script
docker build -t <image name>:<version> -f Dockerfile .
``` 

### Configuration

This project uses environment variables as its settings

ENV VAR NAME | DEFAULT VALUE | DESCRIPTION
------------ | ------------- | -----------
TH2_EVENT_STORAGE_GRPC_HOST | localhost | Event storage gRPC host
TH2_EVENT_STORAGE_GRPC_PORT | 8080 | Event storage gRPC port
HAND_GRPC_HOST | localhost | TH2-Hand gRPC host
HAND_GRPC_PORT | 8080 | TH2-Hand gRPC port
GRPC_PORT | 8090 | TH2-Act gRPC Server port to run on