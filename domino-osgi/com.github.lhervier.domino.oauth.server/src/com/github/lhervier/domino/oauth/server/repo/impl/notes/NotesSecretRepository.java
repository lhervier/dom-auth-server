package com.github.lhervier.domino.oauth.server.repo.impl.notes;

import java.io.IOException;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.github.lhervier.domino.oauth.server.AuthContext;
import com.github.lhervier.domino.oauth.server.repo.SecretRepository;
import com.github.lhervier.domino.oauth.server.utils.Base64Utils;
import com.github.lhervier.domino.oauth.server.utils.DominoUtils;

/**
 * Registre pour m�moriser les secrets
 * @author Lionel HERVIER
 */
@Repository
public class NotesSecretRepository implements SecretRepository {

	/**
	 * Le nom de la vue qui contient les configs SSO
	 */
	public static final String WEBSSOCONFIG_VIEW = "($WebSSOConfigs)";
	
	/**
	 * Le nom du champ dans lequel r�cup�rer le secret
	 */
	public static final String SECRET_FIELD_NAME = "LTPA_DominoSecret";
	
	/**
	 * Name of the LTPA config used to encrypt refresh tokens
	 */
	@Value("${oauth2.server.refreshTokenConfig}")
	private String refreshTokenConfig;
	
	/**
	 * The notes context
	 */
	@Autowired
	private AuthContext authContext;
	
	/**
	 * Retourne un secret
	 * @param base64 le secret en base 64
	 * @param size la taille finale
	 * @throws IOException 
	 */
	private byte[] genSecret(String base64, int size) throws IOException {
		byte[] min = Base64Utils.decode(base64);
		byte[] ret = new byte[size];
		for( int nb = 0; nb < size; nb++ )
			ret[nb] = min[nb % min.length];
		return ret;
	}
	
	/**
	 * Retourne un secret
	 * @param ssoConfig la config sso
	 * @param size la taille
	 */
	private byte[] getSecret(String ssoConfig, int size) throws NotesException, IOException {
		if( ssoConfig == null )
			return null;
		Database nab = null;
		View v = null;
		Document docSsoConfig = null;
		try {
			nab = DominoUtils.openDatabase(this.authContext.getServerSession(), "names.nsf");
			v = nab.getView(WEBSSOCONFIG_VIEW);
			if( v == null )
				throw new RuntimeException("La vue " + WEBSSOCONFIG_VIEW + " n'existe pas dans le NAB. Impossible de continuer.");
			docSsoConfig = v.getDocumentByKey(ssoConfig);
			if( docSsoConfig == null )
				return null;
			String secret = docSsoConfig.getItemValueString(SECRET_FIELD_NAME);
			return this.genSecret(secret, size);
		} finally {
			DominoUtils.recycleQuietly(docSsoConfig);
			DominoUtils.recycleQuietly(v);
			DominoUtils.recycleQuietly(nab);
		}
	}
	
	// ==================================================================================
	
	/**
	 * Retourne un secret pour signer
	 * @param ssoConfig la config sso
	 */
	public byte[] findSignSecret(String ssoConfig) throws NotesException, IOException {
		return this.getSecret(ssoConfig, 32);
	}
	
	/**
	 * Retourne un secret pour crypter
	 * @param ssoConfig la config sso
	 */
	public byte[] findCryptSecret(String ssoConfig) throws NotesException, IOException {
		return this.getSecret(ssoConfig, 16);
	}
	
	/**
	 * Retourne le secret utilis� pour crypter le refresh token
	 * @throws IOException 
	 */
	public byte[] findRefreshTokenSecret() throws NotesException, IOException {
		return this.findCryptSecret(this.refreshTokenConfig);
	}
}
