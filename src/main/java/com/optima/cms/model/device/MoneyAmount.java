package com.optima.cms.model.device;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MoneyAmount {

	private String currency;
	private BigDecimal value;

}
