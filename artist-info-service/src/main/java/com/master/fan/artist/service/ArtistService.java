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
import com.master.fan.artist.exceptions.ArtistNotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mohsin
 * 
 * ArtistService will have business logic related to artist information
 *
 */

@Service
public class ArtistService {
	
	@Autowired
	private ArtistRestClient restClient;
	
	@Autowired
	private EventsService eventsService;
	
	/**
	 * 
	 * @return Flux<Artist>, this will return all artists from artist service.
	 * 
	 * This method will connect with Artist service and fetch all artist data from it.
	 */
	private Flux<Artist> getAllArtistsFromClient(){
		return restClient.retrieveArtists(); //.log();
	}

	/**
	 * 
	 * @param artistId
	 * @return Mono<ArtistDto>, this will return ArtistDto along with event and venue information.
	 * 
	 * This method fetches artist information by id, then it will fetch events and venues data for that artist.
	 */
	public Mono<ArtistDto> getArtistData(String artistId){
		
		return getArtistById(artistId).flatMap(artist -> {			
			
			// get Events Data
			Flux<EventsDto> events = eventsService.getEventsWithVenuesByArtistId(artistId);
			
			// map artist, events and venues data to DTO
			return events.collectList().map(eventsList -> getArtistDTO(artist, eventsList));
		});		
	}
	
	/**
	 * 
	 * @param id
	 * @return Mono<Artist>, return Artist
	 * 
	 * This method filters and returns id artist information based on id given in parameter.
	 * This will throw exception if artist with given id not found.
	 */
	public Mono<Artist> getArtistById(String id) {
		Flux<Artist> allArtists = getAllArtistsFromClient();
		return  allArtists.filter(artist -> artist.getId().equals(id)).take(1).next().switchIfEmpty(Mono.error(new ArtistNotFoundException("Artist not found with id " + id)));
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
