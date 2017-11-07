package com.github.lhervier.domino.oauth.server.entity;

import java.util.List;
import java.util.Map;

/**
 * Un code autorisation.
 * @author Lionel HERVIER
 */
public class AuthCodeEntity {

	/**
	 * L'identifiant
	 */
	private String id;
	
	/**
	 * Le champ lecteur qui contient le nom de l'application
	 */
	private String application;
	
	/**
	 * L'id client de l'application
	 */
	private String clientId;
	
	/**
	 * L'uri de redirection
	 */
	private String redirectUri;
	
	/**
	 * La date d'expiration
	 */
	private long expires;
	
	/**
	 * Le scopes demand�
	 */
	private List<String> scopes;
	
	/**
	 * Le scopes autoris�
	 */
	private List<String> grantedScopes;
	
	/**
	 * The context classes for each extension
	 */
	private Map<String, String> contextClasses;
	
	/**
	 * The context objets for each extension
	 * (as a json String)
	 */
	private Map<String, String> contextObjects;
	
	/**
	 * @return the application
	 */
	public String getApplication() {
		return application;
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(String application) {
		this.application = application;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the redirectUri
	 */
	public String getRedirectUri() {
		return redirectUri;
	}

	/**
	 * @param redirectUri the redirectUri to set
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * @return the expires
	 */
	public long getExpires() {
		return expires;
	}

	/**
	 * @param expires the expires to set
	 */
	public void setExpires(long expires) {
		this.expires = expires;
	}

	/**
	 * @return the scopes
	 */
	public List<String> getScopes() {
		return scopes;
	}

	/**
	 * @param scopes the scopes to set
	 */
	public void setScopes(List<String> scope) {
		this.scopes = scope;
	}

	/**
	 * @return the grantedScopes
	 */
	public List<String> getGrantedScopes() {
		return grantedScopes;
	}

	/**
	 * @param grantedScopes the grantedScopes to set
	 */
	public void setGrantedScopes(List<String> grantedScope) {
		this.grantedScopes = grantedScope;
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
	 * @return the contextClasses
	 */
	public Map<String, String> getContextClasses() {
		return contextClasses;
	}

	/**
	 * @param contextClasses the contextClasses to set
	 */
	public void setContextClasses(Map<String, String> contextClasses) {
		this.contextClasses = contextClasses;
	}

	/**
	 * @return the contextObjects
	 */
	public Map<String, String> getContextObjects() {
		return contextObjects;
	}

	/**
	 * @param contextObjects the contextObjects to set
	 */
	public void setContextObjects(Map<String, String> contextObjects) {
		this.contextObjects = contextObjects;
	}
}