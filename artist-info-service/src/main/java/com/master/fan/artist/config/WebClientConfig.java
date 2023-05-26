/**
 * 
 */
package com.master.fan.artist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

/**
 * @author mohsin
 *
 */

@Configuration
public class WebClientConfig {
	
	@Value("${client.rest.timeout.milli.seconds}")
	private int timeout;
	
	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		
		HttpClient client = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout);
		
		return builder
				.clientConnector(new ReactorClientHttpConnector(client))
				.build();
	}

}
