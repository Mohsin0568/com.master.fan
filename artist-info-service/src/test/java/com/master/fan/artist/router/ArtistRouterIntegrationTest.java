/**
 * 
 */
package com.master.fan.artist.router;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.master.fan.artist.dto.ArtistDto;

/**
 * @author mohsin
 *
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084) // spins up the http server in the port 8084
@TestPropertySource(
		properties = {
				"client.rest.artist.url=http://localhost:8084/artists.json",
				"client.rest.events.url=http://localhost:8084/events.json",
				"client.rest.venues.url=http://localhost:8084/venues.json",
				"client.rest.timeout.seconds=10"
		}
	)
public class ArtistRouterIntegrationTest {
	
	@Autowired
    WebTestClient webTestClient;
	
	@DisplayName("Get Artist by id 21 with events and venues")
	@Test
	void test_getArtistById() {
		stubFor(get(urlEqualTo("/artists.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("artistinfo.json")));
		
		stubFor(get(urlEqualTo("/events.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("events.json")));
		
		stubFor(get(urlEqualTo("/venues.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("venues.json")));
		
		webTestClient.get()
	        .uri("/v2/artist/{id}", "21")
	        .exchange()
	        .expectStatus().isOk()
	        .expectBody(ArtistDto.class)
	        .consumeWith(entityExchangeResult -> {
	        	var artistInfo = entityExchangeResult.getResponseBody();
	        	assertEquals("HRH Prog", artistInfo.getName());
	        	assertEquals(1, artistInfo.getRank());
	        	assertEquals(3, artistInfo.getEvents().size()); // assert if we get correct number of events or not
	        	
	        	assertTrue(artistInfo.getEvents().get(0).getId().equals("1") ||
	        			artistInfo.getEvents().get(1).getId().equals("1") ||
	        			artistInfo.getEvents().get(2).getId().equals("1"));
	        	
	        	assertTrue(artistInfo.getEvents().get(0).getId().equals("7") ||
	        			artistInfo.getEvents().get(1).getId().equals("7") ||
	        			artistInfo.getEvents().get(2).getId().equals("7"));
	        	
	        	assertTrue(artistInfo.getEvents().get(0).getId().equals("13") ||
	        			artistInfo.getEvents().get(1).getId().equals("13") ||
	        			artistInfo.getEvents().get(2).getId().equals("13"));
	        	
//	        	// assert 1st event details and its venue
//	        	assertEquals("Fusion Prog", artistInfo.getEvents().get(0).getTitle());
//	        	assertEquals("1", artistInfo.getEvents().get(0).getId());
//				assertEquals("41", artistInfo.getEvents().get(0).getVenue().getId());
//				assertEquals("O2 Academy Sheffield", artistInfo.getEvents().get(0).getVenue().getName());
//				assertEquals("Sheffield", artistInfo.getEvents().get(0).getVenue().getCity());
//				
//				// assert 2nd event details and its venue
//	        	assertEquals("A festival Live", artistInfo.getEvents().get(1).getTitle());
//	        	assertEquals("7", artistInfo.getEvents().get(1).getId());
//				assertEquals("45", artistInfo.getEvents().get(1).getVenue().getId());
//				assertEquals("O2 Academy Brixton", artistInfo.getEvents().get(1).getVenue().getName());
//				assertEquals("London", artistInfo.getEvents().get(1).getVenue().getCity());
//				
//				// assert 3rd event details and its venue
//	        	assertEquals("Huge Live", artistInfo.getEvents().get(2).getTitle());
//	        	assertEquals("13", artistInfo.getEvents().get(2).getId());
//				assertEquals("41", artistInfo.getEvents().get(2).getVenue().getId());
//				assertEquals("O2 Academy Sheffield", artistInfo.getEvents().get(2).getVenue().getName());
//				assertEquals("Sheffield", artistInfo.getEvents().get(2).getVenue().getCity());
	         }
        );
	}
	
	@DisplayName("Get Artist by id 2 - Arstist not found")
	@Test
	void test_getArtistById_ArtistNotFoundWithGivenId() {
		stubFor(get(urlEqualTo("/artists.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("artistinfo.json")));
		
		webTestClient.get()
        .uri("/v2/artist/{id}", "2")
        .exchange()
        .expectStatus().isNotFound();		
	}
	
	@DisplayName("Get Artist by id 27, this artist does not have any events")
	@Test
	void test_getArtistById_withNoEvents() {
		stubFor(get(urlEqualTo("/artists.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("artistinfo.json")));
		
		stubFor(get(urlEqualTo("/events.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("events.json")));
		
		webTestClient.get()
	        .uri("/v2/artist/{id}", "27")
	        .exchange()
	        .expectStatus().isOk()
	        .expectBody(ArtistDto.class)
	        .consumeWith(entityExchangeResult -> {
	        	var artistInfo = entityExchangeResult.getResponseBody();
	        	assertEquals("The Crazy World of Arthur Brown", artistInfo.getName());
	        	assertEquals(7, artistInfo.getRank());
	        	assertEquals(0, artistInfo.getEvents().size()); // assert if we get correct number of events or not
	         }
        );
	}
	
	@DisplayName("Artist Service throws 500")
	@Test
	void test_getArtistById_whenArtistServiceThrows500() {
		
		stubFor(get(urlEqualTo("/artists.json"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("artist-info Service Unavailable")));
		
		webTestClient.get()
	        .uri("/v2/artist/{id}", "21")
	        .exchange()
	        .expectStatus()
	        .is5xxServerError()
	        .expectBody(String.class)
	        .value(message -> {
	            assertEquals("Service Unavailable at the moment", message);
	        });
		
	}
	
	@DisplayName("Events Service throws 500")
	@Test
	void test_getArtistById_whenEventsServiceThrows500() {
		
		stubFor(get(urlEqualTo("/artists.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("artistinfo.json")));
		
		stubFor(get(urlEqualTo("/events.json"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("events-info Service Unavailable")));
		
		webTestClient.get()
	        .uri("/v2/artist/{id}", "21")
	        .exchange()
	        .expectStatus()
	        .is5xxServerError()
	        .expectBody(String.class)
	        .value(message -> {
	            assertEquals("Service Unavailable at the moment", message);
	        });		
	}
	
	@DisplayName("Venue Service throws 500")
	@Test
	void test_getArtistById_whenVenueServiceThrows500() {
		
		stubFor(get(urlEqualTo("/artists.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("artistinfo.json")));
		
		stubFor(get(urlEqualTo("/events.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("events.json")));
		
		stubFor(get(urlEqualTo("/venues.json"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("venues-info Service Unavailable")));
		
		webTestClient.get()
	        .uri("/v2/artist/{id}", "21")
	        .exchange()
	        .expectStatus()
	        .is5xxServerError()
	        .expectBody(String.class)
	        .value(message -> {
	            assertEquals("Service Unavailable at the moment", message);
	        });		
	}
	
	@DisplayName("Artist Service throws 404")
	@Test
	void test_getArtistById_whenArtistServiceThrows404() {
		
		stubFor(get(urlEqualTo("/artists.json"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("artist-info Service Unavailable")));
		
		webTestClient.get()
	        .uri("/v2/artist/{id}", "21")
	        .exchange()
	        .expectStatus()
	        .is5xxServerError()
	        .expectBody(String.class)
	        .value(message -> {
	            assertEquals("Service Unavailable at the moment", message);
	        });
		
	}
	
	@DisplayName("Events Service throws 404")
	@Test
	void test_getArtistById_whenEventsServiceThrows404() {
		
		stubFor(get(urlEqualTo("/artists.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("artistinfo.json")));
		
		stubFor(get(urlEqualTo("/events.json"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("events-info Service Unavailable")));
		
		webTestClient.get()
	        .uri("/v2/artist/{id}", "21")
	        .exchange()
	        .expectStatus()
	        .is5xxServerError()
	        .expectBody(String.class)
	        .value(message -> {
	            assertEquals("Service Unavailable at the moment", message);
	        });		
	}
	
	@DisplayName("Venue Service throws 404")
	@Test
	void test_getArtistById_whenVenueServiceThrows404() {
		
		stubFor(get(urlEqualTo("/artists.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("artistinfo.json")));
		
		stubFor(get(urlEqualTo("/events.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("events.json")));
		
		stubFor(get(urlEqualTo("/venues.json"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("venues-info Service Unavailable")));
		
		webTestClient.get()
	        .uri("/v2/artist/{id}", "21")
	        .exchange()
	        .expectStatus()
	        .is5xxServerError()
	        .expectBody(String.class)
	        .value(message -> {
	            assertEquals("Service Unavailable at the moment", message);
	        });		
	}

}
