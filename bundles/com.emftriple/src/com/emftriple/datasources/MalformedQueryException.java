package com.emftriple.datasources;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.7.0
 */
public class MalformedQueryException extends RuntimeException {

	public MalformedQueryException(String message) {
		super(message);
	}
	
	public MalformedQueryException(Exception e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
