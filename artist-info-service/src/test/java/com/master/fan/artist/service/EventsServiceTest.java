package com.master.fan.artist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.master.fan.artist.client.EventsRestClient;
import com.master.fan.artist.domain.Artist;
import com.master.fan.artist.domain.Events;
import com.master.fan.artist.domain.Venue;
import com.master.fan.artist.dto.EventsDto;
import com.master.fan.artist.dto.VenueDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class EventsServiceTest {
	
	@Mock
	private EventsRestClient restClient;
	
	@Mock
	private VenueService venueService;
	
	@InjectMocks
	private EventsService eventsService;
	
	// this will fetch all events and will verify
	@DisplayName("Fetch all events")
	@Test
	void test_getAllEvents() {
		
		List<Events> events = Arrays.asList(
				getEventsObject("1", "title1", "1"),
				getEventsObject("2", "title2", "1")
			); 
		when(restClient.retrieveEvents()).thenReturn(Flux.fromIterable(events));
		
		Flux<Events> result = eventsService.getAllEvents();
		
		StepVerifier.create(result)
			.expectNextCount(2).verifyComplete();
		
		StepVerifier.create(result)
			.consumeNextWith(x -> {
				assertEquals("1", x.getId());
				assertEquals("title1", x.getTitle());
			})
			.consumeNextWith(x -> {
				assertEquals("2", x.getId());
				assertEquals("title2", x.getTitle());
			})
			.verifyComplete();
		
	}
	
	// this will test if our service is returning correct events of artist with id - 1
	@DisplayName("Fetch all events of artist with id - 1")
	@Test
	void test_getEventsByArtistId() {
		List<Events> events = Arrays.asList(
				getEventsObject("1", "title1", "1"),
				getEventsObject("2", "title2", "2"),
				getEventsObject("3", "title3", "1")
			);
		when(restClient.retrieveEvents()).thenReturn(Flux.fromIterable(events));
		Flux<EventsDto> result = eventsService.getEventsByArtistId("1");
		
		StepVerifier.create(result)
		.expectNextCount(2).verifyComplete();		
		
		StepVerifier.create(result)
			//.expectNextCount(2)
			.consumeNextWith(x -> {
				assertEquals("1", x.getId());
				assertEquals("title1", x.getTitle());
				assertNull(x.getVenue());
			})
			.consumeNextWith(x -> {
				assertEquals("3", x.getId());
				assertEquals("title3", x.getTitle());
				assertNull(x.getVenue());
			})
			.verifyComplete();
	}
	
	// this will test if our service is returning correct events with venues of artist with id - 1
	@DisplayName("Fetch all events of artist with id - 1 along with venue details")
	@Test
	void test_getEventsWithVenuesByArtistId() {
		List<Events> events = Arrays.asList(
				getEventsObject("1", "title1", "1")
			);
		
		when(restClient.retrieveEvents()).thenReturn(Flux.fromIterable(events));
		when(venueService.fetchVenueById(Mockito.anyString())).thenReturn(Mono.just(getVenueDtoObject()));
		Flux<EventsDto> result = eventsService.getEventsWithVenuesByArtistId("1");
		
		StepVerifier.create(result)
		.expectNextCount(1).verifyComplete();
		
		StepVerifier.create(result)
		.consumeNextWith(x -> {
			assertEquals("1", x.getId());
			assertEquals("title1", x.getTitle());
			assertEquals("1", x.getVenue().getId());
			assertEquals("London", x.getVenue().getCity());
			assertEquals("O2 Academy Sheffield", x.getVenue().getName());
		})
		.verifyComplete();
	}
	
	private Events getEventsObject(String eventId, String title, String artistId ) {
		
		Artist artist = new Artist();
		artist.setId(artistId);
		
		Venue venue = new Venue();
		venue.setId("1");
		
		Events event = new Events();
		event.setId(eventId);
		event.setTitle(title);
		event.setDateStatus("");
		event.setHiddenFromSearch(true);
		event.setStartDate(LocalDateTime.now());
		event.setTimeZone("Europe/London");
		event.setVenue(venue);
		event.setArtists(Arrays.asList(artist));
		
		return event;
	}
	
	private VenueDto getVenueDtoObject() {
		return VenueDto.builder()
				.id("1")
				.city("London")
				.name("O2 Academy Sheffield")
				.url("testUrl")
				.build();
	}

}
