package com.tpe.controller;


import com.tpe.dto.UserRequest;
import com.tpe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // localhost:8080/register + POST + JSON body
    @RequestMapping("/register")    //method level kullandik burda
    @PostMapping
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest) { // userRequest->dto

        userService.saveUser(userRequest);

        return new ResponseEntity<>("User is registered succesfully", HttpStatus.CREATED);  //201
    }


}



