package com.github.lhervier.domino.oauth.server.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {

	/**
	 * Encode un buffer en base 64
	 * @param buff le buffer � encoder
	 * @return la cha�ne en base 64
	 */
	public static final String encode(byte[] buff) {
		try {
			return new String(Base64.encodeBase64(buff), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Encode une cha�ne UTF-8
	 * @param s la cha�ne
	 * @return la version base64
	 */
	public static final String encodeFromUTF8String(String s) {
		try {
			return encode(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * D�code une cha�ne base 64
	 * @param s la cha�ne � d�coder
	 * @return le buffer
	 */
	public static final byte[] decode(String s) {
		try {
			return Base64.decodeBase64(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * D�code une cha�ne base 64 vers une autre cha�ne UTF-8
	 * @param s la cha�ne � d�coder
	 * @return la cha�ne d�cod�e
	 */
	public static final String decodeToUTF8String(String s) {
		try {
			return new String(decode(s), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
