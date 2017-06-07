package com.github.lhervier.domino.oauth.common.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;

public class HttpUtils<T, E> {

	/**
	 * L'URL � appeler
	 */
	private String url;
	
	/**
	 * Le type de retour si OK
	 */
	private Class<T> okType;
	
	/**
	 * Le type de retour si KO
	 */
	private Class<E> errorType;
	
	/**
	 * La callback si c'est OK
	 */
	private Callback<T> okCallback;
	
	/**
	 * La callback si c'est KO
	 */
	private Callback<E> errorCallback;
	
	/**
	 * Les headers � ajouter
	 */
	private Map<String, String> headers = new HashMap<String, String>();
	
	/**
	 * Un �ventuel hostname verifier (si connection https)
	 */
	private HostnameVerifier verifier = null;
	
	/**
	 * Un �ventuel SSLFactory (si connection https)
	 */
	private SSLSocketFactory factory = null;
	
	/**
	 * Le contenu � envoyer
	 */
	private InputStream content;
	
	/**
	 * Initialise une connection 
	 * @param <T> le type de retour si OK (200)
	 * @param <E> le type de retour si KO
	 * @param url l'url � appeler
	 * @param okType le type de retour si OK
	 * @param errorType le type de retour si KO
	 * @return 
	 */
	public static final <T, E> HttpUtils<T, E> createConnection(String url, Class<T> okType, Class<E> errorType) {
		HttpUtils<T, E> ret = new HttpUtils<T, E>();
		ret.url = url;
		ret.okType = okType;
		ret.errorType = errorType;
		return ret;
	}
	public static final <T, E> HttpUtils<T, E> createConnection(String url) {
		return HttpUtils.createConnection(url, null, null);
	}
	
	/**
	 * D�fini la callback quand on re�oit une r�ponse correct
	 * @param callback la callback
	 * @return
	 */
	public HttpUtils<T, E> onOk(Callback<T> callback) {
		this.okCallback = callback;
		return this;
	}
	
	/**
	 * D�fini la callback quand on re�oit une erreur
	 * @param callback la callback
	 * @return
	 */
	public HttpUtils<T, E> onError(Callback<E> callback) {
		this.errorCallback = callback;
		return this;
	}
	
	/**
	 * Ajoute un header
	 * @param name le nom du header
	 * @param value la valeur
	 * @return
	 */
	public HttpUtils<T, E> addHeader(String name, String value) {
		this.headers.put(name, value);
		return this;
	}
	
	/**
	 * Pour ajouter un verifier
	 * @param verifier le verifier
	 */
	public HttpUtils<T, E> withVerifier(HostnameVerifier verifier) {
		this.verifier = verifier;
		return this;
	}
	
	/**
	 * Pour ajouter un SSLFactory
	 * @param factory la factory
	 */
	public HttpUtils<T, E> withFactory(SSLSocketFactory factory) {
		this.factory = factory;
		return this;
	}
	
	/**
	 * Pour d�finir un contenu texte
	 * @param content le contenu
	 * @param encoding l'encodage � utiliser
	 * @throws UnsupportedEncodingException 
	 */
	public HttpUtils<T, E> setTextContent(String content, String encoding) throws UnsupportedEncodingException {
		this.content = new ByteArrayInputStream(content.getBytes(encoding));
		return this;
	}
	
	/**
	 * Pour d�finir un contenu objet � serialiser en Json
	 * @param content le contenu
	 * @param encoding l'encodage
	 * @throws UnsupportedEncodingException 
	 */
	public HttpUtils<T, E> setJsonContent(Object content, String encoding) throws UnsupportedEncodingException {
		this.content = new ByteArrayInputStream(GsonUtils.toJson(content).getBytes(encoding));
		return this;
	}
	
	/**
	 * Pour d�finir un contenu
	 * @param content le contenu
	 */
	public HttpUtils<T, E> setContent(byte[] content) {
		this.content = new ByteArrayInputStream(content);
		return this;
	}
	
	/**
	 * Pour d�finir un contenu
	 * @param in stream vers le contenu
	 */
	public HttpUtils<T, E> setContent(InputStream in) {
		this.content = in;
		return this;
	}
	
	/**
	 * Emet la requ�te
	 * @throws IOException 
	 */
	public void execute() throws IOException {
		URL url = new URL(this.url);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();;
		if( "https".equals(url.getProtocol()) ) {
			HttpsURLConnection conns = (HttpsURLConnection) conn;
			if( this.verifier != null )
				conns.setHostnameVerifier(this.verifier);
			
			if( this.factory != null )
				conns.setSSLSocketFactory(this.factory);
		}
		
		InputStream in = null;
		Reader reader = null;
		try {
			// Input et output. Va �mettre un GET ou un POST
			conn.setDoInput(this.okCallback != null || this.errorCallback != null);
			conn.setDoOutput(this.content != null);
			
			// Ajoute les en t�tes http
			for( Entry<String, String> entry : this.headers.entrySet() )
				conn.addRequestProperty(entry.getKey(), entry.getValue());
			
			// Envoi l'objet
			if( this.content != null ) {
				OutputStream out = null;
				try {
					out = conn.getOutputStream();
					IOUtils.copy(this.content, out);
				} finally {
					IOUtils.closeQuietly(out);
					IOUtils.closeQuietly(this.content);
				}
			}
			
			// Charge la r�ponse (du JSON)
			StringBuffer sb = new StringBuffer();
			if( this.okCallback != null || this.errorCallback != null ) {
				in = conn.getInputStream();
				reader = new InputStreamReader(in, "UTF-8");
				char[] buff = new char[4 * 1024];
				int read = reader.read(buff);
				while( read != -1 ) {
					sb.append(buff, 0, read);
					read = reader.read(buff);
				}
			}
			
			// Code 200 => OK
			if( conn.getResponseCode() == 200 && this.okCallback != null ) {
				T resp = GsonUtils.fromJson(sb.toString(), this.okType);
				this.okCallback.run(resp);
				
			// Code autre => Erreur
			} else if( conn.getResponseCode() != 200 && this.errorCallback != null ) {
				E resp = GsonUtils.fromJson(sb.toString(), this.errorType);
				this.errorCallback.run(resp);
			}
		} catch(IOException e) {
			throw e;
		} catch(Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(in);
			conn.disconnect();
		}
	}
}
