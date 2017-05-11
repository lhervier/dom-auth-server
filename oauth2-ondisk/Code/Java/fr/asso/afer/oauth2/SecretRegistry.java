package fr.asso.afer.oauth2;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;

/**
 * Registre pour m�moriser les secrets
 * @author Lionel HERVIER
 */
public class SecretRegistry {

	/**
	 * Le r�le pour pouvoir utiliser cette classe
	 */
	public static final String ROLE = "[SecretExtractor]";
	
	/**
	 * Le nom de la vue qui contient les configs SSO
	 */
	public static final String WEBSSOCONFIG_VIEW = "($WebSSOConfigs)";
	
	/**
	 * Le nom du champ dans lequel r�cup�rer le secret
	 */
	public static final String SECRET_FIELD_NAME = "LTPA_DominoSecret";
	
	/**
	 * Retourne le document config SSO
	 * @param config le nom de la config � extraire
	 * @return la config SSO
	 * @throws NotesException en cas de pb
	 */
	private Document getSsoConfig(String config) throws NotesException {
		Session session = JSFUtils.getSession();
		
		Database db = JSFUtils.getDatabase();
		if( !db.queryAccessRoles(session.getEffectiveUserName()).contains(ROLE) )
			throw new RuntimeException("Vous n'avez pas les droits pour extraire les secrets");
		
		Database names = DominoUtils.openDatabase(session, Constants.PATH_NAMES);
		if( names == null )
			throw new RuntimeException("Je n'arrive pas � acc�der � la base " + Constants.PATH_NAMES);
		
		View v = names.getView(WEBSSOCONFIG_VIEW);
		if( v == null )
			throw new RuntimeException("La vue " + WEBSSOCONFIG_VIEW + " n'existe pas dans la base " + Constants.PATH_NAMES + ". Impossible de continuer.");
		Document ssoConfig = v.getDocumentByKey("AFER:AccessToken");
		if( ssoConfig == null )
			throw new RuntimeException("Je ne trouve pas la confg SSO '" + config + "'");
		
		return ssoConfig;
	}
	
	/**
	 * Retourne le secret utilis� pour signer l'access token
	 */
	public String getAccessTokenSecret() throws NotesException {
		return this.getSsoConfig(Constants.CONFIG_NAME_ACCESS_TOKEN).getItemValueString(SECRET_FIELD_NAME);
	}
	
	/**
	 * Retourne le secret utilis� pour crypter le refresh token
	 */
	public String getRefreshTokenSecret() throws NotesException {
		return this.getSsoConfig(Constants.CONFIG_NAME_REFRESH_TOKEN).getItemValueString(SECRET_FIELD_NAME);
	}
}
