package com.optima.cms.port;

import com.optima.cms.model.theme.ThemeDeliveryPage;

/**
 * Magnolia-backed theme body delivery (palette, logos, token backgrounds) for {@link com.optima.cms.model.theme.ThemeOverride}.
 */
public interface ThemeCatalogPort {

	ThemeDeliveryPage getThemeDelivery();
}
