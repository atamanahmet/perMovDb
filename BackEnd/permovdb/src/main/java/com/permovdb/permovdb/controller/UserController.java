package com.permovdb.permovdb.controller;

import org.springframework.web.bind.annotation.RestController;

import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String getForm() {
        return new String(
                "To register: POST 127.0.0.1:8080/register, Body={\"username\":\"{yourUsername}\",\"password\":\"{yourPassword}\"}");
    }

    @PostMapping("/register")
    public ResponseEntity<String> postMethodName(@RequestBody User user) {
        try {
            userService.saveUser(user);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
