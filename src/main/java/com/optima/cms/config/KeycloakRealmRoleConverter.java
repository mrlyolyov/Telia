package com.optima.cms.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * Maps Keycloak {@code realm_access.roles} (and optional resource client roles) to Spring authorities,
 * matching the role checks used alongside cms-adapter.
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	private final JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		Collection<GrantedAuthority> fromScopes = scopeConverter.convert(jwt);
		Collection<GrantedAuthority> realm = realmRoles(jwt);
		Collection<GrantedAuthority> resource = resourceRoles(jwt);
		Collection<GrantedAuthority> combined = new java.util.ArrayList<>(fromScopes);
		combined.addAll(realm);
		combined.addAll(resource);
		return combined;
	}

	@SuppressWarnings("unchecked")
	private static Collection<GrantedAuthority> realmRoles(Jwt jwt) {
		Map<String, Object> realmAccess = jwt.getClaim("realm_access");
		if (realmAccess == null) {
			return Collections.emptyList();
		}
		Object rolesObj = realmAccess.get("roles");
		if (!(rolesObj instanceof List<?>)) {
			return Collections.emptyList();
		}
		List<?> roles = (List<?>) rolesObj;
		return roles.stream()
				.filter(String.class::isInstance)
				.map(r -> new SimpleGrantedAuthority("ROLE_" + (String) r))
				.collect(Collectors.toSet());
	}

	@SuppressWarnings("unchecked")
	private static Collection<GrantedAuthority> resourceRoles(Jwt jwt) {
		Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
		if (resourceAccess == null || resourceAccess.isEmpty()) {
			return Collections.emptyList();
		}
		return resourceAccess.values().stream()
				.filter(Map.class::isInstance)
				.map(m -> (Map<String, Object>) m)
				.map(m -> m.get("roles"))
				.filter(List.class::isInstance)
				.flatMap(roles -> ((List<?>) roles).stream())
				.filter(String.class::isInstance)
				.map(r -> new SimpleGrantedAuthority("ROLE_" + (String) r))
				.collect(Collectors.toSet());
	}
}
