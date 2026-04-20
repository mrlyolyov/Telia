package com.optima.cms.model.device;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeviceCharacteristics {

	private List<DeviceCharacteristicValue> color;
	private List<DeviceCharacteristicValue> storage;
	private List<DeviceFeature> features;

}
