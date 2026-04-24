package com.optima.cms.adapter.magnolia;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.port.DeviceCatalogPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Magnolia-backed device catalog. Until Magnolia exposes a delivery API, serves a classpath mock
 * ({@code mock/device-findall.json}) with the full response envelope.
 */
@Component
@Slf4j
public class MagnoliaDeviceCatalogAdapter implements DeviceCatalogPort {

	private static final String MOCK_RESOURCE = "mock/device-findall.json";

	private final ObjectMapper objectMapper;
	private volatile JsonNode cachedRoot;

	public MagnoliaDeviceCatalogAdapter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public JsonNode getDeviceCatalog(FindAllRequest request) {
		JsonNode base = loadMockRoot();
		return applyExternalIdFilter(base, request);
	}

	private JsonNode loadMockRoot() {
		JsonNode local = cachedRoot;
		if (local != null) {
			return local;
		}
		synchronized (this) {
			if (cachedRoot != null) {
				return cachedRoot;
			}
			ClassPathResource resource = new ClassPathResource(MOCK_RESOURCE);
			try (InputStream in = resource.getInputStream()) {
				cachedRoot = objectMapper.readTree(in);
				return cachedRoot;
			} catch (IOException e) {
				throw new IllegalStateException("Failed to read classpath " + MOCK_RESOURCE, e);
			}
		}
	}

	/**
	 * When {@code externalId} is provided, keep only matching docs (trimmed, exact match on {@code externalId}).
	 * If none match, returns the full mock. Updates {@code totalDocs} when filtering returns a non-empty subset.
	 */
	private JsonNode applyExternalIdFilter(JsonNode base, FindAllRequest request) {
		if (base == null || !base.isObject()) {
			return base;
		}
		JsonNode docsNode = base.get("docs");
		if (docsNode == null || !docsNode.isArray()) {
			return base;
		}
		List<String> requested = request != null ? request.getExternalId() : null;
		if (requested == null || requested.isEmpty()) {
			return base;
		}
		Set<String> wanted = requested.stream()
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toCollection(LinkedHashSet::new));
		if (wanted.isEmpty()) {
			return base;
		}
		ArrayNode filtered = objectMapper.createArrayNode();
		for (JsonNode d : docsNode) {
			if (d != null && d.isObject() && d.hasNonNull("externalId")) {
				String ext = d.get("externalId").asText("").trim();
				if (wanted.contains(ext)) {
					filtered.add(d);
				}
			}
		}
		if (filtered.isEmpty()) {
			log.info("No device documents matched externalId filter {}; returning full mock catalog", wanted);
			return base;
		}
		ObjectNode copy = base.deepCopy();
		copy.set("docs", filtered);
		copy.put("totalDocs", filtered.size());
		return copy;
	}
}
