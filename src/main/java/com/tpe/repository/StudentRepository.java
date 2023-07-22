package com.tpe.repository;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository //opsiyonel
//jpa den extend ettigimiz icin gerek yok, okunabilirlik acisindan kalsin

public interface StudentRepository extends JpaRepository<Student, Long> {
    //  JpaRepository<Student, Long>
    //  JpaRepository<Entity Class, Id data tipi>


    //existsById yerine
    Boolean existsByEmail(String email);// bu emaile sahip kayit varsa true, yoksa false donecek

    //jpa de hazir olan methodlar var, cok sik kullanilan methodlar icin interfacede ornekler olusturulmustur,
    //  bu methodlari turetebiliyoruz,  existsBy...... baslangici-koku ayni olmali
    //  findByName, findByEmail, findByLastName....
    //otomatik olarak spring tarafindan uygulaniyor, dogrudan kullanabiliyoruz

    //findAllById yerine
    List<Student> findAllByLastName(String lastName);

    List<Student> findAllByGrade(Integer grade);

    //  JPQL-Java Persistent Query Lang.
    //kendimiz method yaptik query ile alcaz
    // @Query("FROM Student s WHERE s.grade=:pGrade")   --> dinamik olmasi icin degisken kullandik pGrade
    @Query("SELECT s FROM Student s WHERE s.grade=:pGrade")
    List<Student> findAllGradeEquals(@Param("pGrade") Integer grade);
    //SQL
//    @Query(value = "SELECT * FROM student s WHERE s.grade=:pGrade", nativeQuery = true) // tablo adi buyuk harfle de olsa kabul ediyor
//    List<Student> findAllGradeEquals(@Param("pGrade") Integer grade); // @Param "pGrade" i Integer grade'e koyar.



    //DB den gelen student'i DTO ya cevirerek-mapleyerek gonderiyor
    @Query("SELECT new com.tpe.dto.StudentDTO(s) FROM Student s WHERE s.id=:pId")
    Optional<StudentDTO> findStudentDtoById(@Param("pId") Long id);



}
