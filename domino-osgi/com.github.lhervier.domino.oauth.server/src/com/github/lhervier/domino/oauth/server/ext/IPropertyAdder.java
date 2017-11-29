package com.github.lhervier.domino.oauth.server.ext;

/**
 * Pour ajouter une propri�t� � la r�ponse du grant
 * @author Lionel HERVIER
 */
public interface IPropertyAdder {

	/**
	 * Ajoute une propri�t� sign�e
	 * @param name le nom de la propri�t�
	 * @param obj l'objet � serialiser
	 * @param kid the key id to sign
	 */
	public void addSignedProperty(String name, Object obj, String kid);
	
	/**
	 * Add a standard property
	 * @param name the property name
	 * @param obj the object to add
	 */
	public void addProperty(String name, Object value);
}
