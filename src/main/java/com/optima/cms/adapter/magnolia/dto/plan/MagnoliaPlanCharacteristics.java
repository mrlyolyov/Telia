package com.optima.cms.adapter.magnolia.dto.plan;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MagnoliaPlanCharacteristics {

    private List<MagnoliaAllowanceEntry> allowance;
    private List<MagnoliaNamedValue> features;
    private List<MagnoliaNamedValue> extension;

}
