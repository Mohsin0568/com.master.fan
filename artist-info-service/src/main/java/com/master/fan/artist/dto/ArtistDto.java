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
	
	public static ArtistDtoBuilder builder() {
		return new ArtistDtoBuilder();
	}
	
	public static class ArtistDtoBuilder {
		
		private String id;
		private String name;
		private String imgSrc;
		private String url;
		private int rank;
		
		private List<EventsDto> events;

		public ArtistDtoBuilder id(String id) {
			this.id = id;
			return this;
		}

		public ArtistDtoBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ArtistDtoBuilder imgSrc(String imgSrc) {
			this.imgSrc = imgSrc;
			return this;
		}

		public ArtistDtoBuilder url(String url) {
			this.url = url;
			return this;
		}

		public ArtistDtoBuilder rank(int rank) {
			this.rank = rank;
			return this;
		}

		public ArtistDtoBuilder events(List<EventsDto> events) {
			this.events = events;
			return this;
		}
		
		public ArtistDto build() {
			return new ArtistDto(this);
		}
		
	}
}
