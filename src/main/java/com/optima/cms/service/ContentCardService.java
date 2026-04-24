package com.optima.cms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.cms.model.content.ContentCardResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Content card sections per page (Magnolia-backed later). Mock from classpath until delivery exists.
 */
@Service
public class ContentCardService {

	private static final Map<String, String> PAGE_TO_MOCK_RESOURCE = new LinkedHashMap<>();

	static {
		PAGE_TO_MOCK_RESOURCE.put("shop-home", "mock/content-card-shop-home.json");
		PAGE_TO_MOCK_RESOURCE.put("plan-gallery", "mock/content-card-plan-gallery.json");
	}

	private final ObjectMapper objectMapper;
	private final ConcurrentHashMap<String, ContentCardResponse> cachedMocks = new ConcurrentHashMap<>();

	public ContentCardService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * @param page page key (e.g. {@code shop-home}, {@code plan-gallery}); other values return an empty {@code docs} envelope.
	 */
	public ContentCardResponse getContentCards(String page) {
		String resource = resolveMockResource(page);
		if (resource == null) {
			return ContentCardResponse.empty();
		}
		return cachedMocks.computeIfAbsent(resource, this::loadMock);
	}

	private String resolveMockResource(String page) {
		if (page == null || page.isBlank()) {
			return null;
		}
		String key = page.trim();
		for (Map.Entry<String, String> e : PAGE_TO_MOCK_RESOURCE.entrySet()) {
			if (e.getKey().equalsIgnoreCase(key)) {
				return e.getValue();
			}
		}
		return null;
	}

	private ContentCardResponse loadMock(String classpathResource) {
		ClassPathResource resource = new ClassPathResource(classpathResource);
		try (InputStream in = resource.getInputStream()) {
			return objectMapper.readValue(in, ContentCardResponse.class);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to read classpath " + classpathResource, e);
		}
	}
}
