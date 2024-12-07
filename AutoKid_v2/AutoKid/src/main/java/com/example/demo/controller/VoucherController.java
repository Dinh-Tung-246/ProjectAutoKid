package com.example.demo.controller;


import com.example.demo.model.Voucher;
import com.example.demo.service.QuanLyVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @PostMapping("/aplly")
    public String applyVoucher(@RequestParam("ma") String ma,
                               @RequestParam("tongHoaDon") double tongHoaDon,
                               Model model) {
        model.addAttribute("voucher", service.getAll());
        Voucher voucher = service.findCode(ma);

        try {
            double discount = service.applyVoucher(voucher, tongHoaDon);
            model.addAttribute("discount", discount);
            model.addAttribute("finalTotal", tongHoaDon - discount);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "/admin/voucher";
    }
}
