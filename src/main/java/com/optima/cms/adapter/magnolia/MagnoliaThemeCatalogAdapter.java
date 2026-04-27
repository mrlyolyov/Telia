package com.optima.cms.adapter.magnolia;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.cms.adapter.magnolia.theme.MagnoliaThemeDeliveryParser;
import com.optima.cms.model.theme.ThemeDeliveryPage;
import com.optima.cms.port.ThemeCatalogPort;
import org.springframework.stereotype.Component;

/**
 * Fetches {@code /.rest/delivery/theme/v1} and maps {@code results} to {@link ThemeDeliveryPage}.
 */
@Component
public class MagnoliaThemeCatalogAdapter implements ThemeCatalogPort {

	private final MagnoliaClient magnoliaClient;
	private final ObjectMapper objectMapper;

	public MagnoliaThemeCatalogAdapter(MagnoliaClient magnoliaClient, ObjectMapper objectMapper) {
		this.magnoliaClient = magnoliaClient;
		this.objectMapper = objectMapper;
	}

	@Override
	public ThemeDeliveryPage getThemeDelivery() {
		JsonNode root = magnoliaClient.getThemeDeliveryEnvelope();
		return MagnoliaThemeDeliveryParser.parse(root, objectMapper);
	}
}
