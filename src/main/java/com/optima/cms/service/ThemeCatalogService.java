package com.optima.cms.service;

import com.optima.cms.model.theme.ThemeDeliveryPage;
import com.optima.cms.port.ThemeCatalogPort;
import org.springframework.stereotype.Service;

@Service
public class ThemeCatalogService {

	private final ThemeCatalogPort themeCatalogPort;

	public ThemeCatalogService(ThemeCatalogPort themeCatalogPort) {
		this.themeCatalogPort = themeCatalogPort;
	}

	public ThemeDeliveryPage getThemeDelivery() {
		return themeCatalogPort.getThemeDelivery();
	}
}
