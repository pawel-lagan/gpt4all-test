# quarkus-gpt

This project uses GPT4All project to create simple api that respond to question about generating
Java code - simple code generator powered by GPT4All model. 

This project uses Quarkus, the Supersonic Subatomic Java Framework.
If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Prerequsits

Download models:
https://huggingface.co/orel12/ggml-gpt4all-j-v1.3-groovy/tree/main
https://tfhub.dev/google/universal-sentence-encoder/4

Run postgresql with pgvector extension

docker run -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -p 5432:5432 -v /data:/var/lib/postgresql/data --name postgresql ankane/pgvector

Run SQL that adds ex:
create database embeddings;
create extension vector;

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-gpt-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Usage guide

Steps:

Check if app is responding
GET http://localhost:8080/interact 

Try code generation skills of the chat (response will take around 1 min)
POST http://localhost:8080/interact
Content-Type: text/plain
Body:
write a Java program that encodes string to base64

Add a content to db:
POST http://localhost:8080/interact/article
Content-Type: text/plain
Body:
Elephant is cool


Ask a question about your's content:
POST http://localhost:8080/interact/ask
Content-Type: text/plain
Body:
What is Elephant?






