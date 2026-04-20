package com.optima.cms.model.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceFeature {

	private String key;

	@JsonProperty("Value")
	private String value;

}
