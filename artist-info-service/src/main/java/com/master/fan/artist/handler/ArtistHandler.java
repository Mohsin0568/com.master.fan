/**
 * 
 */
package com.master.fan.artist.handler;

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
	
	public Mono<ServerResponse> getArtistInformation(ServerRequest request) {
		
		String id = request.pathVariable("id");
		
		var response = artistService.getArtistData(id);
		return ServerResponse.ok().body(response, ArtistDto.class);
		
	}

}
