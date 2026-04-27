package com.optima.cms.port;

import com.optima.cms.model.theme.ThemeFooterDeliveryPage;

/**
 * Magnolia-backed footer delivery for theme / layout.
 */
public interface FooterCatalogPort {

	ThemeFooterDeliveryPage getFooterDelivery();
}
