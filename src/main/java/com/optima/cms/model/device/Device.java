package com.optima.cms.model.device;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Device {

	private String externalId;
	private String name;
	private String description;

	private List<DeviceAttachment> attachment;
	private List<DevicePrice> price;
	private DeviceCharacteristics characteristics;
	private List<DeviceVariant> variants;

}
