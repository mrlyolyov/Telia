package com.optima.cms.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Characteristics {

    private List<Allowance> allowance;
    private List<Object> features;
    private List<Object> extension;

}