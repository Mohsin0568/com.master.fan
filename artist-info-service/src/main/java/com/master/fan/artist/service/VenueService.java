/**
 * 
 */
package com.master.fan.artist.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.master.fan.artist.client.VenuesRestClient;
import com.master.fan.artist.dto.VenueDto;
import com.master.fan.artist.entity.Venue;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mohsin
 * 
 * VenueService will have business logic related to venues.
 *
 */

@Service
public class VenueService {
	
	@Autowired
	private VenuesRestClient restClient;
	
	Logger log = LoggerFactory.getLogger(VenueService.class);
	
	/**
	 * 
	 * @return Flux<Venue>, this will return all venues fetched from venues service.
	 * 
	 * This method will connect with Venues service and fetch all venus information from it.
	 */
	public Flux<Venue> fetchAllVenues() {		
		return restClient.retrieveVenues(); //.log();		
	}
	
	/**
	 * 
	 * @param id
	 * @return Mono<VenueDto>, return venue
	 * 
	 * This method will return VenueDto for the given id, should return empty if now Venue is found
	 */
	public Mono<VenueDto> fetchVenueById(String id){
		
		Flux<Venue> venues = fetchAllVenues(); // fetch all venues from venues service
		
		log.info("Fetched all venues, now will filter venue with id {} and will send back", id);
		
		return venues.filter(venue -> venue.getId().equalsIgnoreCase(id)) // filter venue by id from all venues
					.next()
					.map(this :: convertVenueToVenueDto);
		
	}
	
	private VenueDto convertVenueToVenueDto(Venue venue) {
		return VenueDto.builder()
					.id(venue.getId())
					.city(venue.getCity())
					.name(venue.getName())
					.url(venue.getUrl())
					.build();
	}

}
