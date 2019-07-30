package com.real.name.auth.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Permission implements Serializable {

    private static final long serialVersionUID = 6728287426900239946L;

    private Integer permissionId;

    private String url;

    private String name;

    private String description;


}













