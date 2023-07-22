package com.tpe.service;


import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourcesNotFoundException;
import com.tpe.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jca.cci.object.SimpleRecordOperation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {


    @Autowired
    private StudentRepository studentRepository;


    //  2--
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
//        List<Student> students = studentRepository.findAll();
//        // repomuzda findAll yok fakat JPA sayesinde hazir methodu kullanabiliyoruz
//        // olmasaydi query yazardik-> select * from student;
//        return students;
    }


    //  4--
    public void saveStudent(Student student) {
        // unique islemler icin genellikle karsilastirmalar email ile yapilr
        // student daha once kaydedilmis mi? --> ayni emaile sahip student var mi?

        if (studentRepository.existsByEmail(student.getEmail())) { // existsByEmail->existsById gibi calisiyor
            throw new ConflictException("Email is already exists!");
        }
        studentRepository.save(student);
    }


    // 6 ----
    public Student getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Student not found by id : " + id));
        return student;
    }

    //  8----
    public void deleteStudent(Long id) {
        // id ye sahip student var mi emin olmak icin;
        Student foundStudent = getStudentById(id);
        studentRepository.delete(foundStudent);
    }


    /// 10---
    public void updateStudent(Long id, StudentDTO studentDTO) {
        //  gelen id ile ogrenci var mi? varsa getirelim
        Student foundStudent = getStudentById(id);

        //studentDTO.getEmail()  daha onceden DB'de varsa?
        Boolean existsEmail = studentRepository.existsByEmail(studentDTO.getEmail());

        //existsEmail true ise bu email başka bir studentın olabilir, studentın kendi emaili olabilir??
        // id:3 student email:a@email.com
        //dto                student
        //b@email.com        id:4 b@email.com->existsEmail:true--->exception -senaryo 1
        //c@email.com        DB de yok-------->existsEmail:false--update:OK -senaryo 2
        //a@email.com        id:3 a@email.com ->existsEmail:true--update:OK -senaryo 3

        if (existsEmail && !foundStudent.getEmail().equals(studentDTO.getEmail())) {
            throw new ConflictException("Email already exists!!!");
        }
        foundStudent.setName(studentDTO.getName());
        foundStudent.setLastName(studentDTO.getLastName());
        foundStudent.setGrade(studentDTO.getGrade());
        foundStudent.setPhoneNumber(studentDTO.getPhoneNumber());
        foundStudent.setEmail(studentDTO.getEmail()); //email zaten daha onceden varsa ne olacak?

        studentRepository.save(foundStudent);   //saveOrUpdate gibi islem yapar.
    }


    // 12---
    public Page<Student> getAllStudentPaging(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }


    //  14-----
    public List<Student> getAllStudentByLastName(String lastName) {
        return studentRepository.findAllByLastName(lastName);  //select * from student where lastName=' '
        //findAllById()'yi turettik, findAllBy....
    }


    //  16---
    public List<Student> getAllStudentByGrade(Integer grade) {
        //  return studentRepository.findAllByGrade(grade);   // hazir method yokmus varsayalim
        return studentRepository.findAllGradeEquals(grade);

    }


    //  18----

//    public StudentDTO getStudentDtoById(Long id) {
//        Student student = getStudentById(id);
//
//        // dto serviste repoya gitmiyor!!!!
//        //parametre olarak student objesinin kendisini verdigimizde bize DTO olusturan cons olsaydi?
//        StudentDTO studentDTO=new StudentDTO(student);
//        return studentDTO;
//    }


    // studenti dto ya mapleme islemini JPQL ile yapalim
    public StudentDTO getStudentDtoById(Long id) {
        StudentDTO studentDTO = studentRepository.findStudentDtoById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Student not found by id : " + id));
        return studentDTO;
    }


}
