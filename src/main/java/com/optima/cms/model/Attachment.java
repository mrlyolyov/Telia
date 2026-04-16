package com.optima.cms.model;

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

    private ValidFor validFor;
    private List<Extension> extension;

}