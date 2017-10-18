package com.github.lhervier.domino.oauth.server;

import lotus.domino.Database;
import lotus.domino.NotesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.github.lhervier.domino.oauth.server.utils.DominoUtils;
import com.github.lhervier.domino.spring.servlet.NotesContext;

public class BaseServerComponent {

	/**
	 * The notes context
	 */
	@Autowired
	protected NotesContext notesContext;
	
	/**
	 * The database where to store application information
	 */
	@Value("${oauth2.server.db}")
	private String oauth2db;
	
	/**
	 * Return the oauth2 database as the server
	 * @throws NotesException 
	 */
	public Database getOauth2DatabaseAsServer() throws NotesException {
		return DominoUtils.openDatabase(this.notesContext.getServerSession(), this.oauth2db);
	}
	
	/**
	 * Return the oauth2 database as the user (the application)
	 * @throws NotesException 
	 */
	public Database getOauth2DatabaseAsUser() throws NotesException {
		return DominoUtils.openDatabase(this.notesContext.getUserSession(), this.oauth2db);
	}
	
	
}