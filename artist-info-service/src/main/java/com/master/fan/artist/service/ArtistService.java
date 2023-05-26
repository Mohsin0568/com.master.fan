/**
 * 
 */
package com.master.fan.artist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.master.fan.artist.client.ArtistRestClient;
import com.master.fan.artist.dto.ArtistDto;
import com.master.fan.artist.dto.EventsDto;
import com.master.fan.artist.entity.Artist;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mohsin
 * 
 * This class will have business logic related to artists
 *
 */

@Service
public class ArtistService {
	
	@Autowired
	ArtistRestClient restClient;
	
	@Autowired
	EventsService eventsService;
	
	private Flux<Artist> getAllArtistsFromClient(){
		return restClient.retrieveArtists(); //.log();
	}

	public Mono<ArtistDto> getArtistData(String artistId){
		
		return getArtistById(artistId).flatMap(artist -> {			
			
			Flux<EventsDto> events = eventsService.getEventsWithVenuesByArtistId(artistId);
			
			return events.collectList().map(eventsList -> getArtistDTO(artist, eventsList));
		});		
	}
	
	
	public Mono<Artist> getArtistById(String id) {
		Flux<Artist> allArtists = getAllArtistsFromClient();
		return  allArtists.filter(artist -> artist.getId().equals(id)).take(1).next().switchIfEmpty(Mono.error(new RuntimeException("Given id not found")));
	}	
	
	private ArtistDto getArtistDTO(Artist artist, List<EventsDto> events) {		
		
		return ArtistDto.builder()
			.id(artist.getId())
			.name(artist.getName())
			.imgSrc(artist.getImgSrc())
			.url(artist.getUrl())
			.rank(artist.getRank())
			.events(events)
			.build();
	}
}
