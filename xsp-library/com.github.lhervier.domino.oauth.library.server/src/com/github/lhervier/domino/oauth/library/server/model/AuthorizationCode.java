package com.github.lhervier.domino.oauth.library.server.model;

/**
 * Un code autorisation
 * @author Lionel HERVIER
 */
public class AuthorizationCode extends IdToken {

	/**
	 * Le champ lecteur qui contient le nom de l'application
	 */
	private String application;
	
	/**
	 * L'identifiant
	 */
	private String id;
	
	/**
	 * L'uri de redirection
	 */
	private String redirectUri;
	
	/**
	 * La date d'expiration
	 */
	private long expires;
	
	/**
	 * Le scope demand�
	 */
	private String scope;
	
	/**
	 * Le scope autoris�
	 */
	private String grantedScope;
	
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
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the grantedScope
	 */
	public String getGrantedScope() {
		return grantedScope;
	}

	/**
	 * @param grantedScope the grantedScope to set
	 */
	public void setGrantedScope(String grantedScope) {
		this.grantedScope = grantedScope;
	}
}
