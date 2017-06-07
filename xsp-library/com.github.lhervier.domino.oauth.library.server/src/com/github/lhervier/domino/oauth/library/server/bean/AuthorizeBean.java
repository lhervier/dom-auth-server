package com.github.lhervier.domino.oauth.library.server.bean;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.github.lhervier.domino.oauth.common.model.StateResponse;
import com.github.lhervier.domino.oauth.common.utils.DominoUtils;
import com.github.lhervier.domino.oauth.common.utils.JSFUtils;
import com.github.lhervier.domino.oauth.common.utils.QueryStringUtils;
import com.github.lhervier.domino.oauth.common.utils.SystemUtils;
import com.github.lhervier.domino.oauth.library.server.Constants;
import com.github.lhervier.domino.oauth.library.server.ex.AuthorizeException;
import com.github.lhervier.domino.oauth.library.server.ex.InvalidUriException;
import com.github.lhervier.domino.oauth.library.server.ex.authorize.InvalidRequestException;
import com.github.lhervier.domino.oauth.library.server.ex.authorize.ServerErrorException;
import com.github.lhervier.domino.oauth.library.server.ex.authorize.UnsupportedResponseTypeException;
import com.github.lhervier.domino.oauth.library.server.model.Application;
import com.github.lhervier.domino.oauth.library.server.model.AuthorizationCode;
import com.github.lhervier.domino.oauth.library.server.model.AuthorizeResponse;

/**
 * Bean pour g�rer le endpoint "authorize"
 * @author Lionel HERVIER
 */
public class AuthorizeBean {

	/**
	 * Notre g�n�rateur de nombres al�atoires
	 */
	private static final SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * La bean pour acc�der aux applications
	 */
	private AppBean appBean;
	
	/**
	 * La bean pour acc�der au param�trage
	 */
	private ParamsBean paramsBean;
	
	/**
	 * Les param�tres dans la requ�te
	 */
	private Map<String, String> param;
	
	/**
	 * La databaseAsSigner courante
	 */
	private Database databaseAsSigner;
	
	/**
	 * La session courante
	 */
	private Session session;
	
	/**
	 * Le requestScope
	 */
	private Map<String, Object> requestScope;
	
	/**
	 * G�n�re le code autorization
	 * @throws IOException 
	 */
	public void authorize() throws IOException {
		StateResponse ret;
		String redirectUri = null;
		try {
			// Le responseType est obligatoire
			String responseType = this.param.get("response_type");
			if( responseType == null )
				throw new InvalidRequestException();
			
			// Authorization Code Grant
			// ===========================
			if( "code".equals(responseType) ) {
				// Le clientId est obligatoire
				String clientId = this.param.get("client_id");
				if( clientId == null )
					throw new InvalidRequestException("No client_id in query string.");
				
				// Le redirectUri est obligatoire
				// FIXME: En fonction du response_type, le redirectUri peut �tre facultatif
				// Voir https://tools.ietf.org/html/rfc6749#section-4.1.1
				redirectUri = this.param.get("redirect_uri");
				if( redirectUri == null )
					throw new InvalidUriException("No redirect_uri in query string.");
				try {
					URI uri = new URI(redirectUri);
					if( !uri.isAbsolute() )
						throw new InvalidUriException("Invalid redirect_uri");
				} catch (URISyntaxException e) {
					throw new InvalidUriException("Invalid redirect_uri", e);
				}
				
				// Ex�cution
				ret = this.authorizationCode(
						clientId, 
						redirectUri, 
						this.param.get("scope")
				);
			} else
				throw new UnsupportedResponseTypeException();
		
		// Cas particulier (cf RFC) si l'uri de redirection est invalide
		} catch(InvalidUriException e) {
			e.printStackTrace(System.err);			// FIXME: O� envoyer �a ???
			this.requestScope.put("error", e);
			ret = null;
		
		// Erreur pendant l'autorisation
		} catch(AuthorizeException e) {
			e.printStackTrace(System.err);			// FIXME: O� envoyer �a ???
			ret = e.getError();
		}
		
		// Pas de r�ponse => Pas de redirection
		if( ret == null )
			return;
		
		// Ajoute le state
		ret.setState(this.param.get("state"));		// Eventuellement null
		
		// Redirige
		JSFUtils.sendRedirect(QueryStringUtils.addBeanToQueryString(redirectUri, ret));
	}
	
