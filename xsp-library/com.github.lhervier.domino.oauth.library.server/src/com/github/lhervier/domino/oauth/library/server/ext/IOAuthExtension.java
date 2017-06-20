package com.github.lhervier.domino.oauth.library.server.ext;

import java.util.List;

import lotus.domino.NotesException;

import com.google.gson.JsonObject;

/**
 * Interface � impl�menter quand on ajoute un scope
 * @author Lionel HERVIER
 */
public interface IOAuthExtension {

	/**
	 * Retourne un id
	 */
	public String getId();
	
	/**
	 * Appel� lors de l'utilisation du endpoint authorize.
	 * Les informations retourn�es seront ajout�es au code autorisation.
	 * Ce code est appel� alors que la session courante est ouverte en tant que l'utilisateur qui cherche � se connecter.
	 * @param granter pour d�clarer d'un scope a �t� autoris�.
	 * @param clientId l'id client de l'application demand�e.
	 * @param scopes les scopes demand�s
	 * @return le contexte ou null pour ne pas participer.
	 * @throws NotesException en ca de pb
	 */
	public JsonObject authorize(IScopeGranter granter, String clientId, List<String> scopes) throws NotesException;
	
	/**
	 * G�n�re des attributs � ajouter � la r�ponse de grant.
	 * Cette m�thode est appel�e lors de la g�n�ration du token.
	 * Ce code est appel� alors que la session courante est ouverte en tant que l'application.
	 * @param context le contexte g�n�r� lors de l'appel � authorize
	 * @param adder pour ajouter des propri�t�s � la r�ponse au grant.
	 * @param scopes les scopes demand�s
	 * @throws NotesException en ca de pb
	 */
	public void token(JsonObject context, IPropertyAdder adder) throws NotesException;
	
	/**
	 * G�n�re les attributs � ajouter � la r�ponse de grant.
	 * Cette m�thode est appel�e lors du rafra�chissement d'un token.
	 * Ce code est appel� alors que la session courante est ouverte en tant que l'application.
	 * @param context le contexte g�n�r� lors de l'appel � authorize.
	 * @param adder pour ajouter des propri�t�s � la r�ponse au grant
	 * @param granter pour d�clarer les scopes accord�s
	 * @param scopes les scopes demand�s
	 * @throws NotesException en ca de pb
	 */
	public void refresh(JsonObject context, IPropertyAdder adder, IScopeGranter granter, List<String> scopes) throws NotesException;
	
}
