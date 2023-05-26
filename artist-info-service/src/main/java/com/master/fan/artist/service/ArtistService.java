/**
 * 
 */
package com.master.fan.artist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.master.fan.artist.client.ArtistRestClient;
import com.master.fan.artist.dto.ArtistDto;
import com.master.fan.artist.entity.Artist;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mohsin
 * 
 * 
 *
 */

@Service
public class ArtistService {
	
	@Autowired
	ArtistRestClient restClient;

	public Mono<ArtistDto> getArtistData(String id){
		
		return getArtistById(id).flatMap(artist -> {			
			return Mono.just(getArtistDTO(artist));
		});		
	}
	
	
	public Mono<Artist> getArtistById(String id) {
		Flux<Artist> allArtists = getAllArtistsFromClient();
		return  allArtists.filter(artist -> artist.getId().equals(id)).take(1).next().switchIfEmpty(Mono.error(new RuntimeException("Given id not found")));
	}	
	
	private Flux<Artist> getAllArtistsFromClient(){
		return restClient.retrieveArtists();
	}
	
	private ArtistDto getArtistDTO(Artist artist) {
		return ArtistDto.builder()
				.id(artist.getId())
				.name(artist.getName())
				.imgSrc(artist.getImgSrc())
				.url(artist.getUrl())
				.rank(artist.getRank())
				.build();
	}
}
