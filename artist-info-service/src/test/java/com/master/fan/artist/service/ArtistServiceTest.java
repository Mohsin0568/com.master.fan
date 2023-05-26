package com.master.fan.artist.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.master.fan.artist.client.ArtistRestClient;
import com.master.fan.artist.entity.Artist;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class ArtistServiceTest {
	
	@Mock
	ArtistRestClient restClient;
	
	@InjectMocks
	ArtistService artistService;
	
	@Test
	void getArtistDataTest() {
		List<Artist> artists = Arrays.asList(
				getArtistObject("1", "Mohsin", "testImgSrc1", "testURL1", 1),
				getArtistObject("2", "Anderson", "testImgSrc2", "testURL2", 2),
				getArtistObject("3", "Stuart", "testImgSrc3", "testURL3", 3)
				);
		
		when(restClient.retrieveArtists()).thenReturn(Flux.fromIterable(artists));
		
		var result = artistService.getArtistData("1");
		
		StepVerifier.create(result)
		.consumeNextWith(x -> {
			assertEquals("1", x.getId());
			assertEquals("Mohsin", x.getName());
			assertEquals("testImgSrc1", x.getImgSrc());
			assertEquals("testURL1", x.getUrl());
		});	
	}
	
	@Test()
	void getArtistDataTest_whenArtistIdNotFound() {
		
		List<Artist> artists = Arrays.asList(
				getArtistObject("1", "Mohsin", "testImgSrc1", "testURL1", 1),
				getArtistObject("2", "Anderson", "testImgSrc2", "testURL2", 2),
				getArtistObject("3", "Stuart", "testImgSrc3", "testURL3", 3)
				);
		
		when(restClient.retrieveArtists()).thenReturn(Flux.fromIterable(artists));
		
		var result = artistService.getArtistData("4");
		
		StepVerifier.create(result)
			.expectErrorMessage("Given id not found")
			.verify();
	}

	@Test
	void getArtistByIdTest() {
		
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
			});		
	}
	
	@Test()
	void getArtistByIdTest_whenIdNotFound() {
		
		List<Artist> artists = Arrays.asList(
				getArtistObject("1", "Mohsin", "testImgSrc1", "testURL1", 1),
				getArtistObject("2", "Anderson", "testImgSrc2", "testURL2", 2),
				getArtistObject("3", "Stuart", "testImgSrc3", "testURL3", 3)
				);
		
		when(restClient.retrieveArtists()).thenReturn(Flux.fromIterable(artists));
		
		var result = artistService.getArtistById("4");
		
		StepVerifier.create(result)
			.expectErrorMessage("Given id not found")
			.verify();
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

}
