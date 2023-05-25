/**
 * 
 */
package com.master.fan.artist.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author mohsin
 * 
 * ArtistDto class will have artists information along with events and venues while sending data back to client
 * This class object will be created using builder pattern
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArtistDto{
    
	private String id;
	private String name;
	private String imgSrc;
	private String url;
	private int rank;
	
	private List<EventsDto> events;
	
	private ArtistDto(ArtistDtoBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.imgSrc = builder.imgSrc;
		this.url = builder.url;
		this.rank = builder.rank;
		this.events = builder.events;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getImgSrc() {
		return imgSrc;
	}
	
	public String getUrl() {
		return url;
	}
	
	public int getRank() {
		return rank;
	}
	
	public List<EventsDto> getEvents() {
		return events;
	}
	
	public static class ArtistDtoBuilder {
		
		private String id;
		private String name;
		private String imgSrc;
		private String url;
		private int rank;
		
		private List<EventsDto> events;

		public void id(String id) {
			this.id = id;
		}

		public void name(String name) {
			this.name = name;
		}

		public void imgSrc(String imgSrc) {
			this.imgSrc = imgSrc;
		}

		public void url(String url) {
			this.url = url;
		}

		public void rank(int rank) {
			this.rank = rank;
		}

		public void events(List<EventsDto> events) {
			this.events = events;
		}
		
		public ArtistDto builder() {
			return new ArtistDto(this);
		}
		
	}
}
