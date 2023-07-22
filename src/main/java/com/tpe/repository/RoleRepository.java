package com.tpe.repository;

import com.tpe.domain.Role;
import com.tpe.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {




    // type ina bakarak bakarak role bulmak isteyebiliriz
    // findById'yi findByType olarak turettik
    Optional<Role> findByType(RoleType type);
}
