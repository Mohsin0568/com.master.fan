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
 * EventsService will have business logic related to events
 *
 */

@Service
public class EventsService {
	
	@Autowired
	private EventsRestClient restClient;
	
	@Autowired
	private VenueService venueService;
	
	
	/**
	 * 
	 * @return Flux<Events>, this will return all events fetched from events service.
	 * 
	 * This method will connect with Events service and fetch all events data from it.
	 */
	public Flux<Events> getAllEvents(){
		// get all events data
		return restClient.retrieveEvents(); //.log();
	}
	
	/**
	 * 
	 * @param artistId
	 * @return Flux<EventsDto> This will return all events in EventsDTO object for an artist, eventsDto object will not have venues information
	 * 
	 * This method will return all events by artist id. Events will not have venue details
	 */
	public Flux<EventsDto> getEventsByArtistId(String artistId){		
		
		return getAllEvents() // this will fetch only events data base on artist id
				.filter(event -> event.getArtists()
									.stream()
									.anyMatch(artist -> artist.getId().equalsIgnoreCase(artistId))
				).map(this :: convertEventToEventDto);
	}
	
	/**
	 * 
	 * @param artistId
	 * @return Flux<EventsDto> This will return all events in EventsDTO object for an artist, eventsDto object will also have venues information
	 * 
	 * This method will return all events by artist id along with event venue.
	 */
	public Flux<EventsDto> getEventsWithVenuesByArtistId(String artistId){		

		return getAllEvents() // this will fetch all events from upstream server
				.filter(event -> event.getArtists() // fitler events based on artist id
									.stream()
									.anyMatch(artist -> artist.getId().equalsIgnoreCase(artistId))
				).flatMap(event -> {
						Mono<VenueDto> eventVenue = venueService.fetchVenueById(event.getVenue().getId()); // get Venues for each event
						return eventVenue.map(venue -> convertEventToEventDto(event, venue)); // map data to event DTO
					
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