	// ===========================================================================================================
	
	/**
	 * G�n�re un identifiant pour les codes autorization
	 * @return l'identifiant
	 */
	private String generateCode() {
		// see https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
		return new BigInteger(260, RANDOM).toString(32);
	}
	
	/**
	 * Traitement d'une demande d'authorisation pour un code autorization.
	 * @param clientId l'id du client
	 * @param redirectUri l'uri de redirection
	 * @param scope le scope FIXME: Il n'est pas utilis�
	 * @return l'url de redirection
	 * @throws AuthorizeException en cas de pb
	 * @throws InvalidUriException si l'uri est invalide
	 */
	private AuthorizeResponse authorizationCode(
			String clientId, 
			String redirectUri,
			String scope) throws AuthorizeException, InvalidUriException {
		// R�cup�re l'application
		Application app;
		try {
			app = this.appBean.getApplicationFromClientId(clientId);
		} catch (NotesException e) {
			throw new ServerErrorException(e);
		}
		if( app == null )
			throw new ServerErrorException();
		
		// V�rifie que l'uri de redirection est bien dans la liste
		Set<String> redirectUris = new HashSet<String>();
		redirectUris.add(app.getRedirectUri().toString());
		for( URI uri : app.getRedirectUris() )
			redirectUris.add(uri.toString());
		if( !redirectUris.contains(redirectUri) )
			throw new InvalidUriException("invalid redirect_uri");		// Cf RFC. On ne doit pas (MUST NOT) rediriger vers une uri invalide
		
		// Cr�� le document authorization
		String id = this.generateCode();
		Document authDoc = null;
		Name nn = null;
		try {
			nn = this.databaseAsSigner.getParent().createName(app.getName() + this.paramsBean.getApplicationRoot());
			
			// Cr�� le code authorization
			AuthorizationCode authCode = new AuthorizationCode();
			authCode.setApplication(nn.toString());
			authCode.setId(id);
			authCode.setRedirectUri(redirectUri);
			authCode.setExpires(SystemUtils.currentTimeSeconds() + this.paramsBean.getAuthCodeLifetime());
			authCode.setIss(Constants.NAMESPACE);
			authCode.setSub(this.session.getEffectiveUserName());
			authCode.setAud(app.getClientId());
			authCode.setIat(SystemUtils.currentTimeSeconds());
			authCode.setAuthTime(SystemUtils.currentTimeSeconds());
			
			// On le persiste dans la base
			authDoc = this.databaseAsSigner.createDocument();
			authDoc.replaceItemValue("Form", "AuthorizationCode");
			DominoUtils.fillDocument(authDoc, authCode);
			
			DominoUtils.computeAndSave(authDoc);
		} catch (NotesException e) {
			e.printStackTrace(System.err);
			throw new ServerErrorException();
		} finally {
			DominoUtils.recycleQuietly(nn);
			DominoUtils.recycleQuietly(authDoc);
		}
		
		AuthorizeResponse ret = new AuthorizeResponse();
		ret.setCode(id);
		return ret;
	}
	
	// ==============================================================================================

	/**
	 * @param appBean the appBean to set
	 */
	public void setAppBean(AppBean appBean) {
		this.appBean = appBean;
	}

	/**
	 * @param paramsBean the paramsBean to set
	 */
	public void setParamsBean(ParamsBean paramsBean) {
		this.paramsBean = paramsBean;
	}

	/**
	 * @param param the param to set
	 */
	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	/**
	 * @param databaseAsSigner the databaseAsSigner to set
	 */
	public void setDatabaseAsSigner(Database database) {
		this.databaseAsSigner = database;
	}

	/**
	 * @param requestScope the requestScope to set
	 */
	public void setRequestScope(Map<String, Object> requestScope) {
		this.requestScope = requestScope;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}
	
}
