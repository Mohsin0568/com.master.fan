/**
 * 
 */
package com.master.fan.artist.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.master.fan.artist.entity.Venue;

import reactor.core.publisher.Flux;

/**
 * @author mohsin
 * 
 * This class will make a call to venues upstream service to fetch venues data
 *
 */

@Component
public class VenuesRestClient {
	
	@Autowired
	WebClient webClient;
	
	@Value("${client.rest.venues.url}")
	private String venuesRestUrl;
	
	public Flux<Venue> retrieveVenues(){
		
		return webClient
			.get()
			.uri(venuesRestUrl)
			.retrieve()
			.bodyToFlux(Venue.class);
			//.log();		
	}

}
