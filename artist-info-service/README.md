# artist-info-service

## Purpose
This microservice is a clever tool that gives you all the information you need about your favorite artists, upcoming events, and cool venues. It's like having a personal assistant who keeps you in the loop with the latest news, releases, and concert dates. You can find detailed profiles of artists, discover new talents, and never miss a chance to see them perform live. The microservice also helps you explore different venues, check out their facilities, and see photos to get a sense of what they're like. It's user-friendly, fast, and makes your music experience even more exciting and convenient.

## Requirements
- [JDK 11](https://www.oracle.com/uk/java/technologies/javase/jdk11-archive-downloads.html)

## Running the application locally

```
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
Note: Make sure our command/terminal is in artist-info-service foler

## Run the application

```
curl --include http://localhost:8080/v2/artist/21
```
