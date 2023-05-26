/**
 * 
 */
package com.master.fan.artist.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.master.fan.artist.handler.ArtistHandler;

/**
 * @author mohsin
 *
 */

@Configuration
public class ArtistRouter {
	
	@Bean
	public RouterFunction<ServerResponse> reviewRoutes(ArtistHandler handler){
		
		return RouterFunctions.route()
				.nest(RequestPredicates.path("/v2/artist"), builder -> {
					builder.GET("/{id}", handler :: getArtistInformation);
				})
				.build();
	}

}
