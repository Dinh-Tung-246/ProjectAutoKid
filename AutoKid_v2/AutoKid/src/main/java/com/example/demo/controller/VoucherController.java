package com.example.demo.controller;


import com.example.demo.service.QuanLyVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/voucher")
public class VoucherController {

    @Autowired
    private QuanLyVoucherService service;

    @GetMapping("/index")
    public String getAllVoucher(Model model){
        model.addAttribute("voucher", service.getAll());
        return "/admin/voucher";
    }
}
