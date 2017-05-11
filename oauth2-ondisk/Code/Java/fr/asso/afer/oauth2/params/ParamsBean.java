package fr.asso.afer.oauth2.params;

import java.io.Serializable;

import fr.asso.afer.oauth2.utils.DominoUtils;
import fr.asso.afer.oauth2.utils.JSFUtils;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;

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
		Database database = JSFUtils.getDatabase();
		View v = database.getView(VIEW_PARAMS);
		Document doc = v.getFirstDocument();
		if( doc == null )
			throw new RuntimeException("Le document de param�trage n'existe pas. Impossible de d�marrer l'application.");
		DominoUtils.fillObject(this, doc);
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
}
