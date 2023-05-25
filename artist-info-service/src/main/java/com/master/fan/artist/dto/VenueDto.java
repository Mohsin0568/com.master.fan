/**
 * 
 */
package com.master.fan.artist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author mohsin
 * 
 * VenueDto class will have venue information while sending data back to client
 * This class object will be created using builder pattern
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class VenueDto {
	
	private String id;
	private String url;
	private String city;
	private String name;
	
	private VenueDto(VenueDtoBuilder builder) {
		this.id = builder.id;
		this.url = builder.url;
		this.name = builder.name;
		this.city = builder.city;
	}
	
	public String getId() {
		return id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getName() {
		return name;
	}
	
	public static VenueDtoBuilder builder() {
		return new VenueDtoBuilder();
	}
	
	public static class VenueDtoBuilder{
		
		private String id;
		private String url;
		private String city;
		private String name;
		
		public VenueDtoBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public VenueDtoBuilder url(String url) {
			this.url = url;
			return this;
		}
		
		public VenueDtoBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		public VenueDtoBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public VenueDto build() {
			return new VenueDto(this);
		}
		
	}
}
