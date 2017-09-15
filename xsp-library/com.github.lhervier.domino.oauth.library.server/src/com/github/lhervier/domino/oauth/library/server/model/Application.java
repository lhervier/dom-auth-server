package com.github.lhervier.domino.oauth.library.server.model;

import java.io.Serializable;
import java.util.List;

/**
 * Une application OAUTH2
 * @author Lionel HERVIER
 */
public class Application implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1213696763673347194L;

	/**
	 * Son nom
	 */
	private String name;
	
	/**
	 * Son client_id
	 */
	private String clientId;
	
	/**
	 * Son URI de redirection par d�faut
	 */
	private String redirectUri;
	
	/**
	 * Ses autres URIs de redirection
	 */
	private List<String> redirectUris;
	
	/**
	 * Les personnes autoris�es � se logger sur cette application
	 */
	private String readers;
	
	/**
	 * L'application elle m�me (champ lecteur)
	 */
	private String appReader;

	/**
	 * @return the readers
	 */
	public String getReaders() {
		return readers;
	}

	/**
	 * @param readers the readers to set
	 */
	public void setReaders(String readers) {
		this.readers = readers;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the redirectUri
	 */
	public String getRedirectUri() {
		return redirectUri;
	}

	/**
	 * @param redirectUrl the redirectUri to set
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * @return the redirectUris
	 */
	public List<String> getRedirectUris() {
		return redirectUris;
	}

	/**
	 * @param redirectUris the redirectUris to set
	 */
	public void setRedirectUris(List<String> redirectUris) {
		this.redirectUris = redirectUris;
	}

	/**
	 * @return the appReader
	 */
	public String getAppReader() {
		return appReader;
	}

	/**
	 * @param appReader the appReader to set
	 */
	public void setAppReader(String appReader) {
		this.appReader = appReader;
	}
}
