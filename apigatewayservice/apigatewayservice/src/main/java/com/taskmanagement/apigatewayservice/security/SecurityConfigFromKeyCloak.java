package com.taskmanagement.apigatewayservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class SecurityConfigFromKeyCloak {

	@Autowired
	private final JWTConvertorFromKeyCloak jwtConvertorFromKeyCloak;

	public static final String ADMIN = "ADMIN_ROLE";

	public static final String USER = "USER_ROLE";

	@Bean
	SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
		.authorizeExchange(request ->

		request.pathMatchers(HttpMethod.POST, "/auth/*").permitAll()
				.pathMatchers(HttpMethod.GET, "/api/submissions/**").hasRole(USER)
				.pathMatchers(HttpMethod.POST, "/api/submissions/**").hasRole(USER)
				.pathMatchers(HttpMethod.PUT, "/api/submissions/**").hasRole(USER)
				.pathMatchers(HttpMethod.GET, "/api/tasks/**").hasRole(ADMIN)
				.pathMatchers(HttpMethod.POST, "/api/tasks/**").hasRole(ADMIN)
				.pathMatchers(HttpMethod.PUT, "/api/tasks/**").hasRole(ADMIN)
				.pathMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole(USER, ADMIN)
				.pathMatchers(HttpMethod.POST, "/api/users/**").hasAnyRole(USER, ADMIN)
				.pathMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole(USER, ADMIN)
				.anyExchange().authenticated())
		;

		httpSecurity.oauth2ResourceServer( oAuth2 -> oAuth2.jwt(
					jwt ->  jwt.jwtAuthenticationConverter(jwtConvertorFromKeyCloak)));
		
		return httpSecurity.build();
	}

}
