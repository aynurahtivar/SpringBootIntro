package com.tpe.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_user") //user keyword olarak da kullanildigi icin ismini degistirmek gerekiyr
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25, nullable = false)
    private String firstName;

    @Column(length = 25, nullable = false)
    private String lastName;

    @Column(length = 25, nullable = false, unique = true)
    private String userName;


    @Column(length = 255, nullable = false)//password DB ye kaydedilmeden önce şifreleneceği için
    private String password;//karmaşık ve uzun olacak length=255 aldik

    //role
    @ManyToMany(fetch = FetchType.EAGER) //userin brden fazla rolu olabilir, bir rolun birden fazla useri olabilir
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), //iliskinin takip edilecegi tablo
            inverseJoinColumns = @JoinColumn(name = "role_id"))//tek yonlu iliskide opsiyonel  @JoinTable
    private Set<Role> roles = new HashSet<>();  //icinde roller olan tekrarrsiz rol set


}
