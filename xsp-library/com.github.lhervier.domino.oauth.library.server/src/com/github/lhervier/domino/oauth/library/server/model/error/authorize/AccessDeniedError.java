package com.github.lhervier.domino.oauth.library.server.model.error.authorize;

import com.github.lhervier.domino.oauth.common.model.error.AuthorizeError;
import com.github.lhervier.domino.oauth.library.server.utils.Utils;

/**
 * Erreur d'autorisation
 * @author Lionel HERVIER
 */
public class AccessDeniedError extends AuthorizeError {

	/**
	 * Constructeur
	 */
	public AccessDeniedError() {
		this.setError("access_denied");
		this.setErrorDescription("The resource owner or authorization server denied the request.");
		this.setErrorUri(Utils.getIssuer() + "/error/authorize/access_denied");
	}
}
