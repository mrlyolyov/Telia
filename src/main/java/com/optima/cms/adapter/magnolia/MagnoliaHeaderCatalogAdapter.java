package com.optima.cms.adapter.magnolia;

import com.fasterxml.jackson.databind.JsonNode;
import com.optima.cms.adapter.magnolia.header.MagnoliaHeaderDeliveryParser;
import com.optima.cms.model.theme.ThemeHeaderDeliveryPage;
import com.optima.cms.port.HeaderCatalogPort;
import org.springframework.stereotype.Component;

/**
 * Fetches {@code /.rest/delivery/header/v1} and maps JCR-style {@code results} to {@link ThemeHeaderDeliveryPage}.
 */
@Component
public class MagnoliaHeaderCatalogAdapter implements HeaderCatalogPort {

	private final MagnoliaClient magnoliaClient;

	public MagnoliaHeaderCatalogAdapter(MagnoliaClient magnoliaClient) {
		this.magnoliaClient = magnoliaClient;
	}

	@Override
	public ThemeHeaderDeliveryPage getHeaderDelivery() {
		JsonNode root = magnoliaClient.getHeaderDeliveryEnvelope();
		return MagnoliaHeaderDeliveryParser.parse(root);
	}
}
