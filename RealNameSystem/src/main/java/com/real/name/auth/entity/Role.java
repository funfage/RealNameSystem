package com.real.name.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Role implements Serializable {

    private static final long serialVersionUID = 8502159860556353357L;

    private Integer roleId;

    private String roleName;

    private String description;

    @JsonIgnore
    private String permissionIds;

    @JsonIgnore
    private List<Permission> permissions;

}



















