package com.optima.cms.model.plan;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Attachment {

    private String id;
    private String name;
    private String attachmentType;
    private String content;
    /** Present for {@code attachmentType} such as {@code picture}. */
    private String url;

    /** Magnolia interval or validity metadata; omitted from JSON when null. */
    private JsonNode validFor;
    private List<Extension> extension;

}