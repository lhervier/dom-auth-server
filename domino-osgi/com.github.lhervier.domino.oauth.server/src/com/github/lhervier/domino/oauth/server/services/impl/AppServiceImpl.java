package com.github.lhervier.domino.oauth.server.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.github.lhervier.domino.oauth.server.entity.ApplicationEntity;
import com.github.lhervier.domino.oauth.server.entity.PersonEntity;
import com.github.lhervier.domino.oauth.server.model.Application;
import com.github.lhervier.domino.oauth.server.repo.ApplicationRepository;
import com.github.lhervier.domino.oauth.server.repo.PersonRepository;
import com.github.lhervier.domino.oauth.server.services.AppService;
import com.github.lhervier.domino.oauth.server.utils.Utils;

/**
 * Service to manipualte applications
 * @author Lionel HERVIER
 */
@Service
public class AppServiceImpl implements AppService {
	
	/**
	 * The application root
	 */
	@Value("${oauth2.server.applicationRoot}")
	private String applicationRoot;
	
	/**
	 * The application repository
	 */
	@Autowired
	private ApplicationRepository appRepo;
	
	/**
	 * The Person repository
	 */
	@Autowired
	private PersonRepository personRepo;
	
	/**
	 * Convert an entity to an application
	 * @param entity the entity
	 * @return the application
	 */
	private Application fromEntity(ApplicationEntity entity) {
		if( entity == null )
			return null;
		
		Application app = new Application();
		app.setClientId(entity.getClientId());
		app.setName(entity.getName());
		app.setReaders(entity.getReaders());
		app.setRedirectUri(entity.getRedirectUri());
		app.setRedirectUris(new ArrayList<String>());
		if( entity.getRedirectUris() != null )
			app.getRedirectUris().addAll(entity.getRedirectUris());
		app.setFullName(entity.getFullName());
		
		return app;
	}
	
	/**
	 * Convert an application to an entity
	 * @param app the application
	 * @return the entity
	 */
	private ApplicationEntity toEntity(Application app) {
		ApplicationEntity entity = new ApplicationEntity();
		entity.setAppReader(app.getFullName());
		entity.setClientId(app.getClientId());
		entity.setName(app.getName());
		entity.setFullName("CN=" + app.getName() + this.applicationRoot);
		entity.setReaders(app.getReaders());
	
		String error = Utils.checkRedirectUri(app.getRedirectUri());
		if( error != null )
			throw new DataIntegrityViolationException(error);
		entity.setRedirectUri(app.getRedirectUri());

		entity.setRedirectUris(new ArrayList<String>());
		if( app.getRedirectUris() != null ) {
			for( String uri : app.getRedirectUris() ) {
				error = Utils.checkRedirectUri(uri);
				if( error != null )
					throw new DataIntegrityViolationException(error);
				entity.getRedirectUris().add(uri);
			}
		}
		
		return entity;
	}
	
	/**
	 * @see com.github.lhervier.domino.oauth.server.services.AppService#getApplicationsNames()
	 */
	public List<String> getApplicationsNames() {
		return this.appRepo.listNames();
	}
	
	/**
	 * @see com.github.lhervier.domino.oauth.server.services.AppService#getApplicationFromName(java.lang.String)
	 */
	public Application getApplicationFromName(String appName) {
		return this.fromEntity(this.appRepo.findOneByName(appName));
	}
	
	/**
	 * @see com.github.lhervier.domino.oauth.server.services.AppService#getApplicationFromClientId(java.lang.String)
	 */
	public Application getApplicationFromClientId(String clientId) {
		return this.fromEntity(this.appRepo.findOne(clientId));
	}
	
	/**
	 * @see com.github.lhervier.domino.oauth.server.services.AppService#prepareApplication()
	 */
	public Application prepareApplication() {
		Application ret = new Application();
		ret.setName("");
		ret.setClientId(UUID.randomUUID().toString());
		ret.setRedirectUri("");
		ret.setRedirectUris(new ArrayList<String>());
		ret.setReaders("*");
		return ret;
	}
	
	/**
	 * @see com.github.lhervier.domino.oauth.server.services.AppService#addApplication(com.github.lhervier.domino.oauth.server.model.Application)
	 */
	public String addApplication(Application app) {
		// Check that it does not already exist
		ApplicationEntity existing = this.appRepo.findOneByName(app.getName());
		if( existing != null )
			throw new DataIntegrityViolationException("Application '" + app.getName() + "' already exists");
		existing = this.appRepo.findOne(app.getClientId());
		if( existing != null )
			throw new DataIntegrityViolationException("Application with client id '" + app.getClientId() + "' already exists");
		
		// Compute the full name
		app.setFullName("CN=" + app.getName() + this.applicationRoot);
		
		// Create the associated person
		PersonEntity person = new PersonEntity();
		person.setFullNames(Arrays.asList(app.getFullName(), app.getClientId()));
		person.setLastName(app.getName());
		person.setShortName(app.getName());
		person = this.personRepo.save(person);
		String pwd = person.getHttpPassword();
		
		// Save the application
		this.appRepo.save(this.toEntity(app));
		
		return pwd;
	}
	
	/**
	 * @see com.github.lhervier.domino.oauth.server.services.AppService#updateApplication(com.github.lhervier.domino.oauth.server.model.Application)
	 */
	public void updateApplication(Application app) {
		ApplicationEntity existing = this.appRepo.findOne(app.getClientId());
		if( existing == null )
			throw new DataIntegrityViolationException("Application does not exist...");
		if( !Utils.equals(app.getName(), existing.getName()) )
			throw new DataIntegrityViolationException("Cannot change name of application...");
		
		this.appRepo.save(this.toEntity(app));
	}
	
	/**
	 * @see com.github.lhervier.domino.oauth.server.services.AppService#removeApplication(java.lang.String)
	 */
	public void removeApplication(String name) {
		Application app = this.getApplicationFromName(name);
		if( app == null )
			return;
		
		this.personRepo.delete(app.getFullName());
		this.appRepo.deleteByName(name);
	}
}
