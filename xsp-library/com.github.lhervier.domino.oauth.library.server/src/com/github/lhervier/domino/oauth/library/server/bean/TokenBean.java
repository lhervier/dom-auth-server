package com.github.lhervier.domino.oauth.library.server.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;

import org.apache.commons.io.IOUtils;

import com.github.lhervier.domino.oauth.common.utils.DominoUtils;
import com.github.lhervier.domino.oauth.common.utils.GsonUtils;
import com.github.lhervier.domino.oauth.common.utils.JSFUtils;
import com.github.lhervier.domino.oauth.library.server.ex.GrantException;
import com.github.lhervier.domino.oauth.library.server.ex.ServerErrorException;
import com.github.lhervier.domino.oauth.library.server.ex.grant.InvalidClientException;
import com.github.lhervier.domino.oauth.library.server.ex.grant.InvalidGrantException;
import com.github.lhervier.domino.oauth.library.server.ex.grant.InvalidRequestException;
import com.github.lhervier.domino.oauth.library.server.ex.grant.UnsupportedGrantTypeException;
import com.github.lhervier.domino.oauth.library.server.model.AccessToken;
import com.github.lhervier.domino.oauth.library.server.model.Application;
import com.github.lhervier.domino.oauth.library.server.model.AuthorizationCode;
import com.github.lhervier.domino.oauth.library.server.model.GrantResponse;
import com.github.lhervier.domino.oauth.library.server.model.RefreshToken;
import com.github.lhervier.domino.oauth.library.server.utils.Utils;
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
	 * La session
	 */
	private Session session;
	
	/**
	 * La session en tant que le signataire de la XPage
	 */
	private Session sessionAsSigner;
	
	/**
	 * La base courante
	 */
	private Database database;
	
	/**
	 * La vue des codes authorization
	 */
	private View v;
	
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
	 * Constructeur
	 * @throws NotesException en cas de pb
	 */
	public TokenBean() throws NotesException {
		this.session = JSFUtils.getSession();
		this.sessionAsSigner = JSFUtils.getSessionAsSigner();
		this.database = JSFUtils.getDatabase();
		this.appBean = Utils.getAppBean();
		this.secretBean = Utils.getSecretBean();
		this.paramsBean = Utils.getParamsBean();
		this.v = DominoUtils.getView(this.database, VIEW_AUTHCODES);
	}
	
	/**
	 * Pour supprimer un code authorization.
	 * On se sert de la session ouverte par le signataire de la XPage car
	 * l'application n'a pas le droit.
	 * @param code le code
	 * @throws ServerErrorException
	 */
	private void removeCode(String code) throws ServerErrorException {
		Database db = null;
		View v = null;
		Document authDoc = null;
		try {
			db = DominoUtils.openDatabase(this.sessionAsSigner, this.database.getFilePath());
			v = DominoUtils.getView(db, VIEW_AUTHCODES);
			authDoc = v.getDocumentByKey(code, true);
			if( authDoc == null )
				throw new RuntimeException("Erreur � la suppression du document contenant les infos du code authorization");
			if( !authDoc.remove(true) )
				throw new RuntimeException("Je n'arrive pas � supprimer le document contenant les infos du code authorization");
		} catch (NotesException e) {
			throw new ServerErrorException(e);
		} finally {
			DominoUtils.recycleQuietly(authDoc);
			DominoUtils.recycleQuietly(v);
			DominoUtils.recycleQuietly(db);
		}
	}
	
	// =============================================================================
	
	/**
	 * Gestion du token.
	 * @param response la r�ponse http
	 * @throws IOException
	 */
	public void token(HttpServletResponse response) throws IOException {
		// Calcul l'objet r�ponse
		Object resp;
		try {
			Map<String, String> param = JSFUtils.getParam();
			String grantType = param.get("grant_type");
			if( grantType == null )
				throw new InvalidRequestException();
			
			// L'objet pour la r�ponse
			if( "authorization_code".equals(grantType) )
				resp = this.authorizationCode(
						param.get("code"),
						param.get("redirect_uri"),
						param.get("client_id")
				);
			else if( "refresh_token".equals(grantType) )
				resp = this.refreshToken(param.get("refresh_token"));
			else
				throw new UnsupportedGrantTypeException();
		} catch(GrantException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp = e.getError();
		} catch (ServerErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp = null;
		}
		
		// Envoi dans la stream http
		OutputStream out = null;
		OutputStreamWriter wrt = null;
		try {
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			if( resp != null ) {
				out = response.getOutputStream();
				wrt = new OutputStreamWriter(out, "UTF-8");
				wrt.write(GsonUtils.toJson(resp));
			}
		} finally {
			IOUtils.closeQuietly(wrt);
			IOUtils.closeQuietly(out);
		}
	}
	
	/**
	 * G�n�ration d'un token � partir d'un code autorisation
	 * @param code le code autorisation
	 * @param redirectUri l'uri de redirection
	 * @param clientId l'id de l'appli cliente
	 * @throws GrantException en cas de pb das le grant
	 * @throws ServerErrorException en cas d'erreur pendant l'acc�s au serveur
	 */
	public GrantResponse authorizationCode(String code, String redirectUri, String clientId) throws GrantException, ServerErrorException {
		if( code == null )
			throw new InvalidRequestException();
		if( redirectUri == null )
			throw new InvalidRequestException();
		if( clientId == null )
			throw new InvalidRequestException();
		
		GrantResponse resp = new GrantResponse();
		
		Name nn = null;
		Document authDoc = null;
		try {
			nn = this.session.createName(this.session.getEffectiveUserName());
			
			// R�cup�re l'application
			Application app = this.appBean.getApplicationFromName(nn.getCommon());
			if( app == null )
				throw new InvalidClientException();
			
			// R�cup�re le document correspondant au code authorization
			authDoc = this.v.getDocumentByKey(code, true);
			if( authDoc == null )
				throw new InvalidGrantException();
			AuthorizationCode authCode = DominoUtils.fillObject(new AuthorizationCode(), authDoc);
			
			// V�rifie qu'il n'est pas expir�
			long expired = (long) authCode.getExpires();
			if( expired < System.currentTimeMillis() )
				throw new InvalidGrantException();
			
			// V�rifie que le clientId est le bon
			if( !clientId.equals(authCode.getAud()) )
				throw new InvalidClientException();
			
			// V�rifie que l'uri de redirection est la m�me
			if( !redirectUri.equals(authCode.getRedirectUri()) )
				throw new InvalidGrantException();
			
			// G�n�re le access token. Il est sign� avec la cl� partag�e avec les serveurs de ressources.
			AccessToken accessToken = DominoUtils.fillObject(new AccessToken(), authDoc);
			accessToken.setAccessExp(System.currentTimeMillis() + this.paramsBean.getAccessTokenLifetime());
			JWSObject jwsObject = new JWSObject(
					new JWSHeader(JWSAlgorithm.HS256),
                    new Payload(GsonUtils.toJson(accessToken))
			);
			jwsObject.sign(new MACSigner(
					this.secretBean.getAccessTokenSecret()
			));
			resp.setAccessToken(jwsObject.serialize());
			
			// G�n�re le refresh token
			RefreshToken refreshToken = DominoUtils.fillObject(new RefreshToken(), authDoc);
			refreshToken.setRefreshExp(System.currentTimeMillis() + this.paramsBean.getRefreshTokenLifetime());
			JWEObject jweObject = new JWEObject(
					new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM), 
					new Payload(GsonUtils.toJson(refreshToken))
			);
			jweObject.encrypt(new DirectEncrypter(
					this.secretBean.getRefreshTokenSecret()
			));
			resp.setRefreshToken(jweObject.serialize());
			
			// La dur�e d'expiration. On prend celle du accessToken
			resp.setExpiresIn(accessToken.getAccessExp() - System.currentTimeMillis());
			
			// Le type de token
			resp.setTokenType("bearer");
			
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
			
			// Supprime le code authorization pour emp�cher une r�-utilisation
			this.removeCode(code);
		}
	}
	
	/**
	 * G�n�ration d'un token � partir d'un refresh token
	 * @throws GrantException 
	 * @throws ServerErrorException
	 */
	public GrantResponse refreshToken(String sRefreshToken) throws GrantException, ServerErrorException {
		if( sRefreshToken == null )
			throw new InvalidGrantException();
		
		try {
			// D�crypte le refresh token
			JWEObject jweObject = JWEObject.parse(sRefreshToken);
			jweObject.decrypt(new DirectDecrypter(this.secretBean.getRefreshTokenSecret()));
			String json = jweObject.getPayload().toString();
			RefreshToken refreshToken = GsonUtils.fromJson(json, RefreshToken.class);
			
			// V�rifie qu'il est valide
			if( refreshToken.getRefreshExp() < System.currentTimeMillis() )
				throw new InvalidGrantException();
			
			// Prolonge la dur�e de vie du refresh token
			refreshToken.setRefreshExp(System.currentTimeMillis() + this.paramsBean.getRefreshTokenLifetime());		// 10 heures
			
			// G�n�re l'access token
			AccessToken accessToken = new AccessToken();
			accessToken.setAccessExp(System.currentTimeMillis() + this.paramsBean.getAccessTokenLifetime());
			accessToken.setAud(refreshToken.getAud());
			accessToken.setAuthTime(refreshToken.getAuthTime());
			accessToken.setIat(refreshToken.getIat());
			accessToken.setIss(refreshToken.getIss());
			accessToken.setSub(refreshToken.getSub());
			
			// Cr�� les jwt et jwe
			JWSObject jwsObject = new JWSObject(
					new JWSHeader(JWSAlgorithm.HS256),
	                new Payload(GsonUtils.toJson(accessToken))
			);
			jwsObject.sign(new MACSigner(
					this.secretBean.getAccessTokenSecret()
			));
			jweObject = new JWEObject(
					new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM), 
					new Payload(GsonUtils.toJson(refreshToken))
			);
			jweObject.encrypt(new DirectEncrypter(
					this.secretBean.getRefreshTokenSecret()
			));
			
			GrantResponse resp = new GrantResponse();
			resp.setAccessToken(jwsObject.serialize());
			resp.setRefreshToken(jweObject.serialize());
			resp.setExpiresIn(accessToken.getAccessExp());		// Expiration en m�me temps que le refresh token
			
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
	
}