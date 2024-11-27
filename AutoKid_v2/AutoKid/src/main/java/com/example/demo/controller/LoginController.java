package com.example.demo.controller;

import com.example.demo.model.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class LoginController {

    @GetMapping("admin/login")
    public  String login(){
        return ("/admin/login");
    }
}
