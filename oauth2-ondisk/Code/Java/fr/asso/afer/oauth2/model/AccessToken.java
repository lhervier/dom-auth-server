package fr.asso.afer.oauth2.model;

import fr.asso.afer.oauth2.utils.JsonUtils.JsonName;

/**
 * Le access token
 * @author Lionel HERVIER
 */
public class AccessToken extends Token {

	/**
	 * La date d'expiration
	 */
	private long accessExp;

	/**
	 * @return the accessExp
	 */
	@JsonName("exp")
	public long getAccessExp() {
		return accessExp;
	}

	/**
	 * @param accessExp the accessExp to set
	 */
	public void setAccessExp(long accessExp) {
		this.accessExp = accessExp;
	}
}
