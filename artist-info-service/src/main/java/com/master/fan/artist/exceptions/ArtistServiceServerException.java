package com.master.fan.artist.exceptions;

public class ArtistServiceServerException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;


    public ArtistServiceServerException(String message) {
        super(message);
        this.message = message;
    }
}
