package com.master.fan.artist.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.master.fan.artist.dto.Artist;
import com.master.fan.artist.service.ArtistService;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ArtistController.class)
@AutoConfigureWebTestClient
public class ArtistControllerTest{
	
	@Autowired
	WebTestClient webTestClient;
	
	@MockBean
	ArtistService artistService;
	
	private final static String ARTIST_CONTROLLER_URL = "/v1/artist";
	
	@Test
	public void getArtistInfoByIdTest() {
		
		Artist artist = new Artist("1", "testArist", "testImageSource", "testUrl", 1);
		
		when(artistService.getArtist("1")).thenReturn(Mono.just(artist));
		
		webTestClient
			.get()
			.uri(ARTIST_CONTROLLER_URL+"/1")
			.exchange()
			.expectStatus()
			.is2xxSuccessful()
			.expectBody()
			.jsonPath("$.id", "1");
	}
	
}