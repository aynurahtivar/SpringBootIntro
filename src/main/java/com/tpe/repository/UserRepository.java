package com.tpe.repository;


import com.tpe.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    //kullanici bir username ve password ile request gonderdiginde dbden useri bulmak icin
    // findById'yi findByUserName olarak turettik
    Optional<User> findByUserName(String username) throws UsernameNotFoundException;
    //                                   bu method cagrilirsa exception firlat, handle edilsin


}
