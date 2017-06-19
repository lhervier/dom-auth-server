package com.github.lhervier.domino.oauth.library.client.bean;

import java.io.IOException;

import com.github.lhervier.domino.oauth.common.model.error.GrantError;
import com.github.lhervier.domino.oauth.common.utils.Callback;
import com.github.lhervier.domino.oauth.common.utils.JSFUtils;
import com.github.lhervier.domino.oauth.library.client.model.GrantResponse;
import com.github.lhervier.domino.oauth.library.client.utils.Utils;

public class RefreshBean {

	/**
	 * La bean pour acc�der aux param�tres
	 */
	private InitParamsBean initParamsBean;
	
	/**
	 * La bean pour acc�der a l'access token
	 */
	private AccessTokenBean accessTokenBean;
	
	/**
	 * Rafra�chit les tokens
	 * @throws IOException 
	 */
	public void refresh() throws IOException {
		String refreshToken = (String) JSFUtils.getSessionScope().get("refresh_token");
		
		// Refresh token pas pr�sent (session non initialis�e ou expir�e)
		if( refreshToken == null ) {
			this.accessTokenBean.sendToken();
			return;
		}
		
		Utils.createConnection(this.initParamsBean.getTokenEndPoint())
				.setTextContent(
						new StringBuffer()
								.append("grant_type=refresh_token&")
								.append("refresh_token=").append(refreshToken)
								.toString(), 
						"UTF-8"
				)
				
				// OK => Met � jour la session et retourne le token
				.onOk(new Callback<GrantResponse>() {
					@Override
					public void run(GrantResponse grant) throws IOException {
						JSFUtils.getSessionScope().put("refresh_token", grant.getRefreshToken());
						JSFUtils.getSessionScope().put("access_token", grant.getAccessToken());
						
						RefreshBean.this.accessTokenBean.sendToken();
					}
				})
				
				// Erreur => Non autoris�
				.onError(new Callback<GrantError>() {
					@Override
					public void run(GrantError error) throws IOException {
						JSFUtils.getSessionScope().put("refresh_token", null);
						JSFUtils.getSessionScope().put("access_token", null);
						
						RefreshBean.this.accessTokenBean.sendToken();
					}
				})
				
				.execute();
	}
	
	// ======================================================================

	/**
	 * @param accessTokenBean the accessTokenBean to set
	 */
	public void setAccessTokenBean(AccessTokenBean accessTokenBean) {
		this.accessTokenBean = accessTokenBean;
	}

	/**
	 * @param initParamsBean the initParamsBean to set
	 */
	public void setInitParamsBean(InitParamsBean initParamsBean) {
		this.initParamsBean = initParamsBean;
	}
}
