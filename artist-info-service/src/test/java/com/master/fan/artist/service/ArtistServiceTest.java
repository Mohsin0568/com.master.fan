package com.master.fan.artist.service;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class ArtistServiceTest {
	
	@InjectMocks
	ArtistService artistService;

	@Test
	void getArtistTest_sendValidIdInArgument() {
		
		var artist = artistService.getArtist("1");
		StepVerifier.create(artist)
			.consumeNextWith(x -> {
				assertEquals("1", x.getId());
			});
		
	}

}
