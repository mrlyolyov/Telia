package com.optima.cms.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Allowance {

    private String type;
    private String value;
    private List<Object> extension;
    private String id;

    public static Allowance createAllowance(String type, String value, String id) {
        Allowance a = new Allowance();
        a.setType(type);
        a.setValue(value);
        a.setId(id);
        a.setExtension(List.of());
        return a;
    }
}