package com.github.lhervier.domino.oauth.library.server.bean;

import java.io.Serializable;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;

import com.github.lhervier.domino.oauth.common.utils.DominoUtils;
import com.github.lhervier.domino.oauth.common.utils.JSFUtils;

/**
 * Bean pour acc�der aux param�tres de l'application
 * @author Lionel HERVIER
 */
public class ParamsBean implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 25035453476284192L;

	/**
	 * Le nom de la vue qui contient le doc de param
	 */
	private static final String VIEW_PARAMS = "Params";
	
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
	 * Constructeur
	 * @throws NotesException en cas de pb
	 */
	public ParamsBean() throws NotesException {
		this.reload();
	}
	
	/**
	 * Pour recharger la configuration
	 * @throws NotesException en cas de probl�me
	 */
	public void reload() throws NotesException {
		Database database = null;
		View v = null;
		Document doc = null;
		try {
			database = DominoUtils.openDatabase(
					JSFUtils.getSessionAsSigner(), 
					JSFUtils.getDatabase().getFilePath()
			);
			v = database.getView(VIEW_PARAMS);
			doc = v.getFirstDocument();
			if( doc == null )
				throw new RuntimeException("Le document de param�trage n'existe pas. Impossible de d�marrer l'application.");
			DominoUtils.fillObject(this, doc);
		} finally {
			DominoUtils.recycleQuietly(doc);
			DominoUtils.recycleQuietly(v);
			DominoUtils.recycleQuietly(database);
		}
	}
	
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
}
