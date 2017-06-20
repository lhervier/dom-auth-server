package com.github.lhervier.domino.oauth.library.server.bean;

import java.util.List;

import com.github.lhervier.domino.oauth.common.bean.BaseParamsBean;

/**
 * Bean pour acc�der aux param�tres de l'application
 * @author Lionel HERVIER
 */
public class ParamsBean extends BaseParamsBean {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 25035453476284192L;

	/**
	 * L'issuer
	 */
	private String issuer;
	
	/**
	 * Le carnet d'adresse o� cr�er les applications
	 */
	private String nab;
	
	/**
	 * Pour compl�ter les noms des applications
	 */
	private String applicationRoot;
	
	/**
	 * La dur�e de vie de l'access token
	 */
	private long accessTokenLifetime;
	
	/**
	 * La dur�e de vie du refresh token
	 */
	private long refreshTokenLifetime;
	
	/**
	 * La dur�e de vie des codes autorisation
	 */
	private long authCodeLifetime;
	
	/**
	 * Le nom de la config SSO qui contient la cl� pour l'access token
	 */
	private String accessTokenConfig;
	
	/**
	 * Le nom de la config SSO qui contient la cl� pour le refresh token
	 */
	private String refreshTokenConfig;
	
	/**
	 * Les ids de plugins pour lesquels on d�fini des cl�s
	 */
	private List<String> pluginsNames;
	
	/**
	 * Les noms des cl�s � utiliser pour chaque plugin
	 */
	private List<String> pluginsKeys;
	
	// =============================================================

	/**
	 * @return the nab
	 */
	public String getNab() {
		return nab;
	}

	/**
	 * @param nab the nab to set
	 */
	public void setNab(String nab) {
		this.nab = nab;
	}

	/**
	 * @return the accessTokenLifetime
	 */
	public long getAccessTokenLifetime() {
		return accessTokenLifetime;
	}

	/**
	 * @param accessTokenLifetime the accessTokenLifetime to set
	 */
	public void setAccessTokenLifetime(long accessTokenLifetime) {
		this.accessTokenLifetime = accessTokenLifetime;
	}

	/**
	 * @return the refreshTokenLifetime
	 */
	public long getRefreshTokenLifetime() {
		return refreshTokenLifetime;
	}

	/**
	 * @param refreshTokenLifetime the refreshTokenLifetime to set
	 */
	public void setRefreshTokenLifetime(long refreshTokenLifetime) {
		this.refreshTokenLifetime = refreshTokenLifetime;
	}

	/**
	 * @return the accessTokenConfig
	 */
	public String getAccessTokenConfig() {
		return accessTokenConfig;
	}

	/**
	 * @param accessTokenConfig the accessTokenConfig to set
	 */
	public void setAccessTokenConfig(String accessTokenConfig) {
		this.accessTokenConfig = accessTokenConfig;
	}

	/**
	 * @return the refreshTokenConfig
	 */
	public String getRefreshTokenConfig() {
		return refreshTokenConfig;
	}

	/**
	 * @param refreshTokenConfig the refreshTokenConfig to set
	 */
	public void setRefreshTokenConfig(String refreshTokenConfig) {
		this.refreshTokenConfig = refreshTokenConfig;
	}

	/**
	 * @return the authCodeLifetime
	 */
	public long getAuthCodeLifetime() {
		return authCodeLifetime;
	}

	/**
	 * @param authCodeLifetime the authCodeLifetime to set
	 */
	public void setAuthCodeLifetime(long authCodeLifetime) {
		this.authCodeLifetime = authCodeLifetime;
	}

	/**
	 * @return the applicationRoot
	 */
	public String getApplicationRoot() {
		return applicationRoot;
	}

	/**
	 * @param applicationRoot the applicationRoot to set
	 */
	public void setApplicationRoot(String applicationRoot) {
		this.applicationRoot = applicationRoot;
	}

	/**
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * @param issuer the issuer to set
	 */
	public void setIssuer(String iss) {
		this.issuer = iss;
	}

	/**
	 * @return the pluginsNames
	 */
	public List<String> getPluginsNames() {
		return pluginsNames;
	}

	/**
	 * @param pluginsNames the pluginsNames to set
	 */
	public void setPluginsNames(List<String> pluginsNames) {
		this.pluginsNames = pluginsNames;
	}

	/**
	 * @return the pluginsKeys
	 */
	public List<String> getPluginsKeys() {
		return pluginsKeys;
	}

	/**
	 * @param pluginsKeys the pluginsKeys to set
	 */
	public void setPluginsKeys(List<String> pluginsKeys) {
		this.pluginsKeys = pluginsKeys;
	}
}
