package com.example.demo.controller;


import com.example.demo.model.Voucher;
import com.example.demo.response.VoucherResponse;
import com.example.demo.service.QuanLyDatHangService;
import com.example.demo.service.QuanLyVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/voucher")
public class VoucherController {

    @Autowired
    private QuanLyVoucherService service;

    @Autowired
    QuanLyDatHangService serviceQLDH;

    @GetMapping("/index")
    public String getAllVoucher(Model model){
        List<Voucher> vouchers = service.getAll();
        model.addAttribute("vouchers", vouchers.stream().map(VoucherResponse::new).collect(Collectors.toList()));
        model.addAttribute("voucherAdd", new Voucher());
        model.addAttribute("updateVoucher", new Voucher());
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("voucher", service.getAll());
        model.addAttribute("namePage","voucher");
        return "/admin/voucher";
    }

    @PostMapping ("/save")
    public String saveVoucher(@ModelAttribute("voucherAdd") Voucher voucher) {
        service.saveVoucher(voucher);
        return "redirect:/admin/voucher/index";
    }

    @PostMapping("/update")
    public String updateVoucher(@ModelAttribute("updateVoucher") Voucher voucher) {
        service.updateVoucher(voucher);
        return "redirect:/admin/voucher/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable Integer id){
        service.deleteVoucher(id);
        return "redirect:/admin/voucher/index";
    }

    @PostMapping("/aplly")
    public String applyVoucher(@RequestParam("ma") String ma,
                               @RequestParam("tongHoaDon") double tongHoaDon,
                               Model model) {
        model.addAttribute("voucher", service.getAll());
        Voucher voucher = service.findCode(ma);
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());

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
