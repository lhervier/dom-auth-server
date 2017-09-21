package com.github.lhervier.domino.oauth.library.server.utils;

import java.io.IOException;
import java.util.Map;

import lotus.domino.NotesException;

import org.codehaus.jackson.map.ObjectMapper;

import com.github.lhervier.domino.oauth.library.server.ext.IPropertyAdder;
import com.github.lhervier.domino.oauth.library.server.services.SecretService;
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
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;

public class PropertyAdderImpl implements IPropertyAdder {

	/**
	 * Map to add properties
	 */
	private Map<String, Object> dest;
	
	/**
	 * La cl� � utiliser pour signer
	 */
	private String signKey;
	
	/**
	 * La cl� � utiliser pour crypter
	 */
	private String cryptKey;
	
	/**
	 * La bean pour acc�der aux secrets
	 */
	private SecretService secretBean;
	
	/**
	 * The jackson mapper
	 */
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Constructeur
	 */
	public PropertyAdderImpl(
			Map<String, Object> dest, 
			SecretService secretBean,
			String signKey,
			String cryptKey) {
		this.dest = dest;
		this.signKey = signKey;
		this.cryptKey = cryptKey;
		this.secretBean = secretBean;
	}
	
	/**
	 * @see com.github.lhervier.domino.oauth.library.server.ext.IPropertyAdder#addCryptedProperty(String, Object)
	 */
	@Override
	public void addCryptedProperty(String name, Object obj) {
		if( dest.containsKey(name) )
			throw new RuntimeException("La propri�t� '" + name + "' est d�j� d�finie dans la r�ponse au grant.");
		
		try {
			byte[] secret = this.secretBean.getCryptSecret(this.cryptKey);
			JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM, null, null, null, null, null, null, null, null, null, this.cryptKey, null, null, null, null, null, 0, null, null, null, null);
			JWEObject jweObject = new JWEObject(
					header, 
					new Payload(this.mapper.writeValueAsString(obj))
			);
			jweObject.encrypt(new DirectEncrypter(secret));
			String jwe = jweObject.serialize();
			this.dest.put(name, jwe);
		} catch (KeyLengthException e) {
			throw new RuntimeException(e);
		} catch (JOSEException e) {
			throw new RuntimeException(e);
		} catch(IOException e) {
			throw new RuntimeException(e);
		} catch (NotesException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see com.github.lhervier.domino.oauth.library.server.ext.IPropertyAdder#addSignedProperty(String, Object)
	 */
	@Override
	public void addSignedProperty(String name, Object obj) {
		if( dest.containsKey(name) )
			throw new RuntimeException("La propri�t� '" + name + "' est d�j� d�finie dans la r�ponse au grant.");
		
		try {
			byte[] secret = this.secretBean.getSignSecret(this.signKey);
			JWSHeader header = new JWSHeader(JWSAlgorithm.HS256, null, null, null, null, null, null, null, null, null, this.signKey, null, null);
			JWSObject jwsObject = new JWSObject(
					header,
	                new Payload(this.mapper.writeValueAsString(obj))
			);
			jwsObject.sign(new MACSigner(secret));
			String jws = jwsObject.serialize();
			
			this.dest.put(name, jws);
		} catch (KeyLengthException e) {
			throw new RuntimeException(e);
		} catch (JOSEException e) {
			throw new RuntimeException(e);
		} catch (NotesException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
