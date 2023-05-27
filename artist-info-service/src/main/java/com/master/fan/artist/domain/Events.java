/**
 * 
 */
package com.master.fan.artist.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author mohsin
 * 
 * Events data coming from upstream service will be serialized into this class object
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Events {
	
	private String id;
	private String title;
	private String dateStatus;
	private String timeZone;
	private LocalDateTime startDate;
	private boolean hiddenFromSearch;
	private List<Artist> artists;
	private Venue venue;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDateStatus() {
		return dateStatus;
	}
	public void setDateStatus(String dateStatus) {
		this.dateStatus = dateStatus;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public boolean getHiddenFromSearch() {
		return hiddenFromSearch;
	}
	public void setHiddenFromSearch(boolean hiddenFromSearch) {
		this.hiddenFromSearch = hiddenFromSearch;
	}
	public List<Artist> getArtists() {
		return artists;
	}
	public void setArtists(List<Artist> artists) {
		this.artists = artists;
	}
	public Venue getVenue() {
		return venue;
	}
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	@Override
	public String toString() {
		return "Events [id=" + id + ", title=" + title + ", artists=" + artists + "]";
	}

}
