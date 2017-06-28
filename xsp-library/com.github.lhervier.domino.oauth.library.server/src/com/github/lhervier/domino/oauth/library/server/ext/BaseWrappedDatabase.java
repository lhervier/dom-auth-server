package com.github.lhervier.domino.oauth.library.server.ext;

import java.util.Vector;

import lotus.domino.ACL;
import lotus.domino.Agent;
import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Form;
import lotus.domino.NoteCollection;
import lotus.domino.NotesException;
import lotus.domino.Outline;
import lotus.domino.Replication;
import lotus.domino.Session;
import lotus.domino.View;

public abstract class BaseWrappedDatabase implements Database {
	
	public abstract Database getDatabase();

	public boolean isNull() {
		return this.getDatabase() == null;
	}
	
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#FTDomainSearch(java.lang.String, int, int, int, int, int, java.lang.String)
	 */
	public Document FTDomainSearch(String arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5, String arg6) throws NotesException {
		return this.getDatabase().FTDomainSearch(arg0, arg1, arg2, arg3, arg4, arg5,
				arg6);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#FTSearch(java.lang.String)
	 */
	public DocumentCollection FTSearch(String arg0) throws NotesException {
		return this.getDatabase().FTSearch(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#FTSearch(java.lang.String, int)
	 */
	public DocumentCollection FTSearch(String arg0, int arg1)
			throws NotesException {
		return this.getDatabase().FTSearch(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#FTSearch(java.lang.String, int, int, int)
	 */
	public DocumentCollection FTSearch(String arg0, int arg1, int arg2, int arg3)
			throws NotesException {
		return this.getDatabase().FTSearch(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#FTSearchRange(java.lang.String, int, int, int, int)
	 */
	public DocumentCollection FTSearchRange(String arg0, int arg1, int arg2,
			int arg3, int arg4) throws NotesException {
		return this.getDatabase().FTSearchRange(arg0, arg1, arg2, arg3, arg4);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#compact()
	 */
	public int compact() throws NotesException {
		return this.getDatabase().compact();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#compactWithOptions(java.lang.String)
	 */
	public int compactWithOptions(String arg0) throws NotesException {
		return this.getDatabase().compactWithOptions(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#compactWithOptions(int)
	 */
	public int compactWithOptions(int arg0) throws NotesException {
		return this.getDatabase().compactWithOptions(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#compactWithOptions(int, java.lang.String)
	 */
	public int compactWithOptions(int arg0, String arg1) throws NotesException {
		return this.getDatabase().compactWithOptions(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createCopy(java.lang.String, java.lang.String)
	 */
	public Database createCopy(String arg0, String arg1) throws NotesException {
		return this.getDatabase().createCopy(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createCopy(java.lang.String, java.lang.String, int)
	 */
	public Database createCopy(String arg0, String arg1, int arg2)
			throws NotesException {
		return this.getDatabase().createCopy(arg0, arg1, arg2);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createDocument()
	 */
	public Document createDocument() throws NotesException {
		return this.getDatabase().createDocument();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createDocumentCollection()
	 */
	public DocumentCollection createDocumentCollection() throws NotesException {
		return this.getDatabase().createDocumentCollection();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws NotesException
	 * @see lotus.domino.Database#createFTIndex(int, boolean)
	 */
	public void createFTIndex(int arg0, boolean arg1) throws NotesException {
		this.getDatabase().createFTIndex(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createFromTemplate(java.lang.String, java.lang.String, boolean)
	 */
	public Database createFromTemplate(String arg0, String arg1, boolean arg2)
			throws NotesException {
		return this.getDatabase().createFromTemplate(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createFromTemplate(java.lang.String, java.lang.String, boolean, int)
	 */
	public Database createFromTemplate(String arg0, String arg1, boolean arg2,
			int arg3) throws NotesException {
		return this.getDatabase().createFromTemplate(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createNoteCollection(boolean)
	 */
	public NoteCollection createNoteCollection(boolean arg0)
			throws NotesException {
		return this.getDatabase().createNoteCollection(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createOutline(java.lang.String)
	 */
	public Outline createOutline(String arg0) throws NotesException {
		return this.getDatabase().createOutline(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createOutline(java.lang.String, boolean)
	 */
	public Outline createOutline(String arg0, boolean arg1)
			throws NotesException {
		return this.getDatabase().createOutline(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createQueryView(java.lang.String, java.lang.String)
	 */
	public View createQueryView(String arg0, String arg1) throws NotesException {
		return this.getDatabase().createQueryView(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createQueryView(java.lang.String, java.lang.String, lotus.domino.View)
	 */
	public View createQueryView(String arg0, String arg1, View arg2)
			throws NotesException {
		return this.getDatabase().createQueryView(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createQueryView(java.lang.String, java.lang.String, lotus.domino.View, boolean)
	 */
	public View createQueryView(String arg0, String arg1, View arg2,
			boolean arg3) throws NotesException {
		return this.getDatabase().createQueryView(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createReplica(java.lang.String, java.lang.String)
	 */
	public Database createReplica(String arg0, String arg1)
			throws NotesException {
		return this.getDatabase().createReplica(arg0, arg1);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createView()
	 */
	public View createView() throws NotesException {
		return this.getDatabase().createView();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createView(java.lang.String)
	 */
	public View createView(String arg0) throws NotesException {
		return this.getDatabase().createView(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createView(java.lang.String, java.lang.String)
	 */
	public View createView(String arg0, String arg1) throws NotesException {
		return this.getDatabase().createView(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createView(java.lang.String, java.lang.String, lotus.domino.View)
	 */
	public View createView(String arg0, String arg1, View arg2)
			throws NotesException {
		return this.getDatabase().createView(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#createView(java.lang.String, java.lang.String, lotus.domino.View, boolean)
	 */
	public View createView(String arg0, String arg1, View arg2, boolean arg3)
			throws NotesException {
		return this.getDatabase().createView(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#enableFolder(java.lang.String)
	 */
	public void enableFolder(String arg0) throws NotesException {
		this.getDatabase().enableFolder(arg0);
	}

	/**
	 * @throws NotesException
	 * @see lotus.domino.Database#fixup()
	 */
	public void fixup() throws NotesException {
		this.getDatabase().fixup();
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#fixup(int)
	 */
	public void fixup(int arg0) throws NotesException {
		this.getDatabase().fixup(arg0);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getACL()
	 */
	public ACL getACL() throws NotesException {
		return this.getDatabase().getACL();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getACLActivityLog()
	 */
	@SuppressWarnings("unchecked")
	public Vector getACLActivityLog() throws NotesException {
		return this.getDatabase().getACLActivityLog();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getAgent(java.lang.String)
	 */
	public Agent getAgent(String arg0) throws NotesException {
		return this.getDatabase().getAgent(arg0);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getAgents()
	 */
	@SuppressWarnings("unchecked")
	public Vector getAgents() throws NotesException {
		return this.getDatabase().getAgents();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getAllDocuments()
	 */
	public DocumentCollection getAllDocuments() throws NotesException {
		return this.getDatabase().getAllDocuments();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getAllReadDocuments()
	 */
	public DocumentCollection getAllReadDocuments() throws NotesException {
		return this.getDatabase().getAllReadDocuments();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getAllReadDocuments(java.lang.String)
	 */
	public DocumentCollection getAllReadDocuments(String arg0)
			throws NotesException {
		return this.getDatabase().getAllReadDocuments(arg0);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getAllUnreadDocuments()
	 */
	public DocumentCollection getAllUnreadDocuments() throws NotesException {
		return this.getDatabase().getAllUnreadDocuments();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getAllUnreadDocuments(java.lang.String)
	 */
	public DocumentCollection getAllUnreadDocuments(String arg0)
			throws NotesException {
		return this.getDatabase().getAllUnreadDocuments(arg0);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getCategories()
	 */
	public String getCategories() throws NotesException {
		return this.getDatabase().getCategories();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getCreated()
	 */
	public DateTime getCreated() throws NotesException {
		return this.getDatabase().getCreated();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getCurrentAccessLevel()
	 */
	public int getCurrentAccessLevel() throws NotesException {
		return this.getDatabase().getCurrentAccessLevel();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getDB2Schema()
	 */
	public String getDB2Schema() throws NotesException {
		return this.getDatabase().getDB2Schema();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getDesignTemplateName()
	 */
	public String getDesignTemplateName() throws NotesException {
		return this.getDatabase().getDesignTemplateName();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getDocumentByID(java.lang.String)
	 */
	public Document getDocumentByID(String arg0) throws NotesException {
		return this.getDatabase().getDocumentByID(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getDocumentByUNID(java.lang.String)
	 */
	public Document getDocumentByUNID(String arg0) throws NotesException {
		return this.getDatabase().getDocumentByUNID(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getDocumentByURL(java.lang.String, boolean)
	 */
	public Document getDocumentByURL(String arg0, boolean arg1)
			throws NotesException {
		return this.getDatabase().getDocumentByURL(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getDocumentByURL(java.lang.String, boolean, boolean, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public Document getDocumentByURL(String arg0, boolean arg1, boolean arg2,
			boolean arg3, String arg4, String arg5, String arg6, String arg7,
			String arg8, boolean arg9) throws NotesException {
		return this.getDatabase().getDocumentByURL(arg0, arg1, arg2, arg3, arg4, arg5,
				arg6, arg7, arg8, arg9);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getFTIndexFrequency()
	 */
	public int getFTIndexFrequency() throws NotesException {
		return this.getDatabase().getFTIndexFrequency();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getFileFormat()
	 */
	public int getFileFormat() throws NotesException {
		return this.getDatabase().getFileFormat();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getFileName()
	 */
	public String getFileName() throws NotesException {
		return this.getDatabase().getFileName();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getFilePath()
	 */
	public String getFilePath() throws NotesException {
		return this.getDatabase().getFilePath();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getFolderReferencesEnabled()
	 */
	public boolean getFolderReferencesEnabled() throws NotesException {
		return this.getDatabase().getFolderReferencesEnabled();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getForm(java.lang.String)
	 */
	public Form getForm(String arg0) throws NotesException {
		return this.getDatabase().getForm(arg0);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getForms()
	 */
	@SuppressWarnings("unchecked")
	public Vector getForms() throws NotesException {
		return this.getDatabase().getForms();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getHttpURL()
	 */
	public String getHttpURL() throws NotesException {
		return this.getDatabase().getHttpURL();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getLastFTIndexed()
	 */
	public DateTime getLastFTIndexed() throws NotesException {
		return this.getDatabase().getLastFTIndexed();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getLastFixup()
	 */
	public DateTime getLastFixup() throws NotesException {
		return this.getDatabase().getLastFixup();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getLastModified()
	 */
	public DateTime getLastModified() throws NotesException {
		return this.getDatabase().getLastModified();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getLimitRevisions()
	 */
	public double getLimitRevisions() throws NotesException {
		return this.getDatabase().getLimitRevisions();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getLimitUpdatedBy()
	 */
	public double getLimitUpdatedBy() throws NotesException {
		return this.getDatabase().getLimitUpdatedBy();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getListInDbCatalog()
	 */
	public boolean getListInDbCatalog() throws NotesException {
		return this.getDatabase().getListInDbCatalog();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getManagers()
	 */
	@SuppressWarnings("unchecked")
	public Vector getManagers() throws NotesException {
		return this.getDatabase().getManagers();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getMaxSize()
	 */
	public long getMaxSize() throws NotesException {
		return this.getDatabase().getMaxSize();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getModifiedDocuments()
	 */
	public DocumentCollection getModifiedDocuments() throws NotesException {
		return this.getDatabase().getModifiedDocuments();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getModifiedDocuments(lotus.domino.DateTime)
	 */
	public DocumentCollection getModifiedDocuments(DateTime arg0)
			throws NotesException {
		return this.getDatabase().getModifiedDocuments(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getModifiedDocuments(lotus.domino.DateTime, int)
	 */
	public DocumentCollection getModifiedDocuments(DateTime arg0, int arg1)
			throws NotesException {
		return this.getDatabase().getModifiedDocuments(arg0, arg1);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getNotesURL()
	 */
	public String getNotesURL() throws NotesException {
		return this.getDatabase().getNotesURL();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getOption(int)
	 */
	public boolean getOption(int arg0) throws NotesException {
		return this.getDatabase().getOption(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getOutline(java.lang.String)
	 */
	public Outline getOutline(String arg0) throws NotesException {
		return this.getDatabase().getOutline(arg0);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getParent()
	 */
	public Session getParent() throws NotesException {
		return this.getDatabase().getParent();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getPercentUsed()
	 */
	public double getPercentUsed() throws NotesException {
		return this.getDatabase().getPercentUsed();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getProfileDocCollection(java.lang.String)
	 */
	public DocumentCollection getProfileDocCollection(String arg0)
			throws NotesException {
		return this.getDatabase().getProfileDocCollection(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getProfileDocument(java.lang.String, java.lang.String)
	 */
	public Document getProfileDocument(String arg0, String arg1)
			throws NotesException {
		return this.getDatabase().getProfileDocument(arg0, arg1);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getReplicaID()
	 */
	public String getReplicaID() throws NotesException {
		return this.getDatabase().getReplicaID();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getReplicationInfo()
	 */
	public Replication getReplicationInfo() throws NotesException {
		return this.getDatabase().getReplicationInfo();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getServer()
	 */
	public String getServer() throws NotesException {
		return this.getDatabase().getServer();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getSize()
	 */
	public double getSize() throws NotesException {
		return this.getDatabase().getSize();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getSizeQuota()
	 */
	public int getSizeQuota() throws NotesException {
		return this.getDatabase().getSizeQuota();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getSizeWarning()
	 */
	public long getSizeWarning() throws NotesException {
		return this.getDatabase().getSizeWarning();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getTemplateName()
	 */
	public String getTemplateName() throws NotesException {
		return this.getDatabase().getTemplateName();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getTitle()
	 */
	public String getTitle() throws NotesException {
		return this.getDatabase().getTitle();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getType()
	 */
	public int getType() throws NotesException {
		return this.getDatabase().getType();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getURL()
	 */
	public String getURL() throws NotesException {
		return this.getDatabase().getURL();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getURLHeaderInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getURLHeaderInfo(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5) throws NotesException {
		return this.getDatabase().getURLHeaderInfo(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getUndeleteExpireTime()
	 */
	public int getUndeleteExpireTime() throws NotesException {
		return this.getDatabase().getUndeleteExpireTime();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getView(java.lang.String)
	 */
	public View getView(String arg0) throws NotesException {
		return this.getDatabase().getView(arg0);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#getViews()
	 */
	@SuppressWarnings("unchecked")
	public Vector getViews() throws NotesException {
		return this.getDatabase().getViews();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws NotesException
	 * @see lotus.domino.Database#grantAccess(java.lang.String, int)
	 */
	public void grantAccess(String arg0, int arg1) throws NotesException {
		this.getDatabase().grantAccess(arg0, arg1);
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isAllowOpenSoftDeleted()
	 */
	public boolean isAllowOpenSoftDeleted() throws NotesException {
		return this.getDatabase().isAllowOpenSoftDeleted();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isClusterReplication()
	 */
	public boolean isClusterReplication() throws NotesException {
		return this.getDatabase().isClusterReplication();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isConfigurationDirectory()
	 */
	public boolean isConfigurationDirectory() throws NotesException {
		return this.getDatabase().isConfigurationDirectory();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isCurrentAccessPublicReader()
	 */
	public boolean isCurrentAccessPublicReader() throws NotesException {
		return this.getDatabase().isCurrentAccessPublicReader();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isCurrentAccessPublicWriter()
	 */
	public boolean isCurrentAccessPublicWriter() throws NotesException {
		return this.getDatabase().isCurrentAccessPublicWriter();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isDB2()
	 */
	public boolean isDB2() throws NotesException {
		return this.getDatabase().isDB2();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isDelayUpdates()
	 */
	public boolean isDelayUpdates() throws NotesException {
		return this.getDatabase().isDelayUpdates();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isDesignLockingEnabled()
	 */
	public boolean isDesignLockingEnabled() throws NotesException {
		return this.getDatabase().isDesignLockingEnabled();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isDirectoryCatalog()
	 */
	public boolean isDirectoryCatalog() throws NotesException {
		return this.getDatabase().isDirectoryCatalog();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isDocumentLockingEnabled()
	 */
	public boolean isDocumentLockingEnabled() throws NotesException {
		return this.getDatabase().isDocumentLockingEnabled();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isFTIndexed()
	 */
	public boolean isFTIndexed() throws NotesException {
		return this.getDatabase().isFTIndexed();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isInMultiDbIndexing()
	 */
	public boolean isInMultiDbIndexing() throws NotesException {
		return this.getDatabase().isInMultiDbIndexing();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isInService()
	 */
	public boolean isInService() throws NotesException {
		return this.getDatabase().isInService();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isLink()
	 */
	public boolean isLink() throws NotesException {
		return this.getDatabase().isLink();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isMultiDbSearch()
	 */
	public boolean isMultiDbSearch() throws NotesException {
		return this.getDatabase().isMultiDbSearch();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isOpen()
	 */
	public boolean isOpen() throws NotesException {
		return this.getDatabase().isOpen();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isPendingDelete()
	 */
	public boolean isPendingDelete() throws NotesException {
		return this.getDatabase().isPendingDelete();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isPrivateAddressBook()
	 */
	public boolean isPrivateAddressBook() throws NotesException {
		return this.getDatabase().isPrivateAddressBook();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#isPublicAddressBook()
	 */
	public boolean isPublicAddressBook() throws NotesException {
		return this.getDatabase().isPublicAddressBook();
	}

	/**
	 * @throws NotesException
	 * @see lotus.domino.Database#markForDelete()
	 */
	public void markForDelete() throws NotesException {
		this.getDatabase().markForDelete();
	}

	/**
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#open()
	 */
	public boolean open() throws NotesException {
		return this.getDatabase().open();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#openByReplicaID(java.lang.String, java.lang.String)
	 */
	public boolean openByReplicaID(String arg0, String arg1)
			throws NotesException {
		return this.getDatabase().openByReplicaID(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#openIfModified(java.lang.String, java.lang.String, lotus.domino.DateTime)
	 */
	public boolean openIfModified(String arg0, String arg1, DateTime arg2)
			throws NotesException {
		return this.getDatabase().openIfModified(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#openWithFailover(java.lang.String, java.lang.String)
	 */
	public boolean openWithFailover(String arg0, String arg1)
			throws NotesException {
		return this.getDatabase().openWithFailover(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#queryAccess(java.lang.String)
	 */
	public int queryAccess(String arg0) throws NotesException {
		return this.getDatabase().queryAccess(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#queryAccessPrivileges(java.lang.String)
	 */
	public int queryAccessPrivileges(String arg0) throws NotesException {
		return this.getDatabase().queryAccessPrivileges(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#queryAccessRoles(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Vector queryAccessRoles(String arg0) throws NotesException {
		return this.getDatabase().queryAccessRoles(arg0);
	}

	/**
	 * @throws NotesException
	 * @see lotus.domino.Base#recycle()
	 */
	public void recycle() throws NotesException {
		this.getDatabase().recycle();
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Base#recycle(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	public void recycle(Vector arg0) throws NotesException {
		this.getDatabase().recycle(arg0);
	}

	/**
	 * @throws NotesException
	 * @see lotus.domino.Database#remove()
	 */
	public void remove() throws NotesException {
		this.getDatabase().remove();
	}

	/**
	 * @throws NotesException
	 * @see lotus.domino.Database#removeFTIndex()
	 */
	public void removeFTIndex() throws NotesException {
		this.getDatabase().removeFTIndex();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#replicate(java.lang.String)
	 */
	public boolean replicate(String arg0) throws NotesException {
		return this.getDatabase().replicate(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#revokeAccess(java.lang.String)
	 */
	public void revokeAccess(String arg0) throws NotesException {
		this.getDatabase().revokeAccess(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#search(java.lang.String)
	 */
	public DocumentCollection search(String arg0) throws NotesException {
		return this.getDatabase().search(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#search(java.lang.String, lotus.domino.DateTime)
	 */
	public DocumentCollection search(String arg0, DateTime arg1)
			throws NotesException {
		return this.getDatabase().search(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws NotesException
	 * @see lotus.domino.Database#search(java.lang.String, lotus.domino.DateTime, int)
	 */
	public DocumentCollection search(String arg0, DateTime arg1, int arg2)
			throws NotesException {
		return this.getDatabase().search(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setAllowOpenSoftDeleted(boolean)
	 */
	public void setAllowOpenSoftDeleted(boolean arg0) throws NotesException {
		this.getDatabase().setAllowOpenSoftDeleted(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setCategories(java.lang.String)
	 */
	public void setCategories(String arg0) throws NotesException {
		this.getDatabase().setCategories(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setDelayUpdates(boolean)
	 */
	public void setDelayUpdates(boolean arg0) throws NotesException {
		this.getDatabase().setDelayUpdates(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setDesignLockingEnabled(boolean)
	 */
	public void setDesignLockingEnabled(boolean arg0) throws NotesException {
		this.getDatabase().setDesignLockingEnabled(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setDocumentLockingEnabled(boolean)
	 */
	public void setDocumentLockingEnabled(boolean arg0) throws NotesException {
		this.getDatabase().setDocumentLockingEnabled(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setFTIndexFrequency(int)
	 */
	public void setFTIndexFrequency(int arg0) throws NotesException {
		this.getDatabase().setFTIndexFrequency(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setFolderReferencesEnabled(boolean)
	 */
	public void setFolderReferencesEnabled(boolean arg0) throws NotesException {
		this.getDatabase().setFolderReferencesEnabled(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setInMultiDbIndexing(boolean)
	 */
	public void setInMultiDbIndexing(boolean arg0) throws NotesException {
		this.getDatabase().setInMultiDbIndexing(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setInService(boolean)
	 */
	public void setInService(boolean arg0) throws NotesException {
		this.getDatabase().setInService(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setLimitRevisions(double)
	 */
	public void setLimitRevisions(double arg0) throws NotesException {
		this.getDatabase().setLimitRevisions(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setLimitUpdatedBy(double)
	 */
	public void setLimitUpdatedBy(double arg0) throws NotesException {
		this.getDatabase().setLimitUpdatedBy(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setListInDbCatalog(boolean)
	 */
	public void setListInDbCatalog(boolean arg0) throws NotesException {
		this.getDatabase().setListInDbCatalog(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws NotesException
	 * @see lotus.domino.Database#setOption(int, boolean)
	 */
	public void setOption(int arg0, boolean arg1) throws NotesException {
		this.getDatabase().setOption(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setSizeQuota(int)
	 */
	public void setSizeQuota(int arg0) throws NotesException {
		this.getDatabase().setSizeQuota(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setSizeWarning(int)
	 */
	public void setSizeWarning(int arg0) throws NotesException {
		this.getDatabase().setSizeWarning(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setTitle(java.lang.String)
	 */
	public void setTitle(String arg0) throws NotesException {
		this.getDatabase().setTitle(arg0);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#setUndeleteExpireTime(int)
	 */
	public void setUndeleteExpireTime(int arg0) throws NotesException {
		this.getDatabase().setUndeleteExpireTime(arg0);
	}

	/**
	 * @throws NotesException
	 * @see lotus.domino.Database#sign()
	 */
	public void sign() throws NotesException {
		this.getDatabase().sign();
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#sign(int)
	 */
	public void sign(int arg0) throws NotesException {
		this.getDatabase().sign(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws NotesException
	 * @see lotus.domino.Database#sign(int, boolean)
	 */
	public void sign(int arg0, boolean arg1) throws NotesException {
		this.getDatabase().sign(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws NotesException
	 * @see lotus.domino.Database#sign(int, boolean, java.lang.String)
	 */
	public void sign(int arg0, boolean arg1, String arg2) throws NotesException {
		this.getDatabase().sign(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @throws NotesException
	 * @see lotus.domino.Database#sign(int, boolean, java.lang.String, boolean)
	 */
	public void sign(int arg0, boolean arg1, String arg2, boolean arg3)
			throws NotesException {
		this.getDatabase().sign(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @throws NotesException
	 * @see lotus.domino.Database#updateFTIndex(boolean)
	 */
	public void updateFTIndex(boolean arg0) throws NotesException {
		this.getDatabase().updateFTIndex(arg0);
	}

}
