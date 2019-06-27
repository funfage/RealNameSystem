package com.real.name.login.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.login.entity.Admin;
import com.real.name.login.service.AdminService;
import com.real.name.login.service.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminImp implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin create(String username, String password) {

        Optional<Admin> u = adminRepository.findByUsername(username);
        if (u.isPresent()) {
            throw new AttendanceException(ResultError.PERSON_EXIST);
        }

        Admin admin = new Admin(username, password);
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> find(String username, String password) {
        return adminRepository.findByUsernameAndPassword(username, password);
    }
}
