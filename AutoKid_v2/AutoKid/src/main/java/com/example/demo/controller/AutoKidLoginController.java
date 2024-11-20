package com.example.demo.controller;


import com.example.demo.service.QuanLyKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/autokid/login")
public class AutoKidLoginController {

    @Autowired
    QuanLyKhachHangService qlkhService;

    @GetMapping("/")
    public String showLogin(){
        return "/autokid/login-form";
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> loginAccount(@RequestParam String email,
                                            @RequestParam String password){
        Map result = qlkhService.checkLogin(email, password);
        return result;
    }

    @PostMapping("/register")
    @ResponseBody
    public String createAccount(@RequestParam String tenKH,
                                @RequestParam String emailKH,
                                @RequestParam String matKhau,
                                @RequestParam String confirmPass){
        String result = qlkhService.registerAccount(tenKH, emailKH, matKhau, confirmPass);
        return  result;
    }
}
