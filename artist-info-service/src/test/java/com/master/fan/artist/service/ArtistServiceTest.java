package com.master.fan.artist.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.master.fan.artist.client.ArtistRestClient;
import com.master.fan.artist.domain.Artist;
import com.master.fan.artist.dto.EventsDto;
import com.master.fan.artist.dto.VenueDto;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class ArtistServiceTest {
	
	@Mock
	ArtistRestClient restClient;
	
	@Mock
	EventsService eventsService;
	
	@InjectMocks
	ArtistService artistService;
	
	// fetch artist data with id i and verify if correct details are fetched along with venues and events
	@DisplayName("Fetch Artist Data with id: 1 along Events and Venues")
	@Test
	void test_getArtistData() {
		List<Artist> artists = Arrays.asList(
				getArtistObject("1", "Mohsin", "testImgSrc1", "testURL1", 1),
				getArtistObject("2", "Anderson", "testImgSrc2", "testURL2", 2),
				getArtistObject("3", "Stuart", "testImgSrc3", "testURL3", 3)
				);
		
		List<EventsDto> eventDto = Arrays.asList(
				getEventsDtoObject("1", "event1", "1", "London"),
				getEventsDtoObject("2", "event2", "2", "London")	
				);
		
		when(restClient.retrieveArtists()).thenReturn(Flux.fromIterable(artists));
		when(eventsService.getEventsWithVenuesByArtistId("1")).thenReturn(Flux.fromIterable(eventDto));
		
		var result = artistService.getArtistData("1");
		
		StepVerifier.create(result)
		.consumeNextWith(x -> {
			assertEquals("1", x.getId());
			assertEquals("Mohsin", x.getName());
			assertEquals("testImgSrc1", x.getImgSrc());
			assertEquals("testURL1", x.getUrl());
			assertEquals("1", x.getEvents().get(0).getId());
			assertEquals("2", x.getEvents().get(1).getId());
			assertEquals("1", x.getEvents().get(0).getVenue().getId());
			assertEquals("2", x.getEvents().get(1).getVenue().getId());
		}).verifyComplete();	
	}
	
	// should give error as given id not found in system
	@DisplayName("Fetch artist data for id not in system - expect error")
	@Test()
	void test_getArtistData_whenArtistIdNotFound() {
		
		List<Artist> artists = Arrays.asList(
				getArtistObject("1", "Mohsin", "testImgSrc1", "testURL1", 1),
				getArtistObject("2", "Anderson", "testImgSrc2", "testURL2", 2),
				getArtistObject("3", "Stuart", "testImgSrc3", "testURL3", 3)
				);
		
		when(restClient.retrieveArtists()).thenReturn(Flux.fromIterable(artists));
		
		var result = artistService.getArtistData("4");
		
		StepVerifier.create(result)
			.expectErrorMessage("Artist not found with id 4")
			.verify();
	}

	// fetch artist data with id i and verify if correct details are fetched
	@DisplayName("Fetch Artist Data with id: 1")
	@Test
	void test_getArtistById() {
		
		List<Artist> artists = Arrays.asList(
				getArtistObject("1", "Mohsin", "testImgSrc1", "testURL1", 1),
				getArtistObject("2", "Anderson", "testImgSrc2", "testURL2", 2),
				getArtistObject("3", "Stuart", "testImgSrc3", "testURL3", 3)
				);
		
		when(restClient.retrieveArtists()).thenReturn(Flux.fromIterable(artists));
		
		var result = artistService.getArtistById("1");
		
		StepVerifier.create(result)
			.consumeNextWith(x -> {
				assertEquals("1", x.getId());
				assertEquals("Mohsin", x.getName());
				assertEquals("testImgSrc1", x.getImgSrc());
				assertEquals("testURL1", x.getUrl());
			}).verifyComplete();		
	}
	
	private Artist getArtistObject(String id, String name, String imgSrc, String url, int rank) {
		Artist artist = new Artist();
		artist.setId(id);
		artist.setName(name);
		artist.setImgSrc(imgSrc);
		artist.setUrl(url);
		artist.setRank(rank);
		
		return artist;
	}
	
	private EventsDto getEventsDtoObject(String id, String title, String venueId, String venueName) {
		
		VenueDto venue = VenueDto.builder()
						.id(venueId)
						.name(venueName)
						.city("testcity")
						.url("testUrl")
						.build();
		
		return EventsDto.builder()
					.id(id)
					.dateStatus("singleDate")
					.hiddenFromSearch(true)
					.startDate(LocalDateTime.now())
					.timeZone("Europe/London")
					.title(title)
					.venue(venue)
					.build();		
	}

}
