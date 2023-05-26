/**
 * 
 */
package com.master.fan.artist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.master.fan.artist.dto.ArtistDto;
import com.master.fan.artist.service.ArtistService;

import reactor.core.publisher.Mono;

/**
 * @author mohsin
 *
 */

@RestController
@RequestMapping("/v1/artist")
public class ArtistController {
	
	@Autowired
	ArtistService artistService;
	
	@GetMapping("/{id}")
	public Mono<ArtistDto> getArtistInfoById(@PathVariable String id){
		return artistService.getArtistData(id);
	}
	

}
