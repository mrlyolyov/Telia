package com.optima.cms.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Validates Bearer JWTs from Keycloak and enforces a configurable realm role on {@code /api/**}.
 * <p>
 * Disabled when {@code keycloak.resource-server.enabled=false}: all requests are allowed (no Bearer validation).
 * When {@code true}, configure {@code spring.security.oauth2.resourceserver.jwt.issuer-uri} or {@code jwk-set-uri}.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${keycloak.resource-server.enabled:false}")
	private boolean keycloakResourceServerEnabled;

	@Value("${keycloak.resource-server.required-role:ESB_AGENT}")
	private String requiredRole;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectProvider<JwtDecoder> jwtDecoder) throws Exception {
		http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.csrf(csrf -> csrf.disable());

		if (!keycloakResourceServerEnabled) {
			http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
			return http.build();
		}

		if (jwtDecoder.getIfAvailable() == null) {
			throw new IllegalStateException(
					"keycloak.resource-server.enabled=true but no JwtDecoder bean: set "
							+ "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://<host>/realms/<realm> "
							+ "or spring.security.oauth2.resourceserver.jwt.jwk-set-uri=<jwks-url> "
							+ "(Spring Boot only auto-configures JwtDecoder when one of these properties is set).");
		}

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());

		String role = requiredRole.startsWith("ROLE_") ? requiredRole.substring("ROLE_".length()) : requiredRole;

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
				.requestMatchers(
						"/swagger-ui.html",
						"/swagger-ui/**",
						"/v3/api-docs/**")
				.permitAll()
				.requestMatchers("/api/**").hasRole(role)
				.anyRequest().authenticated());
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

		return http.build();
	}
}
