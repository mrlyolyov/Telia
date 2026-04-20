package com.optima.cms.model.device;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevicePrice {

	private Integer id;
	private String type;
	private MoneyAmount amount;

}
