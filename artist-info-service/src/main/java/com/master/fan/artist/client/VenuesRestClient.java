/**
 * 
 */
package com.master.fan.artist.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.master.fan.artist.domain.Venue;
import com.master.fan.artist.util.Constants;

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
	
	Logger log = LoggerFactory.getLogger(VenuesRestClient.class);	
	
	
	public Flux<Venue> retrieveVenues(){		
		log.info("going to connect to venues service to fetch all venues");
		
		return webClient
			.get()
			.uri(venuesRestUrl)
			.retrieve()
			.onStatus(HttpStatus :: is4xxClientError, RestUtil.get4xxFailureEvents(Constants.VENUES_SERVICE_CALL))
			.onStatus(HttpStatus :: is5xxServerError, RestUtil.get5xxFailureEvents(Constants.VENUES_SERVICE_CALL))
			.bodyToFlux(Venue.class)
			.retryWhen(RestUtil.getRetryforArtistInfoService(retryAttempts, fixedDelay));
			//.log();		
	}

}
