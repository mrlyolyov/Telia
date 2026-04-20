package com.optima.cms.adapter.magnolia.dto.plan;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MagnoliaAllowanceEntry {

    private String type;
    private String value;
    private String id;
    private List<MagnoliaExtensionRow> extension;
}
