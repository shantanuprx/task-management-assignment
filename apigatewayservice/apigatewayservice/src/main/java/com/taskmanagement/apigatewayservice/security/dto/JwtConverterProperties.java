package com.taskmanagement.apigatewayservice.security.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
public class JwtConverterProperties {

	private String resourceId;
	
    private String principalAttribute;
}
