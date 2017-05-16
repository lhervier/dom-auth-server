package fr.asso.afer.oauth2.ex.authorize;

import fr.asso.afer.oauth2.ex.AuthorizeException;
import fr.asso.afer.oauth2.model.error.authorize.UnauthorizedClientError;

public class UnauthorizedClientException extends AuthorizeException {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -1959919623322631668L;

	/**
	 * Constructeur
	 */
	public UnauthorizedClientException() {
		super(new UnauthorizedClientError());
	}

	/**
	 * Constructeur
	 * @param message
	 */
	public UnauthorizedClientException(String message) {
		super(message, new UnauthorizedClientError());
	}

	/**
	 * Constructeur
	 * @param message
	 * @param cause
	 */
	public UnauthorizedClientException(String message, Throwable cause) {
		super(message, cause, new UnauthorizedClientError());
	}

	/**
	 * Constructeur
	 * @param cause
	 */
	public UnauthorizedClientException(Throwable cause) {
		super(cause, new UnauthorizedClientError());
	}
}
