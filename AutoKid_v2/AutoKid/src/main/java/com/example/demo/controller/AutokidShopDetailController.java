package com.example.demo.controller;

import com.example.demo.repository.LoaiSanPhamRepo;
import com.example.demo.repository.SanPhamChiTietRepo;
import com.example.demo.service.QuanLySanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/autokid/shop-detail")
public class AutokidShopDetailController {
    @Autowired
    LoaiSanPhamRepo lspRepo;

    @Autowired
    QuanLySanPhamService quanLySanPhamService;

    @GetMapping("")
    public String showSPCT(@RequestParam Integer idSP, Model model){
        model.addAttribute("sp", quanLySanPhamService.getById(idSP));
        model.addAttribute("loaisp", lspRepo.findAll());
        model.addAttribute("sanpham", quanLySanPhamService.getAllSP());
        model.addAttribute("related",quanLySanPhamService.getAllRelatedProduct(idSP));
        return "/autokid/shop-details";
    }
}
