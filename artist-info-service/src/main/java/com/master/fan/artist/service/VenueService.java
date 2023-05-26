/**
 * 
 */
package com.master.fan.artist.service;

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
 * This class will have business logic related to events
 *
 */

@Service
public class VenueService {
	
	@Autowired
	VenuesRestClient restClient;
	
	public Flux<Venue> fetchAllVenues() {		
		return restClient.retrieveVenues(); //.log();		
	}
	
	public Mono<VenueDto> fetchVenueById(String id){
		
		Flux<Venue> venues = fetchAllVenues();
		return venues.filter(venue -> venue.getId().equalsIgnoreCase(id))
					.next()
					.map(this :: convertVenueToVenueDto);
		
	}
	
	public VenueDto convertVenueToVenueDto(Venue venue) {
		return VenueDto.builder()
					.id(venue.getId())
					.city(venue.getCity())
					.name(venue.getName())
					.url(venue.getUrl())
					.build();
	}

}
