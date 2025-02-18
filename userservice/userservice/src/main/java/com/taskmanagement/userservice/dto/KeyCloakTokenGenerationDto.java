package com.taskmanagement.userservice.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "keycloak.token.generate")
public class KeyCloakTokenGenerationDto {

	private String url;
	
	private String clientId;
	
	private String clientSecret;
	
	private String grantType;
	
}
