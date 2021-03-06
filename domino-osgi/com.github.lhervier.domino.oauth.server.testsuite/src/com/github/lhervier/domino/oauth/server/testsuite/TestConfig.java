package com.github.lhervier.domino.oauth.server.testsuite;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import com.github.lhervier.domino.oauth.server.OauthServerConfig;
import com.github.lhervier.domino.oauth.server.WebConfig;
import com.github.lhervier.domino.oauth.server.repo.ApplicationRepository;
import com.github.lhervier.domino.oauth.server.repo.AuthCodeRepository;
import com.github.lhervier.domino.oauth.server.repo.PersonRepository;
import com.github.lhervier.domino.oauth.server.repo.SecretRepository;
import com.github.lhervier.domino.oauth.server.services.ExtensionService;
import com.github.lhervier.domino.oauth.server.services.TimeService;
import com.github.lhervier.domino.oauth.server.services.impl.ExtensionServiceImpl;
import com.github.lhervier.domino.oauth.server.testsuite.impl.SecretRepositoryTestImpl;
import com.github.lhervier.domino.oauth.server.testsuite.impl.TimeServiceTestImpl;
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
		return new SecretRepositoryTestImpl();
	}
	
	@Bean
	@Primary
	public TimeService timeService() {
		return new TimeServiceTestImpl();
	}
	
	@Bean
	@Primary
	public ExtensionService extensionService() {
		return mock(ExtensionServiceImpl.class);
	}
}
