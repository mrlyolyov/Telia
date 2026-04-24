package com.optima.cms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.port.DeviceCatalogPort;
import org.springframework.stereotype.Service;

@Service
public class DeviceCatalogService {

	private final DeviceCatalogPort deviceCatalogPort;

	public DeviceCatalogService(DeviceCatalogPort deviceCatalogPort) {
		this.deviceCatalogPort = deviceCatalogPort;
	}

	public JsonNode getDeviceCatalog(FindAllRequest request) {
		return deviceCatalogPort.getDeviceCatalog(request);
	}

}
