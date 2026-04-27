package com.optima.cms.service;

import com.optima.cms.model.theme.ThemeHeaderDeliveryPage;
import com.optima.cms.port.HeaderCatalogPort;
import org.springframework.stereotype.Service;

@Service
public class HeaderCatalogService {

	private final HeaderCatalogPort headerCatalogPort;

	public HeaderCatalogService(HeaderCatalogPort headerCatalogPort) {
		this.headerCatalogPort = headerCatalogPort;
	}

	public ThemeHeaderDeliveryPage getHeaderDelivery() {
		return headerCatalogPort.getHeaderDelivery();
	}
}
