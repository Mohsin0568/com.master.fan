/**
 * 
 */
package com.master.fan.artist.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.master.fan.artist.entity.Artist;

import reactor.core.publisher.Flux;

/**
 * @author mohsin
 *
 */

@Component
public class ArtistRestClient {
	
	@Autowired
	WebClient webClient;
	
	@Value("${client.rest.artist.url}")
	private String artistRestUrl;
	
	public Flux<Artist> retrieveArtists(){
		
		return webClient
			.get()
			.uri(artistRestUrl)
			.retrieve()
			.bodyToFlux(Artist.class);
			//.log();		
	}
}
