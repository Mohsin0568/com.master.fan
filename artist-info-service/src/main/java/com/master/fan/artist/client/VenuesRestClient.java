/**
 * 
 */
package com.master.fan.artist.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
	
	@Value("${client.rest.retryAttempts:3}")
	private int retryAttempts;
	
	@Value("${client.rest.fixedDelay:1}")
	private int fixedDelay;
	
	public Flux<Venue> retrieveVenues(){
		
		return webClient
			.get()
			.uri(venuesRestUrl)
			.retrieve()
			.onStatus(HttpStatus :: is4xxClientError, RestUtil.get4xxFailureEvents())
			.onStatus(HttpStatus :: is5xxServerError, RestUtil.get5xxFailureEvents())
			.bodyToFlux(Venue.class)
			.retryWhen(RestUtil.getRetryforArtistInfoService(retryAttempts, fixedDelay));
			//.log();		
	}

}
