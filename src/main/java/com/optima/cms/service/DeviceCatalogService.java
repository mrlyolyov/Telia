package com.optima.cms.service;

import com.optima.cms.model.device.DeviceFindAllResult;
import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.port.DeviceCatalogPort;
import org.springframework.stereotype.Service;

@Service
public class DeviceCatalogService {

	private final DeviceCatalogPort deviceCatalogPort;

	public DeviceCatalogService(DeviceCatalogPort deviceCatalogPort) {
		this.deviceCatalogPort = deviceCatalogPort;
	}

	public DeviceFindAllResult listDevices(FindAllRequest request) {
		return deviceCatalogPort.listDevices(request);
	}

	public DeviceFindAllResult findDevicesByExternalId(String externalId) {
		return deviceCatalogPort.findDevicesByExternalId(externalId);
	}
}
