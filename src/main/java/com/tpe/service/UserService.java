package com.tpe.service;


import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.domain.enums.RoleType;
import com.tpe.dto.UserRequest;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private RoleService roleService;    // baska bir objenin DBsine ihtiyac duydugumuzda
    //serviceler servicelerle, repositoryler repositorylerle iletisime gecer

    @Autowired
    private UserRepository userRepository;   // DB ye ulasmak icin UserRepository ihtiyacimiz var

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setUserName(userRequest.getUserName());
        //passwordü şifreleyerek DB ye göndercez
        String password = userRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);//requestdeki password karmaşıklaştırıldı
        user.setPassword(encodedPassword);

        //role setlenmesi...
        //RoleService'de getRoleByType(RoleType type) olusturduk
        Set<Role> roles = new HashSet<>();
        Role role = roleService.getRoleByType(RoleType.ROLE_ADMIN);//normalde defaullte en dusuk yetki verilir!
        roles.add(role);
        user.setRoles(roles);//defaultta usera ADMIN rolünü verdik.
        userRepository.save(user);

    }
}



