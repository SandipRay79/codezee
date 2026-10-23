package com.employee_management.ems.Service.user;

import com.employee_management.ems.Entity.Role;
import com.employee_management.ems.Repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository=roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
