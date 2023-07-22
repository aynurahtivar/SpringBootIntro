package com.tpe.service;

import com.tpe.domain.Role;
import com.tpe.domain.enums.RoleType;
import com.tpe.exception.ResourcesNotFoundException;
import com.tpe.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;      // DB ye ulasmak icin RolRepositorye ihtiyacimiz var

    public Role getRoleByType(RoleType type) {
        // repository'de findByType(type) methodu olusturduk
        Role role = roleRepository.findByType(type)
                .orElseThrow(() -> new ResourcesNotFoundException("Role is not found"));// geriye optional dondugu icin handle ettik

        return role;
    }


}