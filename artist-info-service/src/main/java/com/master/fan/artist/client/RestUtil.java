/**
 * 
 */
package com.master.fan.artist.client;

import java.time.Duration;
import java.util.function.Function;

import org.springframework.web.reactive.function.client.ClientResponse;

import com.master.fan.artist.exceptions.ArtistServiceClientException;
import com.master.fan.artist.exceptions.ArtistServiceServerException;

import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * @author mohsin
 *
 */
public class RestUtil {
	
	public static Retry getRetryforArtistInfoService(int retryAttempts, int fixedDelay) {
		
		return Retry.fixedDelay(retryAttempts, Duration.ofSeconds(fixedDelay))
				.filter(e -> e instanceof ArtistServiceServerException) // retry will happen only if exception is of type ArtistInfoServerException, there is no point of retrying if the exception is of 4xx
				.onRetryExhaustedThrow((retryBackOffSpec, retrySignal) -> { // this line will propagate same exception thrown by the server.
					return Exceptions.propagate(retrySignal.failure());
				});
	}
	
	public static Function<ClientResponse, Mono<? extends Throwable>> get4xxFailureEvents(){
		
		return clientResponse -> {
			
			if(clientResponse.statusCode().value() == 429) { // will retry for this error code
				
				return Mono.error(
						new ArtistServiceServerException("Service Unavailable at the moment")
				);
			}
			
			return clientResponse.bodyToMono(String.class) // will not retry for remaining error codes.
				.flatMap(responseBody -> {
					return Mono.error(
							new ArtistServiceClientException("Service Unavailable at the moment", clientResponse.statusCode().value())
						);
				});
		};
		
	}
	
	public static Function<ClientResponse, Mono<? extends Throwable>> get5xxFailureEvents(){
		return clientResponse -> { // will retry for all 500 errors
			
			return clientResponse.bodyToMono(String.class)
				.flatMap(responseBody -> {
					return Mono.error(
							new ArtistServiceServerException("Service Unavailable at the moment")
						);
				});
		};
	}

}
