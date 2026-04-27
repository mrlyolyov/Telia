package com.optima.cms.service;

import com.optima.cms.model.theme.ThemeFooterDeliveryPage;
import com.optima.cms.port.FooterCatalogPort;
import org.springframework.stereotype.Service;

@Service
public class FooterCatalogService {

	private final FooterCatalogPort footerCatalogPort;

	public FooterCatalogService(FooterCatalogPort footerCatalogPort) {
		this.footerCatalogPort = footerCatalogPort;
	}

	public ThemeFooterDeliveryPage getFooterDelivery() {
		return footerCatalogPort.getFooterDelivery();
	}
}
