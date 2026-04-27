package com.optima.cms.port;

import com.optima.cms.model.theme.ThemeHeaderDeliveryPage;

/**
 * Magnolia-backed header delivery (nav tabs) for theme / layout.
 */
public interface HeaderCatalogPort {

	ThemeHeaderDeliveryPage getHeaderDelivery();
}
