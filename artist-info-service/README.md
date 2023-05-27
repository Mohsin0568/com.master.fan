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

## Running the test cases locally

```
./mvnw test
```

## Execute artist details endpoint

```
curl --include http://localhost:8080/v2/artist/21
```
## System Design

I had two approaches for designing an API which will give me artist information by its Id.
1. Load all data related to artists, events and venues from upstream server during application startup and store them in embedded Mondog DB. And fetch data from DB for request coming for getArtist API. This approach has lot of drawbacks like it will slow down the application startup and we might need to restart application whenever there is an update of data in upstream server. 
2. Design microservices architecture, where we will have artist, events and venus as a differnt services. When request comes to artist api, it will make a call to artist service (but ideally it should take artist data from DB, since current request is in artist service only) to fetch artist details, then it wil make a call to events service to fetch events information and then make a call to venues service to fetch venue details.

I have taken **2nd approach** to implement the solution and below are some highlights of the implementation.
1. Have used Spring Webflux (which is non blocking web framework) to implement the solution.
2. Endpoint v2/artist/{id} implementation is written in [ArtistRouter.java](https://github.com/Mohsin0568/com.master.fan/blob/master/artist-info-service/src/main/java/com/master/fan/artist/router/ArtistRouter.java).
3. Used WebClient api to connect with artist, events and venue endpoints.
4. Api will respond back with status code 404 if artist details are not found with given id.
5. Api will respond back with status code 500 if we are unable to connect with any of the three services.
6. Our endpoint will retry 3 times within a time gap of 1 sec if there is failure connecting with upstream services.
7. Retry will happen only for 429 and 5xx error codes.
8. Retry attempts and time gap can be configured in properties file.
9. Have defined response timeout as 10 seconds, again this time can be configured in properties file.
10. Have written integration tests in file [ArtistRouterIntegrationTest.java](https://github.com/Mohsin0568/com.master.fan/blob/master/artist-info-service/src/test/java/com/master/fan/artist/router/ArtistRouterIntegrationTest.java), where I have tested few positives and negetive scenarios.
11. Have used Sleuth library for logs observability.
