# TH2-Act UI framework (win) demo

TH2-Act UI framework demo contains a library of functions to be called from a script to automate windows Mini-FIX application.
TH2-Act UI framework demo calls TH2-Hand to perform corresponding actions in GUI and verifies the results.

This project contains actions which:
1. Opens Mini-FIX application
2. Setting up and initializing connection to fix-server
3. Send NewOrderSingle via application
4. Extracting details about the last received order
5. Extracting details about the last system message
6. Close the connection
7. Close the application

## Requirements

* JDK 11+
* Gradle (Optional)
* Docker

## Build

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

## Configuration
This box should be configured as default act boxes.

## Release Notes

### 1.1.1
+ updated dependencies

### 1.1.0
+ renamed project to th2-act-ui-framework-win-demo
+ updated README.MD
+ related core updates
