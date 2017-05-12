package fr.asso.afer.oauth2.utils;

import java.util.Map;

import javax.faces.context.FacesContext;

import lotus.domino.Database;
import lotus.domino.Session;

import com.ibm.xsp.designer.context.XSPContext;

import fr.asso.afer.oauth2.app.AppBean;
import fr.asso.afer.oauth2.params.ParamsBean;
import fr.asso.afer.oauth2.secret.SecretBean;

/**
 * M�thodes pratiques pour JSF
 * @author Lionel HERVIER
 */
public class JSFUtils {

	/**
	 * Retourne une managed bean
	 * @param name le nom de la bean
	 * @return la bean
	 */
	public static final Object getBean(String name) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return ctx.getApplication().getVariableResolver().resolveVariable(ctx, name);
	}
	
	/**
	 * Retourne la session courante
	 * @return la session
	 */
	public static final Session getSession() {
		return (Session) getBean("session");
	}
	
	/**
	 * Retourne la session ouverte avec les infos
	 * du signataire de la XPages courante
	 * @return la session
	 */
	public static final Session getSessionAsSigner() {
		return (Session) getBean("sessionAsSigner");
	}
	
	/**
	 * Retourne la database courante
	 * @return la database courante
	 */
	public static final Database getDatabase() {
		return (Database) getBean("database");
	}
	
	/**
	 * Retourne le contexte utilisateur
	 * @return le contexte utilisateur
	 */
	public static final XSPContext getContext() {
		return (XSPContext) getBean("context");
	}
	
	/**
	 * Retourne les param�tres de la requ�te
	 * @return les param�tres de la requ�te
	 */
	@SuppressWarnings("unchecked")
	public static final Map<String, String> getParam() {
		return (Map<String, String>) getBean("param");
	}
	
	/**
	 * Retourne la bean de param�trage
	 * @return la bean de param�trage
	 */
	public static final ParamsBean getParamsBean() {
		return (ParamsBean) getBean("paramsBean");
	}
	
	/**
	 * Retourne la bean pour g�rer les apps
	 * @return la bean pour g�rer les apps
	 */
	public static final AppBean getAppBean() {
		return (AppBean) getBean("appBean");
	}
	
	/**
	 * Retourne la bean pour acc�der aux secrets
	 * @return la secretBean
	 */
	public static final SecretBean getSecretBean() {
		return (SecretBean) getBean("secretBean");
	}
	
}
