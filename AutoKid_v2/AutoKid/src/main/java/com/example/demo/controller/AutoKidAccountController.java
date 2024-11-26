package com.example.demo.controller;

import com.example.demo.repository.LoaiSanPhamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/autokid/account")
public class AutoKidAccountController {
    @Autowired
    LoaiSanPhamRepo loaiSanPhamRepo;

    @GetMapping("")
    public String get(Model model){
        model.addAttribute("loaisp", loaiSanPhamRepo.findAll());
        return "/autokid/updateAccount";
    }
}
