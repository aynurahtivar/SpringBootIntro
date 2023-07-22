package com.tpe.controller;


import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController     //rest servisler karsilanacak
@RequestMapping("/students")    //hangi requestler karsilanacak
//end pointler, uri lar karsilanacak..   http://localost:8080/students

public class StudentController {

    Logger logger = LoggerFactory.getLogger(StudentController.class);


    @Autowired
    private StudentService studentService;

    //spring bootu selamlayalim
    //http://localhost:8080/students/greet+GET


    @GetMapping("/greet")
    public String greet() {
        return "Hello Spring Boot :) ";
    }


    // 	1-> Tum studentlari listeleyelim. READ
    //  http://localhost:8080/students  + GET

    @PreAuthorize("hasRole('ADMIN')")   //sadece ADMIN bu istegi yapabilir
    //hasRole'e ozel ROLE_ADMIN icin _dan sonrasini aliyor
    @GetMapping
    public ResponseEntity<List<Student>> listAllStudents() {
        //ResponseEntity: response bodysi ile birlikte http status codeunu gondermemizi saglar, generic bir class

        List<Student> studentList = studentService.getAllStudent();

        // islemin basarili oldugunu gostermek icin
        //  return new ResponseEntity<>(studentList, HttpStatus.OK);    //200
        return ResponseEntity.ok(studentList);  // yukaridaki satirla ayni islemi yapiyor
    }
    //  requestte: Url+ http method
    // responseda : response bodysi ile birlikte  http status codenu gondermemizi saglar
    //  ResponseEntity.ok() methodu HTTP status olarak OK ya da 200 donmek icin bir kisayoldur


    //  3-> Yeni bir student CREATE etme
    //  http://localhost:8080/students  + POST + RequestBody(JSON)
    // ayni end pointle birden fazla request olusturabiliyoruz ( end point listeleme ile ayni! )

    @PostMapping
    public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody Student student) {

        studentService.saveStudent(student);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Student is created successfully");
        response.put("status", "success");

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    // @RequestBody : HTTP requestin bodysindeki JSON formatindaki bilgiyi student objesine mapler.
    //  Entity obje <-> JSON formatina maplemeyi Jackson kutuphanesi yapiyor


    //  5-->1 Belirli bir id ile studenti goruntuleyelim
    //  http://localhost:8080/students/query?id=1 + GET + (@RequestParam)
    //  ?id=1 buradaki degisken ile @RequestParam("id") ayni olmalidir  (1 tane degisken oldugunda opsiyonel)
    @GetMapping("/query")
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id) {
        Student student = studentService.getStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    //  5-->2 Belirli bir id ile studenti goruntuleyelim
    //  http://localhost:8080/students/3 + GET + (@PathVariable )
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") Long id) {
        Student student = studentService.getStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    // istemciden-clienttan bilgi almak icin 3 ane yontem mumkun
    //  - JSON formatinda request body
    //  - request param query?id=1
    //  - path param /1

    //  --NOT:Birden fazla parametre girilecekse @RequestParam, 1 tane parametre varsa @PathVariable
    // objenin tum fieldlari alinacaksa JSON formatinda @RequestBody


    // 7 --> idsi verilen bir studenti silelim  + Path Param
    //  http://localhost:8080/students/1 + DELETE + (@PathVariable )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Student is deleted succesfullly...");
        response.put("status", "success..");

        //return new ResponseEntity<>(response, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }


    // 9 --> UPDATE islemi
    //  belirli bir id ile student i update edelim
    //  studentin name,lastname, grade, email, phoneNumber i update edilebilir, id ve tarih edilmez.
    //  http://localhost:8080/students/1 + PUT+ JSON
    // put patch farki-> patch kismi, put tamamini gunceller
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateStudent(@PathVariable("id") Long id,
                                                             @Valid @RequestBody StudentDTO studentDTO) {
        studentService.updateStudent(id, studentDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Student is updated succesfullly...");
        response.put("status", "success..");
        return ResponseEntity.ok(response);
    }


    //  11--- pagination-sayfalandirma
    // tum kayitlari page page listeleyelim
    //  http://localhost:8080/students/page?page=1&size=10&sort=name&direction=DESC + GET
    @GetMapping("/page")
    public ResponseEntity<Page<Student>> getAllStudentByPage(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,    //hangi page gosterilsin defaut 0
            @RequestParam(value = "size", required = false, defaultValue = "2") int size,    //kac kayit olsun
            @RequestParam("sort") String prop, //hangi fielda gore
            @RequestParam("direction") Sort.Direction direction) { //siralama yonu

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));   //standart code, clienttan alinan bilgiler girildi
        Page<Student> studentsByPage = studentService.getAllStudentPaging(pageable);

        return new ResponseEntity<>(studentsByPage, HttpStatus.OK);
    }
    //page,size,sort,direction parametrelerinin girilmesini opsiyonel yapabiliriz. (required = false)
    // parametrelerin girilmesi opsiyonel oldugunda default value vermeliyiz.   (defaultValue = "2")


    // 13-- lastName ile tum studentlari listeleyelim
    /// http://localhost:8080/students/querylastname?lastName=Bey
    @GetMapping("/querylastname")
    public ResponseEntity<List<Student>> getAllStudentByLastName(@RequestParam("lastName") String lastName) {
        List<Student> studentList = studentService.getAllStudentByLastName(lastName);
        return ResponseEntity.ok(studentList);
    }

    //  15------
    //  ****** ODEV  ******
    // grade ile studentlari listeleyelim
    // http://localhost:8080/students/grade/99
    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<Student>> getAllStudentByGrade(@PathVariable("grade") Integer grade) {
        List<Student> studentList = studentService.getAllStudentByGrade(grade);
        return ResponseEntity.ok(studentList);
    }


    //  17---
    //  id'si verilen student'in goruntuleme istegini response olarak DTO donelim
    //  http://localhost:8080/students/dto/1  + GET

    @GetMapping("/dto/{id}")
    public ResponseEntity<StudentDTO> getStudentDtoById(@PathVariable("id") Long id) {
        StudentDTO studentDTO = studentService.getStudentDtoById(id);

        logger.warn("----------> Serviceden StudentDTO objesi alindi : " + studentDTO.getName());

        return ResponseEntity.ok(studentDTO);
    }

    //  19---
    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {

        logger.info("Welcome mesaji {} ", request.getServletPath());

        return "Welcome Aynur :) ";
    }


}


































































