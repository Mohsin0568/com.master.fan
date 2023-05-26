/**
 * 
 */
package com.master.fan.artist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.master.fan.artist.client.EventsRestClient;
import com.master.fan.artist.dto.EventsDto;
import com.master.fan.artist.dto.VenueDto;
import com.master.fan.artist.entity.Events;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mohsin
 * 
 * This class will have business logic related to events
 *
 */

@Service
public class EventsService {
	
	@Autowired
	EventsRestClient restClient;
	
	@Autowired
	VenueService venueService;
	
	public Flux<Events> getAllEvents(){
		return restClient.retrieveEvents(); //.log();
	}
	
	public Flux<EventsDto> getEventsByArtistId(String artistId){		
		
		return getAllEvents()
				.filter(event -> event.getArtists()
									.stream()
									.anyMatch(artist -> artist.getId().equalsIgnoreCase(artistId))
				).map(this :: convertEventToEventDto);
	}
	
	public Flux<EventsDto> getEventsWithVenuesByArtistId(String artistId){
		
		return getAllEvents()
				.filter(event -> event.getArtists()
									.stream()
									.anyMatch(artist -> artist.getId().equalsIgnoreCase(artistId))
				).flatMap(event -> {
						Mono<VenueDto> eventVenue = venueService.fetchVenueById(event.getVenue().getId());
						return eventVenue.map(venue -> convertEventToEventDto(event, venue));
					
				});		
	}
	
	
	private EventsDto convertEventToEventDto(Events event) {		
		return convertEventToEventDto(event, null);
	}
	
	private EventsDto convertEventToEventDto(Events event, VenueDto venue) {		
		return EventsDto.builder()
				.id(event.getId())
				.title(event.getTitle())
				.dateStatus(event.getDateStatus())
				.timeZone(event.getTimeZone())
				.startDate(event.getStartDate())
				.hiddenFromSearch(event.getHiddenFromSearch())
				.venue(venue)
				.build();
	}

}
