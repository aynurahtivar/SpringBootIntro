package com.tpe.security.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //amaç: UserDetails-->User   GrantedAuthorities-->Role
    // kendi yapilarimizla securitye taniticaz

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // optional objesi dondugunde exc firlattik,User olarak aldik
        User user = userRepository.findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found by username: " + username));

        return new org.springframework.security.core.userdetails.   //bizim user degil, springsecuritydeki user
                //kendi userimizi ayni isimle olusturup import ettigimiz icin bu sekilde yazdik,
                // isimlendirmeyi farkli yapip direkt import edebilirdik uzun uzun yolunu yazmak yerine
                User(user.getUserName(), user.getPassword(), buildGrantedAuthorities(user.getRoles()));
        //            buldugumuz userin name, password ve rolunu verdik
        //              GrantedAuthorities   tipinde rol icin ayri method yaptik buildGrantedAuthorities
    }


    //           GrantedAuthorities'yi implemente eden SimpleGrantedAuthority geri donsun
    private List<SimpleGrantedAuthority> buildGrantedAuthorities(Set<Role> roles) {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            // kendi rollerimizi tek tek SimpleGrantedAuthority'ye ceviriyoruz
            authorities.add(new SimpleGrantedAuthority(role.getType().name())); // rol tipinin ismini aldik
            //                  " String tipinde rol ismi aliyor.
        }
        //rollerin isimlerini parametre olarak SimpleGrantedAuthority nin const verdiğimizde
        //yeni SimpleGrantedAuthority oluştururuz ve bu SimpleGrantedAuthority listeye ekleriz.
        return authorities;
    }


}
