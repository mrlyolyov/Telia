package com.optima.cms.controller.device;

import com.fasterxml.jackson.databind.JsonNode;
import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.service.DeviceCatalogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Device catalog and related HTTP API (expand with CRUD as needed).
 */
@RestController
@RequestMapping("/rest/api")
public class DeviceController {

	private final DeviceCatalogService deviceCatalogService;

	public DeviceController(DeviceCatalogService deviceCatalogService) {
		this.deviceCatalogService = deviceCatalogService;
	}

	/**
	 * {@code POST /rest/api/device/findall} — mock device catalog until Magnolia delivery is wired.
	 * Response matches Magnolia-style envelope: {@code docs}, pagination, optional {@code warning}.
	 */
	@PostMapping("/device/findall")
	public JsonNode findAll(@RequestBody FindAllRequest request) {
		return deviceCatalogService.getDeviceCatalog(request);
	}
}
