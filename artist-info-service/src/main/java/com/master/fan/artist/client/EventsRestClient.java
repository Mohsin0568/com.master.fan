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

import com.master.fan.artist.domain.Events;
import com.master.fan.artist.util.Constants;

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
	
	Logger log = LoggerFactory.getLogger(EventsRestClient.class);
	
	public Flux<Events> retrieveEvents(){
		
		log.info("going to connect to events service to fetch all events");
		
		return webClient
			.get()
			.uri(eventsRestUrl)
			.retrieve()
			.onStatus(HttpStatus :: is4xxClientError, RestUtil.get4xxFailureEvents(Constants.EVENTS_SERVICE_CALL))
			.onStatus(HttpStatus :: is5xxServerError, RestUtil.get5xxFailureEvents(Constants.EVENTS_SERVICE_CALL))
			.bodyToFlux(Events.class)
			.retryWhen(RestUtil.getRetryforArtistInfoService(retryAttempts, fixedDelay));
			//.log();		
	}

}
