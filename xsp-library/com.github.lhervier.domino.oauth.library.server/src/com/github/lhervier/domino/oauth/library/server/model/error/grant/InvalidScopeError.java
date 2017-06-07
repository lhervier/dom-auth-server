package com.github.lhervier.domino.oauth.library.server.model.error.grant;

import com.github.lhervier.domino.oauth.common.model.error.GrantError;
import com.github.lhervier.domino.oauth.library.server.utils.Utils;

public class InvalidScopeError extends GrantError {

	/**
	 * Constructeur
	 */
	public InvalidScopeError() {
		this.setError("invalid_scope");
		this.setErrorDescription(
				"The requested scope is invalid, unknown, malformed, or " +
				"exceeds the scope granted by the resource owner."
		);
		this.setErrorUri(Utils.getIssuer() + "/error/grant/invalid_scope");
	}
}
