package fr.asso.afer.oauth2.model.error.grant;

import fr.asso.afer.oauth2.Constants;
import fr.asso.afer.oauth2.model.error.GrantError;

public class UnsupportedGrantTypeError extends GrantError {

	/**
	 * Constructeur
	 */
	public UnsupportedGrantTypeError() {
		this.setError("unsupported_grant_type");
		this.setErrorDescription(
				"The authorization grant type is not supported by the " +
				"authorization server."
		);
		this.setErrorUri(Constants.NAMESPACE + "/error/grant/unsupported_grant_type");
	}
}
