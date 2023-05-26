/**
 * 
 */
package com.master.fan.artist.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * @author mohsin
 * 
 * This class will handle errors thrown by the endpoints
 *
 */

@Component
public class GlobalErrorHandler implements ErrorWebExceptionHandler{
	
	Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);
	
	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		
		log.error("Exception Message is : {} ", ex.getMessage());
		
		DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        var errorMessage = bufferFactory.wrap(ex.getMessage().getBytes());
        
        if(ex instanceof ArtistNotFoundException){
        	exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return exchange.getResponse().writeWith(Mono.just(errorMessage));
        }
        
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return exchange.getResponse().writeWith(Mono.just(errorMessage));
	}

}
