package com.example.authclient.security.keycloak;

import com.example.authclient.security.oauth2.OAuth2TokenService;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakService {
    
    private static final Logger logger = LoggerFactory.getLogger(KeycloakService.class);

    private final RestTemplate restTemplate;
    private final OAuth2TokenService oAuth2TokenService;
    private final OAuth2ClientProperties.Registration registration;
    private final KeycloakProperties keycloakProperties;

    public KeycloakService(RestTemplate restTemplate,
        OAuth2TokenService oAuth2TokenService,
        OAuth2ClientProperties oAuth2ClientProperties,
        KeycloakProperties keycloakProperties) {
            this.restTemplate = restTemplate;
            this.oAuth2TokenService = oAuth2TokenService;
            this.registration = oAuth2ClientProperties.getRegistration().get("todo");
            this.keycloakProperties = keycloakProperties;
    }

    public void logout() {
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("client_id", registration.getClientId());
        formParams.add("client_secret", registration.getClientSecret());
        formParams.add("refresh_token", oAuth2TokenService.getRefreshTokenValue());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        System.out.println("ーーーーーーーーーーーーーーー");
        RequestEntity<MultiValueMap<String, String>> requestEntity = 
            new RequestEntity<>(formParams, httpHeaders, HttpMethod.POST,
            URI.create(keycloakProperties.getLogoutUri()));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        logger.info("{}", responseEntity.getStatusCode());
        logger.info("{}", requestEntity.getBody());
    }
}
