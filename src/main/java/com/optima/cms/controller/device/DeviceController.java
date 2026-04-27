package com.optima.cms.controller.device;

import com.optima.cms.model.DocsEnvelope;
import com.optima.cms.model.device.Device;
import com.optima.cms.model.device.DeviceFindAllResult;
import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.service.DeviceCatalogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Device catalog HTTP API (Magnolia-backed, same envelope as {@code /rest/api/plan/findall}).
 */
@RestController
@RequestMapping("/rest/api")
@Tag(name = "Device")
public class DeviceController {

	private final DeviceCatalogService deviceCatalogService;

	public DeviceController(DeviceCatalogService deviceCatalogService) {
		this.deviceCatalogService = deviceCatalogService;
	}

	/**
	 * {@code POST /rest/api/device/findall} — device catalog from Magnolia delivery {@code device/v1}.
	 */
	@PostMapping("/device/findall")
	public DocsEnvelope<Device> findAll(@RequestBody FindAllRequest request) {
		DeviceFindAllResult result = deviceCatalogService.listDevices(request);
		return DocsEnvelope.of(result.devices(), result.warning());
	}

	/**
	 * {@code GET /rest/api/device/by-external-id?externalId=…} — same {@link DocsEnvelope} as findall, scoped to one id.
	 */
	@GetMapping("/device/by-external-id")
	public DocsEnvelope<Device> getByExternalId(@RequestParam("externalId") String externalId) {
		if (externalId == null || externalId.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "externalId is required");
		}
		DeviceFindAllResult result = deviceCatalogService.findDevicesByExternalId(externalId);
		return DocsEnvelope.of(result.devices(), result.warning());
	}
}
