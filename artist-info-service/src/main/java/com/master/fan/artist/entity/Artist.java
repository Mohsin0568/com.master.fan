/**
 * 
 */
package com.master.fan.artist.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author mohsin
 * 
 * Artist data coming from upstream service will be serialized into this class object
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {
	
	private String id;
	private String name;
	private String imgSrc;
	private String url;
	private int rank;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	@Override
	public String toString() {
		return "Artist [id=" + id + "]";
	}
}
