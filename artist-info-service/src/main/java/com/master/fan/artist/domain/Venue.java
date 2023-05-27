/**
 * 
 */
package com.master.fan.artist.domain;

/**
 * @author mohsin
 * 
 * Venue data coming from upstream service will be serialized into this class object
 *
 */
public class Venue {
	
	private String id;
	private String url;
	private String city;
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
