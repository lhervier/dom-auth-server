package com.github.lhervier.domino.oauth.library.server.bean;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.View;

import org.apache.commons.lang.StringUtils;

import com.github.lhervier.domino.oauth.common.utils.DominoUtils;
import com.github.lhervier.domino.oauth.common.utils.GsonUtils;
import com.github.lhervier.domino.oauth.common.utils.JSFUtils;
import com.github.lhervier.domino.oauth.common.utils.SystemUtils;
import com.github.lhervier.domino.oauth.library.server.ex.GrantException;
import com.github.lhervier.domino.oauth.library.server.ex.ServerErrorException;
import com.github.lhervier.domino.oauth.library.server.ex.grant.InvalidClientException;
import com.github.lhervier.domino.oauth.library.server.ex.grant.InvalidGrantException;
import com.github.lhervier.domino.oauth.library.server.ex.grant.InvalidRequestException;
import com.github.lhervier.domino.oauth.library.server.ex.grant.InvalidScopeException;
import com.github.lhervier.domino.oauth.library.server.ex.grant.UnsupportedGrantTypeException;
import com.github.lhervier.domino.oauth.library.server.model.AccessToken;
import com.github.lhervier.domino.oauth.library.server.model.Application;
import com.github.lhervier.domino.oauth.library.server.model.AuthorizationCode;
import com.github.lhervier.domino.oauth.library.server.model.RefreshToken;
import com.google.gson.JsonObject;
import com.ibm.xsp.designer.context.XSPContext;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;

/**
 * Bean pour le endpoint "token"
 * @author Lionel HERVIER
 */
public class TokenBean {

	/**
	 * Le nom de la vue qui contient les codes autorization
	 */
	private static final String VIEW_AUTHCODES = "AuthorizationCodes";
	
	/**
	 * La bean pour g�rer les apps
	 */
	private AppBean appBean;
	
	/**
	 * La bean pour acc�der aux secrets
	 */
	private SecretBean secretBean;
	
	/**
	 * La bean pour acc�der aux param�tres
	 */
	private ParamsBean paramsBean;
	
	/**
	 * La base courante
	 */
	private Database database;
	
	/**
	 * La database courante (en tant que signer)
	 */
	private Database databaseAsSigner;
	
	/**
	 * Les param�tres du query string
	 */
	private Map<String, String> param;
	
	/**
	 * Le contexte utilisateur
	 */
	private XSPContext context;
	
	/**
	 * La requ�te http
	 */
	private HttpServletRequest request;
	
	/**
	 * La r�ponse http
	 */
	private HttpServletResponse response;
	
	/**
	 * Retourne la vue qui contient les codes autorisation
	 * @return la vue qui contient les codes autorisation
	 * @throws NotesException en cas de pb
	 */
	private View getAuthCodeView() throws NotesException {
		return DominoUtils.getView(this.database, VIEW_AUTHCODES);
	}
	
	/**
	 * Pour supprimer un code authorization.
	 * On se sert de la session ouverte par le signataire de la XPage car
	 * l'application n'a pas le droit.
	 * @param code le code
	 * @throws ServerErrorException
	 */
	private void removeCode(String code) throws ServerErrorException {
		View v = null;
		Document authDoc = null;
		try {
			v = DominoUtils.getView(this.databaseAsSigner, VIEW_AUTHCODES);
			authDoc = v.getDocumentByKey(code, true);
			if( authDoc == null )
				return;
			if( !authDoc.remove(true) )
				throw new RuntimeException("Je n'arrive pas � supprimer le document contenant les infos du code authorization");
		} catch (NotesException e) {
			throw new ServerErrorException(e);
		} finally {
			DominoUtils.recycleQuietly(authDoc);
			DominoUtils.recycleQuietly(v);
		}
	}
	
	// =============================================================================
	
	/**
	 * Gestion du token.
	 * @throws IOException
	 * @throws NotesException 
	 */
	public void token() throws IOException, NotesException {
		// Calcul l'objet r�ponse
		Object resp;
		try {
			// On doit passer par un POST
			if( !"POST".equals(this.request.getMethod()) )
				throw new InvalidRequestException();
			
			// R�cup�re le client id � partir de l'authentification
			Name nn = null;
			String clientId;
			try {
				String fullName = this.database.getParent().getEffectiveUserName();
				nn = this.database.getParent().createName(fullName);
				String appName = nn.getCommon();
				Application app = this.appBean.getApplicationFromName(appName);
				if( app == null )
					throw new InvalidClientException();
				clientId = app.getClientId();
			} finally {
				DominoUtils.recycleQuietly(nn);
			}
			
			// Si on a un client_id en param�tre, �a doit �tre le client_id courant
			if( this.param.containsKey("client_id") ) {
				if( !clientId.equals(this.param.get("client_id")) )
					throw new InvalidClientException();
			}
			
			// On doit avoir un grant_type
			String grantType = this.param.get("grant_type");
			if( grantType == null )
				throw new InvalidRequestException();
			
			// L'objet pour la r�ponse
			if( "authorization_code".equals(grantType) )
				resp = this.authorizationCode(
						this.param.get("code"),
						this.param.get("redirect_uri"),
						clientId
				);
			else if( "refresh_token".equals(grantType) ) {
				List<String> scopes;
				if( this.param.get("scope") == null )
					scopes = new ArrayList<String>();
				else
					scopes = Arrays.asList(StringUtils.split(this.param.get("scope"), " "));
				resp = this.refreshToken(
						this.param.get("refresh_token"),
						scopes
				);
			} else
				throw new UnsupportedGrantTypeException();
		} catch(GrantException e) {
			this.response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp = e.getError();
		} catch (ServerErrorException e) {
			this.response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp = null;
		}
		
		// Envoi dans la stream http
		JSFUtils.sendJson(resp);
	}
	
