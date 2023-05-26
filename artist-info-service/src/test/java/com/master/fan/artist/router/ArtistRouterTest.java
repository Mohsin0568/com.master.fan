package com.master.fan.artist.router;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.master.fan.artist.dto.ArtistDto;
import com.master.fan.artist.dto.EventsDto;
import com.master.fan.artist.dto.VenueDto;
import com.master.fan.artist.handler.ArtistHandler;
import com.master.fan.artist.service.ArtistService;

import reactor.core.publisher.Mono;

@WebFluxTest
@ContextConfiguration(classes = {ArtistRouter.class, ArtistHandler.class})
@AutoConfigureWebTestClient
class ArtistRouterTest {
	
	@MockBean
	private ArtistService artistService;
	
	@Autowired
	private WebTestClient webTestClient;
	
	private final static String ARTIST_CONTROLLER_URL = "/v2/artist";

	@Test
	void getArtistInfoByIdTest() {
		
		when(artistService.getArtistData("1")).thenReturn(Mono.just(getArtistDtoObject()));
		
		webTestClient
			.get()
			.uri(ARTIST_CONTROLLER_URL+"/1")			
			.exchange()
			.expectStatus()
			.is2xxSuccessful()			
			.expectBody(ArtistDto.class)
			.consumeWith(entityExchangeResult -> {			
				var artist = entityExchangeResult.getResponseBody();
				assert artist != null;
								
				assertEquals("Mohsin", artist.getName());
				assertEquals("O2 Academy Sheffield", artist.getEvents().get(0).getVenue().getName());
				assertEquals("Fusion Prog", artist.getEvents().get(0).getTitle());
				
			});
		
	}
	
	
	private ArtistDto getArtistDtoObject() {
		VenueDto venue = VenueDto.builder()
				.id("1")
				.name("O2 Academy Sheffield")
				.url("/o2-academy-sheffield-tickets-sheffield/venue/41")
				.city("Sheffield")
				.build();
	
		EventsDto eventDto = EventsDto.builder()
				.title("Fusion Prog")
				.id("1")
				.dateStatus("singleDate")
				.venue(venue)
				.build();
	
		return ArtistDto.builder()
			.id("1")
			.name("Mohsin")
			.imgSrc("//some-base-url/colosseum.jpg")
			.url("/colosseum-tickets/artist/22")
			.rank(1)
			.events(Arrays.asList(eventDto))
			.build();
	}

}
