package com.master.fan.artist.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author mohsin
 * 
 * EventsDto class will have events information while sending data back to client
 * This class object will be created using builder pattern
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventsDto {
	
	private String id;
	private String title;
	private String dateStatus;
	private String timeZone;
	private LocalDateTime startDate;
	private String hiddenFromSearch;
	private VenueDto venue;
	
	private EventsDto(EventsDtoBuilder builder) {		
		this.id = builder.id;
		this.title = builder.title;
		this.dateStatus = builder.dateStatus;
		this.timeZone = builder.timeZone;
		this.startDate = builder.startDate;
		this.hiddenFromSearch = builder.hiddenFromSearch;
		this.venue = builder.venue;		
	}
	
	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getDateStatus() {
		return dateStatus;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public String getHiddenFromSearch() {
		return hiddenFromSearch;
	}
	public VenueDto getVenue() {
		return venue;
	}
	
	public static EventsDtoBuilder builder() {
		return new EventsDtoBuilder();
	}
	
	public static class EventsDtoBuilder{
		
		private String id;
		private String title;
		private String dateStatus;
		private String timeZone;
		private LocalDateTime startDate;
		private String hiddenFromSearch;
		private VenueDto venue;
		
		public void id(String id) {
			this.id = id;
		}
		public void title(String title) {
			this.title = title;
		}
		public void dateStatus(String dateStatus) {
			this.dateStatus = dateStatus;
		}
		public void timeZone(String timeZone) {
			this.timeZone = timeZone;
		}
		public void startDate(LocalDateTime startDate) {
			this.startDate = startDate;
		}
		public void hiddenFromSearch(String hiddenFromSearch) {
			this.hiddenFromSearch = hiddenFromSearch;
		}
		public void venue(VenueDto venue) {
			this.venue = venue;
		}	
		
		public EventsDto builder() {
			return new EventsDto(this);
		}
	}

}
