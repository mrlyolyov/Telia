package com.optima.cms.model.plan;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Plan {

    private String id;
    private String createdAt;
    private String updatedAt;
    private String externalId;
    private String name;
    private String description;

    private List<Attachment> attachment;
    private List<Object> price;

    private Characteristics characteristics;
    private List<Object> extension;

}