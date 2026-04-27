package com.optima.cms.model.device;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Device catalog entry aligned with Magnolia delivery {@code device/v1} shape.
 */
@Getter
@Setter
public class Device {

	private String name;
	private String description;
	private String displayName;
	private String externalId;
	private List<DeviceAttachment> attachment;
	/** CMS-specific facets (e.g. color, storage, features); structure mirrors Magnolia. */
	private JsonNode characteristics;
}
