package com.example.demo.controller;


import com.example.demo.model.Voucher;
import com.example.demo.service.QuanLyVoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/voucher")
public class VoucherController {

    @Autowired
    private QuanLyVoucherService service;

    @GetMapping("/index")
    public String getAllVoucher(Model model){
        model.addAttribute("voucherAdd", new Voucher());
        model.addAttribute("voucher", service.getAll());
        return "/admin/voucher";
    }

    @PostMapping ("/save")
    public String saveVoucher(@Valid @ModelAttribute("voucherAdd") Voucher voucher,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Thông tin không hợp lệ!");
            return "/admin/voucher";
        }
        service.saveVoucher(voucher);
        return "redirect:/admin/voucher/index";
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
