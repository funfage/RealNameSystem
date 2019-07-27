package com.real.name.auth.service;

import com.real.name.auth.entity.Admin;

import java.util.Optional;

public interface AdminService {

    Admin create(String username, String password);

    Optional<Admin> find(String username, String password);
}
