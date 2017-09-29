package com.github.lhervier.domino.oauth.server.ext.openid;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lotus.domino.Document;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.lhervier.domino.oauth.common.utils.DominoUtils;
import com.github.lhervier.domino.oauth.common.utils.ReflectionUtils;
import com.github.lhervier.domino.oauth.common.utils.SystemUtils;
import com.github.lhervier.domino.oauth.server.ext.IOAuthExtension;
import com.github.lhervier.domino.oauth.server.ext.IPropertyAdder;
import com.github.lhervier.domino.oauth.server.ext.IScopeGranter;
import com.github.lhervier.domino.oauth.server.model.AuthorizationCode;
import com.github.lhervier.domino.oauth.server.services.NabService;

/**
 * Impl�mentation de OpenID par dessus OAUth2
 * @author Lionel HERVIER
 */
@Component
public class OpenIDExt implements IOAuthExtension<OpenIdContext> {

	/**
	 * The issuer
	 */
	@Value("${oauth2.server.openid.iss}")
	private String iss;
	
	/**
	 * The sign key
	 */
	@Value("${oauth2.server.openid.signKey}")
	private String signKey;
	
	/**
	 * The http servlet request
	 */
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * The NAB service
	 */
	@Autowired
	private NabService nabSvc;
	
	/**
	 * @see com.github.lhervier.domino.oauth.server.ext.IOAuthExtension#getContextClass()
	 */
	@Override
	public Class<OpenIdContext> getContextClass() {
		return OpenIdContext.class;
	}

	/**
	 * @see com.github.lhervier.domino.oauth.server.ext.IOAuthExtension#validateResponseTypes(List)
	 */
	@Override
	public boolean validateResponseTypes(List<String> responseTypes) {
		return responseTypes.contains("id_token");
	}

	/**
	 * @see com.github.lhervier.domino.oauth.server.ext.IOAuthExtension#getId()
	 */
	@Override
	public String getId() {
		return "openid";
	}

	/**
	 * @see com.github.lhervier.domino.oauth.server.ext.IOAuthExtension#initContext(Session, IScopeGranter, String, List)
	 */
	@Override
	public OpenIdContext initContext(
			Session session,
			IScopeGranter granter, 
			String clientId, 
			List<String> scopes) throws NotesException {
		Document doc = null;
		try {
			// On ne r�agit que si on nous demande le scope "openid"
			if( !scopes.contains("openid") )
				return null;
			granter.grant("openid");
			
			// Les attributs par d�faut
			OpenIdContext ctx = new OpenIdContext();
			ctx.setIss(this.iss);
			ctx.setSub(session.getEffectiveUserName());
			ctx.setAud(clientId);
			ctx.setAcr(null);				// TODO: acr non g�n�r�
			ctx.setAmr(null);				// TODO: amr non g�n�r�
			ctx.setAzp(null);				// TODO: azp non g�n�r�
			ctx.setAuthTime(SystemUtils.currentTimeSeconds());
			if( this.request.getParameter("nonce") != null )
				ctx.setNonce(this.request.getParameter("nonce"));
			else
				ctx.setNonce(null);
			
			doc = this.nabSvc.getPersonDoc(session.getEffectiveUserName());
			
			if( scopes.contains("profile") ) {
				granter.grant("profile");
				
				Name nn = null;
				try {
					nn = session.createName(session.getEffectiveUserName());
					ctx.setName(nn.getCommon());
				} finally {
					DominoUtils.recycleQuietly(nn);
				}
				ctx.setGivenName(doc.getItemValueString("FirstName"));
				ctx.setFamilyName(doc.getItemValueString("LastName"));
				ctx.setMiddleName(doc.getItemValueString("MiddleInitial"));
				ctx.setGender(doc.getItemValueString("Title"));		// FIXME: OpenId says it should "male" or "female". We will send "Mr.", "Miss", "Dr.", etc... 
				ctx.setPreferedUsername(doc.getItemValueString("ShortName"));
				ctx.setWebsite(doc.getItemValueString("WebSite"));
				ctx.setPicture(doc.getItemValueString("PhotoUrl"));
				
				ctx.setUpdatedAt(null);						// FIXME: Information is present in the "LastMod" field
				ctx.setLocale(null);						// FIXME: Preferred language is in the "preferredLanguage" field. But it's not a locale.
				
				ctx.setZoneinfo(null);						// Time zone
				ctx.setBirthdate(null);						// Date of birth
				ctx.setProfile(null);						// Profile page URL
				ctx.setNickname(null);						// "Mike" for someone called "Mickael"
			}
			
			if( scopes.contains("email") ) {
				granter.grant("email");
				ctx.setEmail(doc.getItemValueString("InternetAddress"));
				ctx.setEmailVerified(null);
			}
			
			if( scopes.contains("address") ) {
				granter.grant("address");
				// FIXME: Extract street address.
				ctx.setAddress(null);
			}
			
			if( scopes.contains("phone") ) {
				granter.grant("phone");
				ctx.setPhoneNumber(doc.getItemValueString("OfficePhoneNumber"));
				ctx.setPhoneNumberVerified(null);
			}
			
			return ctx;
		} finally {
			DominoUtils.recycleQuietly(doc);
		}
	}

	/**
	 * @see com.github.lhervier.domino.oauth.server.ext.IOAuthExtension#authorize(Object, List, AuthorizationCode, IPropertyAdder)
	 */
	@Override
	public void authorize(OpenIdContext ctx, List<String> responseTypes, AuthorizationCode authCode, IPropertyAdder adder) throws NotesException {
		// Hybrid flow
		if( responseTypes.contains("id_token") ) {
			this.token(
					ctx, 
					adder, 
					authCode.getGrantedScopes()
			);
		}
	}

	/**
	 * Returns an Id token
	 */
	public IdToken createIdToken(OpenIdContext context, List<String> scopes) {
		if( !scopes.contains("openid") )
			return null;
		
		IdToken idToken = new IdToken();
		idToken.setIat(SystemUtils.currentTimeSeconds());
		
		// Main properties
		ReflectionUtils.copyProperties(
				context, 
				idToken, 
				new String[] {"iss", "sub", "aud", "exp", "acr", "amr", "azp", "authTime", "nonce"}
		);
		
		// Profile properties
		if( scopes.contains("profile") ) {
			ReflectionUtils.copyProperties(
					context, 
					idToken, 
					new String[] {"name", "familyName", "givenName", "middleName", "nickname", "preferedUsername", "profile", "picture", "website", "gender", "birthdate", "zoneinfo", "locale", "updatedAt"}
			);
		}
		
		// Email properties
		if( scopes.contains("email") ) {
			ReflectionUtils.copyProperties(
					context, 
					idToken, 
					new String[] {"email", "emailVerified"}
			);
		}
		
		// Address properties
		if( scopes.contains("address") ) {
			ReflectionUtils.copyProperties(
					context, 
					idToken, 
					new String[] {"address"}
			);
		}
		
		// Phone properties
		if( scopes.contains("phone") ) {
			ReflectionUtils.copyProperties(
					context, 
					idToken, 
					new String[] {"phoneNumber", "phoneNumberVerified"}
			);
		}
		
		return idToken;
	}
	
	/**
	 * @see com.github.lhervier.domino.oauth.server.ext.IOAuthExtension#token(Object, IPropertyAdder, List)
	 */
	@Override
	public void token(
			OpenIdContext context, 
			IPropertyAdder adder,
			List<String> scopes) {
		IdToken idToken = this.createIdToken(context, scopes);
		if( idToken == null )
			return;
		
		adder.addSignedProperty("id_token", idToken, this.signKey);
	}
}