	/**
	 * Cr�� un refresh token
	 * @param authCode le code autorisation
	 * @return le refresh token
	 * @throws IOException 
	 * @throws JOSEException 
	 * @throws NotesException 
	 * @throws KeyLengthException 
	 */
	private String createRefreshToken(AuthorizationCode authCode) throws KeyLengthException, NotesException, JOSEException, IOException {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setAuthCode(authCode);
		refreshToken.setExp(SystemUtils.currentTimeSeconds() + this.paramsBean.getRefreshTokenLifetime());
		JWEObject jweObject = new JWEObject(
				new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM), 
				new Payload(GsonUtils.toJson(refreshToken))
		);
		jweObject.encrypt(new DirectEncrypter(
				this.secretBean.getRefreshTokenSecret()
		));
		return jweObject.serialize();
	}
	
	/**
	 * Cr�� un access token
	 * @param authCode le code autorisation
	 * @return un access token
	 * @throws IOException 
	 * @throws JOSEException 
	 * @throws NotesException 
	 * @throws KeyLengthException 
	 */
	private String createAccessToken(AuthorizationCode authCode) throws KeyLengthException, NotesException, JOSEException, IOException {
		AccessToken accessToken = new AccessToken();
		accessToken.setIss(this.paramsBean.getIssuer());
		accessToken.setAud(authCode.getClientId());
		accessToken.setSub(authCode.getUser());
		accessToken.setExp(SystemUtils.currentTimeSeconds() + this.paramsBean.getAccessTokenLifetime());
		JWSObject jwsObject = new JWSObject(
				new JWSHeader(JWSAlgorithm.HS256),
                new Payload(GsonUtils.toJson(accessToken))
		);
		jwsObject.sign(new MACSigner(
				this.secretBean.getAccessTokenSecret()
		));
		return jwsObject.serialize();
	}
	
	/**
	 * G�n�ration d'un token � partir d'un code autorisation
	 * @param code le code autorisation
	 * @param redirectUri l'uri de redirection
	 * @param clientId l'id de l'appli cliente
	 * @throws GrantException en cas de pb das le grant
	 * @throws ServerErrorException en cas d'erreur pendant l'acc�s au serveur
	 */
	public JsonObject authorizationCode(String code, String redirectUri, String clientId) throws GrantException, ServerErrorException {
		if( code == null )
			throw new InvalidRequestException();
		if( redirectUri == null )
			throw new InvalidRequestException();
		if( clientId == null )
			throw new InvalidRequestException();
		
		JsonObject resp = new JsonObject();
		
		Name nn = null;
		Document authDoc = null;
		View v = null;
		try {
			nn = this.database.getParent().createName(this.database.getParent().getEffectiveUserName());
			v = this.getAuthCodeView();
			
			// R�cup�re l'application
			Application app = this.appBean.getApplicationFromName(nn.getCommon());
			if( app == null )
				throw new InvalidClientException();
			
			// R�cup�re le document correspondant au code authorization
			authDoc = v.getDocumentByKey(code, true);
			if( authDoc == null )
				throw new InvalidGrantException();
			AuthorizationCode authCode = DominoUtils.fillObject(new AuthorizationCode(), authDoc);
			
			// V�rifie qu'il n'est pas expir�
			long expired = (long) authCode.getExpires();
			if( expired < SystemUtils.currentTimeSeconds() )
				throw new InvalidGrantException();
			
			// V�rifie que le clientId est le bon
			if( !clientId.equals(authCode.getClientId()) )
				throw new InvalidClientException();
			
			// V�rifie que l'uri de redirection est la m�me
			if( !redirectUri.equals(authCode.getRedirectUri()) )
				throw new InvalidGrantException();
			
			// G�n�re le access token. Il est sign� avec la cl� partag�e avec les serveurs de ressources.
			String accessToken = this.createAccessToken(authCode);
			resp.addProperty("access_token", accessToken);
			
			// G�n�re le refresh token
			String refreshToken = this.createRefreshToken(authCode);
			resp.addProperty("refresh_token", refreshToken);
			
			// La dur�e d'expiration. On prend celle du accessToken
			resp.addProperty("expires_in", this.paramsBean.getAccessTokenLifetime());
			
			// Le type de token
			resp.addProperty("token_type", "Bearer");
			
			// FIXME: Faire ajouter des propri�t�s par des plugins externes
			
			// D�finit les scopes s'il sont diff�rents de ceux demand�s lors de la requ�te � Authorize
			if( !authCode.getScopes().containsAll(authCode.getGrantedScopes()) )
				resp.addProperty("scope", StringUtils.join(authCode.getGrantedScopes().iterator(), " "));
			
			return resp;
		} catch (NotesException e) {
			throw new ServerErrorException(e);
		} catch (KeyLengthException e) {
			throw new ServerErrorException(e);
		} catch (JOSEException e) {
			throw new ServerErrorException(e);
		} catch (IOException e) {
			throw new ServerErrorException(e);
		} finally {
			DominoUtils.recycleQuietly(nn);
			DominoUtils.recycleQuietly(authDoc);
			DominoUtils.recycleQuietly(v);
			
			// Supprime le code authorization pour emp�cher une r�-utilisation
			this.removeCode(code);
		}
	}
	
	/**
	 * G�n�ration d'un token � partir d'un refresh token
	 * @param sRefreshToken le refresh token
	 * @param scopes d'�ventuels nouveaux scopes.
	 * @throws GrantException 
	 * @throws ServerErrorException
	 */
	public JsonObject refreshToken(String sRefreshToken, List<String> scopes) throws GrantException, ServerErrorException {
		if( sRefreshToken == null )
			throw new InvalidGrantException();
		
		try {
			// D�crypte le refresh token
			JWEObject jweObject = JWEObject.parse(sRefreshToken);
			jweObject.decrypt(new DirectDecrypter(this.secretBean.getRefreshTokenSecret()));
			String json = jweObject.getPayload().toString();
			RefreshToken refreshToken = GsonUtils.fromJson(json, RefreshToken.class);
			
			// V�rifie que les scopes demand�s sont bien dans la liste des scopes d�j� accord�s
			if( !refreshToken.getAuthCode().getGrantedScopes().containsAll(scopes) )
				throw new InvalidScopeException();
			
			// V�rifie qu'il est valide
			if( refreshToken.getExp() < SystemUtils.currentTimeSeconds() )
				throw new InvalidGrantException();
			
			// V�rifie qu'il existe bien une application pour ce login
			String appName = this.context.getUser().getCommonName();
			Application app = this.appBean.getApplicationFromName(appName);
			if( app == null )
				throw new InvalidGrantException();
			
			// V�rifie que le token a bien �t� g�n�r� pour cette application
			if( !app.getClientId().equals(refreshToken.getAuthCode().getClientId()) )
				throw new InvalidGrantException();
			
			// Pr�pare la r�ponse
			JsonObject resp = new JsonObject();
			
			// Met � jour les scopes
			if( scopes.size() != 0 ) {
				if( !scopes.containsAll(refreshToken.getAuthCode().getGrantedScopes())) {
					resp.addProperty("scope", StringUtils.join(scopes.iterator(), " "));
					refreshToken.getAuthCode().setGrantedScopes(scopes);
				}
			}
			
			// G�n�re l'access token
			String newAccessToken = this.createAccessToken(refreshToken.getAuthCode());
			resp.addProperty("access_token", newAccessToken);
			
			// G�n�re le refresh token
			String newRefreshToken = this.createRefreshToken(refreshToken.getAuthCode());
			resp.addProperty("refresh_token", newRefreshToken);
			
			// Les autres infos
			resp.addProperty("expires_in", this.paramsBean.getAccessTokenLifetime());		// Expiration en m�me temps que le refresh token
			resp.addProperty("token_type", "Bearer");
			
			return resp;
		} catch (ParseException e) {
			throw new ServerErrorException(e);
		} catch (KeyLengthException e) {
			throw new ServerErrorException(e);
		} catch (NotesException e) {
			throw new ServerErrorException(e);
		} catch (JOSEException e) {
			throw new ServerErrorException(e);
		} catch (IOException e) {
			throw new ServerErrorException(e);
		}
	}
	
	// =========================================================================================================

	/**
	 * @param database the database to set
	 */
	public void setDatabase(Database database) {
		this.database = database;
	}

	/**
	 * @param appBean the appBean to set
	 */
	public void setAppBean(AppBean appBean) {
		this.appBean = appBean;
	}

	/**
	 * @param secretBean the secretBean to set
	 */
	public void setSecretBean(SecretBean secretBean) {
		this.secretBean = secretBean;
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
	 * @param context the context to set
	 */
	public void setContext(XSPContext context) {
		this.context = context;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @param databaseAsSigner the databaseAsSigner to set
	 */
	public void setDatabaseAsSigner(Database databaseAsSigner) {
		this.databaseAsSigner = databaseAsSigner;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
