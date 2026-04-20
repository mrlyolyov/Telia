package com.optima.cms.model.device;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeviceVariant {

	private Long variantId;
	private String name;
	private List<DeviceAttachment> attachment;
	private DeviceCharacteristics characteristics;

}
