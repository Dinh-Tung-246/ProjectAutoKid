package com.example.demo.controller;

import com.example.demo.repository.LoaiSanPhamRepo;
import com.example.demo.service.QuanLyGioHangService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/autokid/shoping-cart")
public class AutokidShoppingCartController {
    @Autowired
    LoaiSanPhamRepo loaiSanPhamRepo;

    @Autowired
    QuanLyGioHangService qlghService;

    @GetMapping("")
    public String showShoppingCart(@RequestParam(required = false) Integer idKH, Model model){
        if (idKH != null) {
            model.addAttribute("cartItems", qlghService.getGioHang(idKH));
        }
        model.addAttribute("loaisp", loaiSanPhamRepo.findAll());
        model.addAttribute("currentPage","shoping-cart");
        return "/autokid/shoping-cart";
    }
}
