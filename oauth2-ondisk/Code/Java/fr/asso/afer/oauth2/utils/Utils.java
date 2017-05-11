package fr.asso.afer.oauth2.utils;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * M�thodes utiles � l'appli
 * @author Lionel HERVIER
 */
public class Utils {

	/**
	 * Retourne la base carnet d'adresse.
	 * @param session la session Notes
	 * @return le NAB
	 * @throws NotesException en cas de pb
	 */
	public static final Database getNab(Session session) throws NotesException {
		Database names = DominoUtils.openDatabase(
				session, 
				JSFUtils.getParamsBean().getNab()
		);
		if( names == null )
			throw new RuntimeException("Je n'arrive pas � ouvrir la base '" + JSFUtils.getParamsBean().getNab() + "'");
		return names;
	}
}
