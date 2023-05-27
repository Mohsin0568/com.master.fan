/**
 * 
 */
package com.master.fan.artist.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	Logger log = LoggerFactory.getLogger(ArtistService.class);
	
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
			
			log.info("Successfully fetched artist data with id {}", artistId);
			
			// get Events Data
			Flux<EventsDto> events = eventsService.getEventsWithVenuesByArtistId(artistId);
			
			log.info("succesfully fetched events data in artist service layer");
			
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
		
		return  allArtists
					.filter(artist -> artist.getId().equals(id)) // this will filter request artist from all artists
					.take(1) // this will take the first artist which matches the filter
					.next() // this will convert Flux to Mono
					.switchIfEmpty(Mono.error(new ArtistNotFoundException("Artist not found with id " + id))); // throw exception if artist not found
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
