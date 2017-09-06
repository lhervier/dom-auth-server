package com.github.lhervier.domino.oauth.common.ctx;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.github.lhervier.domino.oauth.common.NotesContext;
import com.github.lhervier.domino.oauth.common.utils.DatabaseWrapper;
import com.ibm.xsp.designer.context.XSPContext;

public class JSFNotesContext implements NotesContext {

	/**
	 * @param name managed bean name
	 * @return the managed bean
	 */
	private Object getBean(String name) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return ctx.getApplication().getVariableResolver().resolveVariable(ctx, name);
	}
	
	/**
	 * @see com.github.lhervier.domino.oauth.common.NotesContext#getServerDatabase()
	 */
	@Override
	public synchronized Database getServerDatabase() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
		String key = this.getClass().getName() + ".serverDatabase";
		if( request.getAttribute(key) != null )
			return (Database) request.getAttribute(key);
		
		DatabaseWrapper dbAsSigner;
		try {
			dbAsSigner = new DatabaseWrapper(this, this.getUserDatabase().getFilePath(), true);
		} catch (NotesException e) {
			throw new RuntimeException(e);
		}
		request.setAttribute(key, dbAsSigner);
		
		return dbAsSigner;
	}

	/**
	 * @see com.github.lhervier.domino.oauth.common.NotesContext#getServerSession()
	 */
	@Override
	public Session getServerSession() {
		return (Session) this.getBean("sessionAsSigner");
	}

	/**
	 * @see com.github.lhervier.domino.oauth.common.NotesContext#getUserDatabase()
	 */
	@Override
	public Database getUserDatabase() {
		return (Database) this.getBean("database");
	}

	/**
	 * @see com.github.lhervier.domino.oauth.common.NotesContext#getUserRoles()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getUserRoles() {
		return ((XSPContext) this.getBean("context")).getUser().getRoles();
	}

	/**
	 * @see com.github.lhervier.domino.oauth.common.NotesContext#getUserSession()
	 */
	@Override
	public Session getUserSession() {
		return (Session) this.getBean("session");
	}
}