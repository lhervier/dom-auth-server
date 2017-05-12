package fr.asso.afer.oauth2.model;

import fr.asso.afer.oauth2.utils.JsonUtils.SerializedName;

/**
 * Le token de rafra�chissement
 * @author Lionel HERVIER
 */
public class RefreshToken extends Token {

	/**
	 * La date d'expiration
	 */
	private long refreshExp;

	/**
	 * @return the refreshExp
	 */
	@SerializedName("exp")
	public long getRefreshExp() {
		return refreshExp;
	}

	/**
	 * @param refreshExp the refreshExp to set
	 */
	public void setRefreshExp(long refreshExp) {
		this.refreshExp = refreshExp;
	}
}
