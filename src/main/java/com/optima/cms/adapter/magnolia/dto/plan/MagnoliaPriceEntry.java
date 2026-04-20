package com.optima.cms.adapter.magnolia.dto.plan;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MagnoliaPriceEntry {

    private String id;
    private String type;
    private MagnoliaMoneyAmount amount;

}
