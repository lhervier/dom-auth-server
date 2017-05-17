package com.github.lhervier.domino.oauth.library.server.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;

import org.apache.commons.io.IOUtils;

import com.github.lhervier.domino.oauth.common.utils.DominoUtils;
import com.github.lhervier.domino.oauth.common.utils.JSFUtils;
import com.github.lhervier.domino.oauth.library.server.bean.AppBean;
import com.github.lhervier.domino.oauth.library.server.bean.ParamsBean;
import com.github.lhervier.domino.oauth.library.server.bean.SecretBean;

/**
 * M�thodes utiles � l'appli
 * @author Lionel HERVIER
 */
public class Utils {

	/**
	 * Retourne le requestScope
	 * @return le requestScope
	 */
	@SuppressWarnings("unchecked")
	public static final Map<String, Object> getRequestScope() {
		return (Map<String, Object>) JSFUtils.getBean("requestScope");
	}
	
	/**
	 * Retourne la bean de param�trage
	 * @return la bean de param�trage
	 */
	public static final ParamsBean getParamsBean() {
		return (ParamsBean) JSFUtils.getBean("paramsBean");
	}
	
	/**
	 * Retourne la bean pour g�rer les apps
	 * @return la bean pour g�rer les apps
	 */
	public static final AppBean getAppBean() {
		return (AppBean) JSFUtils.getBean("appBean");
	}
	
	/**
	 * Retourne la bean pour acc�der aux secrets
	 * @return la secretBean
	 */
	public static final SecretBean getSecretBean() {
		return (SecretBean) JSFUtils.getBean("secretBean");
	}
	
	/**
	 * Retourne la base carnet d'adresse.
	 * @param session la session Notes
	 * @return le NAB
	 * @throws NotesException en cas de pb
	 */
	public static final Database getNab(Session session) throws NotesException {
		Database names = DominoUtils.openDatabase(
				session, 
				getParamsBean().getNab()
		);
		if( names == null )
			throw new RuntimeException("Je n'arrive pas � ouvrir la base '" + getParamsBean().getNab() + "'");
		return names;
	}
	
	/**
	 * Pour v�rifier si un utilisateur a un r�le. Renvoi un 404 si
	 * ce n'est pas le cas
	 * @param role le role
	 * @throws IOException 
	 */
	public static final void checkRole(String role) throws IOException {
		if( !JSFUtils.getContext().getUser().getRoles().contains(role) )
			send404();
	}
	
	/**
	 * Renvoi une erreur 404
	 * @throws IOException 
	 */
	public static final void send404() throws IOException {
		OutputStream out = null;
		Writer writer = null;
		try {
			HttpServletResponse resp = JSFUtils.getServletResponse();
			resp.setStatus(404);
			out = resp.getOutputStream();
			writer = new OutputStreamWriter(out, "UTF-8");
			writer.write(
					"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
					"<html>\n" +
					"<head>\n" +
					"<title>Error</title></head>\n" +
					"<body text=\"#000000\">\n" +
					"<h1>Error 404</h1>HTTP Web Server: Item Not Found Exception</body>\n" +
					"</html>"
			);
		} finally {
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(out);
		}
		System.err.println(
				"ATTENTION: L'utilisateur '" + JSFUtils.getContext().getUser().getFullName() + "' " +
				"a tent� d'acc�der � la page '" +JSFUtils.getView().getPageName() + "'"
		);
		FacesContext.getCurrentInstance().responseComplete();
	}
}
