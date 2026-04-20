package com.optima.cms.service;

import com.optima.cms.model.device.Device;
import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.port.DeviceCatalogPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceCatalogService {

	private final DeviceCatalogPort deviceCatalogPort;

	public DeviceCatalogService(DeviceCatalogPort deviceCatalogPort) {
		this.deviceCatalogPort = deviceCatalogPort;
	}

	public List<Device> listDevices(FindAllRequest request) {
		return deviceCatalogPort.listDevices(request);
	}

}
