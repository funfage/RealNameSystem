package com.real.name.login.service;

import com.real.name.login.entity.Admin;

import java.util.Optional;

public interface AdminService {

    Admin create(String username, String password);

    Optional<Admin> find(String username, String password);
}
