package com.tpe.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter //Lombok
@Setter     //class veya field seviyesinde kullanilir
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor    // final veya notnull olarak belirlenen fieldlardan cons olusturur
// entity classlarinda cok kullanilmaz

@Entity //derlenmis kodlar arasinda gorunmez,
// bilgilendirme sagliyor, logic olarak etkilemez, okunur yorumlanir
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Setter(AccessLevel.NONE)   //id nin setter methodu olmasin
    private Long id;


    @NotBlank(message = "name can not be space")
    @Size(min = 2, max = 25, message = "name '${validatedValue}' must be between {min} and {max}")
    @Column(nullable = false, length = 25)
    //   @NotBlank @Size Validationdan gelen anatasyonlar   , frontend,client tarafinda islem gerceklesir
    //  uygulama icerisine gelmeden validasyon saglanmis oluyor, clientten bilgiler alinirken
    //  @Column Hibernate den gelen anatasyon   , backend, dbde islem gerceklesir, kaydetme isleminde
    //  daha guvenli olmasi icin her iki tarafta da dogrulama yapilmali
    /*final*/ private String name;

    @NotBlank(message = "lastname can not be space")
    @Size(min = 2, max = 25, message = "name '${validatedValue}' must be between {min} and {max}")
    @Column(nullable = false, length = 25)
    /*final*/ private String lastName;

    /*final*/ private Integer grade;


    @Column(nullable = false, unique = true, length = 50)
    @Email(message = "Please, provide valid email")
    //email degiskeni iceridinde aa@bb.com gibi formatta olmasini sagliyor, icinde @ var mi
    /*final*/ private String email;

    private String phoneNumber;

    private LocalDateTime createDate = LocalDateTime.now();

    @OneToMany(mappedBy = "student")
    private List<Book> books = new ArrayList<>();


    @OneToOne   // 1 studentin 1 tane kullanici hesabi(user) olmali, 1 kullanici hesabinin da 1 tane sahibi olmali
    private User user;


    //getter-setter
    // parametreli/ parametresiz cons


}
