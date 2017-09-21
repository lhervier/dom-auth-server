package com.github.lhervier.domino.oauth.library.server.ext.impl;

import com.github.lhervier.domino.oauth.library.server.ext.openid.OpenIdContext;

public class IdToken extends OpenIdContext {

	/**
	 * Issued date
	 */
	private long iat;

	/**
	 * @return the iat
	 */
	public long getIat() {
		return iat;
	}

	/**
	 * @param iat the iat to set
	 */
	public void setIat(long iat) {
		this.iat = iat;
	}
}
