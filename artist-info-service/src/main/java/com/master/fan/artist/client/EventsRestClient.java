/**
 * 
 */
package com.master.fan.artist.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.master.fan.artist.entity.Events;

import reactor.core.publisher.Flux;

/**
 * @author mohsin
 * 
 * This class will make a call to events upstream service to fetch events data
 *
 */

@Component
public class EventsRestClient {
	
	@Autowired
	WebClient webClient;
	
	@Value("${client.rest.events.url}")
	private String eventsRestUrl;
	
	@Value("${client.rest.retryAttempts:3}")
	private int retryAttempts;
	
	@Value("${client.rest.fixedDelay:1}")
	private int fixedDelay;
	
	public Flux<Events> retrieveEvents(){
		
		return webClient
			.get()
			.uri(eventsRestUrl)
			.retrieve()
			.onStatus(HttpStatus :: is4xxClientError, RestUtil.get4xxFailureEvents())
			.onStatus(HttpStatus :: is5xxServerError, RestUtil.get5xxFailureEvents())
			.bodyToFlux(Events.class)
			.retryWhen(RestUtil.getRetryforArtistInfoService(retryAttempts, fixedDelay));
			//.log();		
	}

}
