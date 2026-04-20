package com.optima.cms.adapter.magnolia.dto.plan;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MagnoliaPlanAttachment {

    private String id;
    private String name;
    private String attachmentType;
    private String content;
    private String url;
    /** Raw Magnolia payload; empty object {@code {}} is treated as absent when mapping to the API model. */
    private JsonNode validFor;
    private List<MagnoliaExtensionRow> extension;

}
