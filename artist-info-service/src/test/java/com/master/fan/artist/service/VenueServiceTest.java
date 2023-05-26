package com.master.fan.artist.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.master.fan.artist.client.VenuesRestClient;
import com.master.fan.artist.entity.Venue;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class VenueServiceTest {
	
	@Mock
	private VenuesRestClient restClient;
	
	@InjectMocks
	private VenueService venueService;

	
	// fetch all venues from restclient and verify count and results
	@DisplayName("Fetch all venues")
	@Test
	void test_fetchAllVenues() {
		List<Venue> venues = Arrays.asList(
				getVenueObject("1", "O2 Academy Sheffield", "Sheffield"),
				getVenueObject("2", "O2 Institute2 Birmingham", "Birmingham")
			);
		
		when(restClient.retrieveVenues()).thenReturn(Flux.fromIterable(venues));
		
		var result = venueService.fetchAllVenues();
		
		StepVerifier.create(result)
			.expectNextCount(2).verifyComplete();		
		
		StepVerifier.create(result)
			.consumeNextWith(x -> {
				assertEquals("1", x.getId());
				assertEquals("O2 Academy Sheffield", x.getName());
				assertEquals("Sheffield", x.getCity());
			})
			.consumeNextWith(x -> {
				assertEquals("2", x.getId());
				assertEquals("O2 Institute2 Birmingham", x.getName());
				assertEquals("Birmingham", x.getCity());
			})
			.verifyComplete();
	}
	
	// verify if we are able to get correct venue details with id 1
	@DisplayName("Fetch venue by id - 1")
	@Test
	void test_fetchVenueById() {
		List<Venue> venues = Arrays.asList(
				getVenueObject("1", "O2 Academy Sheffield", "Sheffield"),
				getVenueObject("2", "O2 Institute2 Birmingham", "Birmingham")
			);
		
		when(restClient.retrieveVenues()).thenReturn(Flux.fromIterable(venues));
		
		var result = venueService.fetchVenueById("1");
		
		StepVerifier.create(result)
			.expectNextCount(1).verifyComplete();
		
		StepVerifier.create(result)
		.consumeNextWith(x -> {
			assertEquals("1", x.getId());
			assertEquals("O2 Academy Sheffield", x.getName());
			assertEquals("Sheffield", x.getCity());
		})
		.verifyComplete();
	}
	
	private Venue getVenueObject(String id, String name, String city) {
		Venue venue = new Venue();
		venue.setId(id);
		venue.setName(name);
		venue.setCity(city);
		return venue;
	}

}
