/**
 * 
 */
package com.master.fan.artist.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

/**
 * @author mohsin
 *
 */

@Configuration
public class WebClientConfig {
	
	@Value("${client.rest.timeout.seconds}")
	private int timeout;
	
	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		
		HttpClient httpClient = HttpClient.create()
				.responseTimeout(Duration.ofSeconds(timeout));
		
		return builder
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
	}

}
