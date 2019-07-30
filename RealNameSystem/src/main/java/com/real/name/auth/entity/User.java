package com.real.name.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 4481891139566008838L;

    private Integer userId;

    private String username;

    private String phone;

    private String password;

    private Integer status;

    @JsonIgnore
    private Integer roleId;

    @JsonIgnore
    private String authCode;

    @JsonIgnore
    private String passwordAgain;

    @JsonIgnore
    private Set<String> projectSet;

    /**
     * 一个用户具有多个角色
     */
    private Set<Role> roles;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }
}














