package com.master.fan.artist.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author mohsin
 * 
 * EventsDto class will have events information while sending data back to client
 * This class object will be created using builder pattern
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventsDto {
	
	private String id;
	private String title;
	private String dateStatus;
	private String timeZone;
	private LocalDateTime startDate;
	private boolean hiddenFromSearch;
	private VenueDto venue;
	
	public EventsDto() {} // this should be deleted
	
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
	public boolean getHiddenFromSearch() {
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
		private boolean hiddenFromSearch;
		private VenueDto venue;
		
		public EventsDtoBuilder id(String id) {
			this.id = id;
			return this;
		}
		public EventsDtoBuilder title(String title) {
			this.title = title;
			return this;
		}
		public EventsDtoBuilder dateStatus(String dateStatus) {
			this.dateStatus = dateStatus;
			return this;
		}
		public EventsDtoBuilder timeZone(String timeZone) {
			this.timeZone = timeZone;
			return this;
		}
		public EventsDtoBuilder startDate(LocalDateTime startDate) {
			this.startDate = startDate;
			return this;
		}
		public EventsDtoBuilder hiddenFromSearch(boolean hiddenFromSearch) {
			this.hiddenFromSearch = hiddenFromSearch;
			return this;
		}
		public EventsDtoBuilder venue(VenueDto venue) {
			this.venue = venue;
			return this;
		}	
		
		public EventsDto build() {
			return new EventsDto(this);
		}
	}

}
