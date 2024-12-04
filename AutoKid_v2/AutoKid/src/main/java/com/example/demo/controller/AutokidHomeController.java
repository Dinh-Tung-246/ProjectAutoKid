package com.example.demo.controller;

import com.example.demo.repository.LoaiSanPhamRepo;
import com.example.demo.service.QuanLyGioHangService;
import com.example.demo.service.QuanLySanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/autokid/home")
public class AutokidHomeController {
    @Autowired
    QuanLySanPhamService quanLySanPhamService;

    @Autowired
    LoaiSanPhamRepo loaiSanPhamRepo;

    @Autowired
    QuanLyGioHangService qlghService;

    @GetMapping("")
    public String home(@RequestParam(required = false) Integer idKH, Model model){
        model.addAttribute("currentPage","home");
        if (idKH != null){
            model.addAttribute("countCart", qlghService.getSoLuongSPCTInCart(idKH));
            model.addAttribute("totalPrice", qlghService.getTotalPrice(idKH));
        }
        model.addAttribute("sanpham", quanLySanPhamService.getAllSP());
        model.addAttribute("loaisp", loaiSanPhamRepo.findAll());
        return "/autokid/index";
    }
}
