package com.optima.cms.adapter.magnolia;

import com.fasterxml.jackson.databind.JsonNode;
import com.optima.cms.adapter.magnolia.footer.MagnoliaFooterDeliveryParser;
import com.optima.cms.model.theme.ThemeFooterDeliveryPage;
import com.optima.cms.port.FooterCatalogPort;
import org.springframework.stereotype.Component;

/**
 * Fetches {@code /.rest/delivery/footer/v1} and maps JCR-style {@code results} to {@link ThemeFooterDeliveryPage}.
 */
@Component
public class MagnoliaFooterCatalogAdapter implements FooterCatalogPort {

	private final MagnoliaClient magnoliaClient;

	public MagnoliaFooterCatalogAdapter(MagnoliaClient magnoliaClient) {
		this.magnoliaClient = magnoliaClient;
	}

	@Override
	public ThemeFooterDeliveryPage getFooterDelivery() {
		JsonNode root = magnoliaClient.getFooterDeliveryEnvelope();
		return MagnoliaFooterDeliveryParser.parse(root);
	}
}
