package com.optima.cms.adapter.magnolia.dto.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnoliaDevice {

	private String name;
	private String description;
	private String displayName;
	private String externalId;
	private List<MagnoliaDeviceAttachment> attachment;
	private JsonNode characteristics;
}
