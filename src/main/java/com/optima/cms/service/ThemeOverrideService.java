package com.optima.cms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Theme override payload until Magnolia exposes a delivery endpoint.
 */
@Service
public class ThemeOverrideService {

	private static final String MOCK_RESOURCE = "mock/theme-override.json";

	private final ObjectMapper objectMapper;
	private volatile JsonNode cached;

	public ThemeOverrideService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public JsonNode getThemeOverride() {
		JsonNode local = cached;
		if (local != null) {
			return local;
		}
		synchronized (this) {
			if (cached != null) {
				return cached;
			}
			ClassPathResource resource = new ClassPathResource(MOCK_RESOURCE);
			try (InputStream in = resource.getInputStream()) {
				cached = objectMapper.readTree(in);
				return cached;
			} catch (IOException e) {
				throw new IllegalStateException("Failed to read classpath " + MOCK_RESOURCE, e);
			}
		}
	}
}
