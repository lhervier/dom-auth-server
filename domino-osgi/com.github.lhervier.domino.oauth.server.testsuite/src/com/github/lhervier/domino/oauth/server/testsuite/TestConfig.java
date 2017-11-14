package com.github.lhervier.domino.oauth.server.testsuite;

import static org.mockito.Mockito.mock;

import java.io.UnsupportedEncodingException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import com.github.lhervier.domino.oauth.server.NotesPrincipal;
import com.github.lhervier.domino.oauth.server.OauthServerConfig;
import com.github.lhervier.domino.oauth.server.WebConfig;
import com.github.lhervier.domino.oauth.server.repo.ApplicationRepository;
import com.github.lhervier.domino.oauth.server.repo.AuthCodeRepository;
import com.github.lhervier.domino.oauth.server.repo.PersonRepository;
import com.github.lhervier.domino.oauth.server.repo.SecretRepository;
import com.github.lhervier.domino.spring.servlet.SpringServletConfig;

@Configuration
@Import(value = {SpringServletConfig.class, OauthServerConfig.class, WebConfig.class})
@PropertySource(value = "classpath:/test.properties")
public class TestConfig {

	@Bean
	@Primary
	public PersonRepository personRespository() {
		return mock(PersonRepository.class);
	}

	@Bean
	@Primary
	public ApplicationRepository applicationRepository() {
		return mock(ApplicationRepository.class);
	}
	
	@Bean
	@Primary
	public AuthCodeRepository authCodeRepository() {
		return mock(AuthCodeRepository.class);
	}
	
	@Bean
	@Primary
	public SecretRepository secretRepository() {
		return new SecretRepository() {
			@Override
			public byte[] findCryptSecret(String ssoConfig) {
				try {
					return "01234567890123456789012345678901".getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public byte[] findRefreshTokenSecret() {
				try {
					return "azertyuiopqsdfghjklmwxcvbnazerty".getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public byte[] findSignSecret(String ssoConfig) {
				try {
					return "98765432109876543210987654321098".getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}
	
	@Bean
	@Primary
	public NotesPrincipal notesPrincipal() {
		return new NotesPrincipalTestImpl();
	}
}