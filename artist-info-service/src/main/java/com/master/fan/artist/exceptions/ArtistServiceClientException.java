package com.master.fan.artist.exceptions;

public class ArtistServiceClientException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private String message;
	 private Integer statusCode;

	 public ArtistServiceClientException(String message, Integer statusCode) {
		 super(message);
	     this.message = message;
	     this.statusCode = statusCode;
	 }
	
	 @Override
	 public String getMessage() {
		 return message;
	 }
	
	 public void setMessage(String message) {
		 this.message = message;
	 }
	
	 public Integer getStatusCode() {
		 return statusCode;
	 }
	
	 public void setStatusCode(Integer statusCode) {
		 this.statusCode = statusCode;
	 }
}
