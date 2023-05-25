/**
 * 
 */
package com.master.fan.artist.service;

import org.springframework.stereotype.Service;

import com.master.fan.artist.dto.Artist;

import reactor.core.publisher.Mono;

/**
 * @author mohsin
 *
 */

@Service
public class ArtistService {

	public Mono<Artist> getArtist(String id){
		Artist artist = new Artist();
		artist.setId("1");
		return Mono.just(artist);
	}
}
