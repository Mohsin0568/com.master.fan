/**
 * 
 */
package com.master.fan.artist.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.master.fan.artist.entity.Artist;

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
	
	
	public Flux<Artist> retrieveArtists(){
		
		return webClient
			.get()
			.uri(artistRestUrl)
			.retrieve()
			.onStatus(HttpStatus :: is4xxClientError, RestUtil.get4xxFailureEvents())
			.onStatus(HttpStatus :: is5xxServerError, RestUtil.get5xxFailureEvents())
			.bodyToFlux(Artist.class)
			.retryWhen(RestUtil.getRetryforArtistInfoService(retryAttempts, fixedDelay));
			//.log();		
	}
}
