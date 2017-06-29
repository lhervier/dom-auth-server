package com.github.lhervier.domino.oauth.library.client.ex;

public class InitException extends Exception {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -6019350761545118817L;

	/**
	 * The object
	 */
	private Object error;
	
	/**
	 * Constructor
	 * @param message
	 */
	public InitException(Object error) {
		super();
		this.error = error;
	}

	/**
	 * Constructor
	 */
	public InitException(Object error, Throwable cause) {
		super(cause);
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public Object getError() {
		return error;
	}
	
}
