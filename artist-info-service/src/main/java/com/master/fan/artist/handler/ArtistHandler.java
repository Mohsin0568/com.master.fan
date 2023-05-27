/**
 * 
 */
package com.master.fan.artist.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.master.fan.artist.dto.ArtistDto;
import com.master.fan.artist.service.ArtistService;

import reactor.core.publisher.Mono;

/**
 * @author mohsin
 *
 */

@Component
public class ArtistHandler {
	
	@Autowired
	ArtistService artistService;
	
	Logger log = LoggerFactory.getLogger(ArtistHandler.class);
	
	public Mono<ServerResponse> getArtistInformation(ServerRequest request) {		
		
		String id = request.pathVariable("id");
		
		log.info("Request received to fetch artist details with id {} ", id);
		
		var response = artistService.getArtistData(id);
		
		log.info("Successfully fetched artistDto with events information");
		return ServerResponse.ok().body(response, ArtistDto.class);
		
	}

}
