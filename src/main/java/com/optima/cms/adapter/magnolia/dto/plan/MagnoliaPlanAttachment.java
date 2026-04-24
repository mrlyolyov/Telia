package com.optima.cms.adapter.magnolia.dto.plan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MagnoliaPlanAttachment {

    private String id;
    private String name;
    private String attachmentType;
    private String content;
    private String url;
    /** Raw Magnolia payload; empty object {@code {}} is treated as absent when mapping to the API model. */
    private JsonNode validFor;
    @JsonDeserialize(using = MagnoliaExtensionRowListDeserializer.class)
    private List<MagnoliaExtensionRow> extension;

}
