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

import com.master.fan.artist.entity.Artist;
import com.master.fan.artist.util.Constants;

import reactor.core.publisher.Flux;

/**
 * @author mohsin
 * 
 * This class will make a call to artists upstream service to fetch artists information
 *
 */

@Component
public class ArtistRestClient {
	
	@Autowired
	WebClient webClient;
	
	@Value("${client.rest.artist.url}")
	private String artistRestUrl;
	
	@Value("${client.rest.retryAttempts:3}")
	private int retryAttempts;
	
	@Value("${client.rest.fixedDelay:1}")
	private int fixedDelay;
	
	Logger log = LoggerFactory.getLogger(ArtistRestClient.class);
	
	public Flux<Artist> retrieveArtists(){
		log.info("going to connect to artist service to fetch all artists");
		return webClient
			.get()
			.uri(artistRestUrl)
			.retrieve()
			.onStatus(HttpStatus :: is4xxClientError, RestUtil.get4xxFailureEvents(Constants.ARTIST_SERVICE_CALL))
			.onStatus(HttpStatus :: is5xxServerError, RestUtil.get5xxFailureEvents(Constants.ARTIST_SERVICE_CALL))
			.bodyToFlux(Artist.class)
			.retryWhen(RestUtil.getRetryforArtistInfoService(retryAttempts, fixedDelay));
			//.log();		
	}
}
