/**
 * 
 */
package com.master.fan.artist.exceptions;

/**
 * @author mohsin
 *
 */
public class ArtistNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
    private Throwable ex;

    public ArtistNotFoundException( String message, Throwable ex) {
        super(message, ex);
        this.message = message;
        this.ex = ex;
    }

    public ArtistNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}
