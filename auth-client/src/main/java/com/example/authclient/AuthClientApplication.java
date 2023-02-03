package com.example.authclient;

import com.example.authclient.security.oauth2.OAuth2TokenService;
import com.example.authclient.web.filter.LoggingFilter;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class AuthClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthClientApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean loggingFilter() {
		LoggingFilter loggingFilter = new LoggingFilter();
		FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>(loggingFilter);
		registrationBean.setOrder(Integer.MIN_VALUE);

		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, OAuth2TokenService oAuth2TokenService) {
		return builder.setConnectTimeout(Duration.ofMillis(500))
			.setReadTimeout(Duration.ofMillis(500))
			.additionalRequestCustomizers(addAccessTokenToHeader(oAuth2TokenService))
			.build();
	}

	private RestTemplateRequestCustomizer addAccessTokenToHeader(OAuth2TokenService oAuth2TokenService) {
		return request -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication == null || !(authentication instanceof OAuth2AuthenticationToken)) {
				return;
			}
			request.getHeaders().setBearerAuth(oAuth2TokenService.getAcceTokenValue());
		};

	}

}
