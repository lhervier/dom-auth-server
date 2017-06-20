package com.github.lhervier.domino.oauth.library.server.ext;

import com.google.gson.JsonObject;

/**
 * Pour ajouter une propri�t� � la r�ponse du grant
 * @author Lionel HERVIER
 */
public interface IPropertyAdder {

	/**
	 * Ajoute une propri�t� sign�e
	 * @param name le nom de la propri�t�
	 * @param obj l'objet � serialiser
	 */
	public void addSignedProperty(String name, JsonObject obj);
	
	/**
	 * Ajoute une propri�t� crypt�e
	 * @param name le nom de la propri�t�
	 * @param obj l'objet � serialiser
	 */
	public void addCryptedProperty(String name, JsonObject obj);
}
