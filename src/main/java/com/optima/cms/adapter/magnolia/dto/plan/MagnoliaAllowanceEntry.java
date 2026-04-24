package com.optima.cms.adapter.magnolia.dto.plan;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MagnoliaAllowanceEntry {

    private String type;
    private String value;
    private String id;
    @JsonDeserialize(using = MagnoliaExtensionRowListDeserializer.class)
    private List<MagnoliaExtensionRow> extension;
}
