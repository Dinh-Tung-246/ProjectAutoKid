package com.example.demo.controller;

import com.example.demo.model.GioHangChiTiet;
import com.example.demo.repository.LoaiSanPhamRepo;
import com.example.demo.response.GioHangChiTietResponse;
import com.example.demo.service.QuanLyGioHangService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/autokid/shoping-cart")
public class AutokidShoppingCartController {
    private static Logger logger = LoggerFactory.getLogger(AutokidShoppingCartController.class);

    @Autowired
    LoaiSanPhamRepo loaiSanPhamRepo;

    @Autowired
    QuanLyGioHangService qlghService;

    @GetMapping("")
    public String showShoppingCart(Model model){
        model.addAttribute("loaisp", loaiSanPhamRepo.findAll());
        model.addAttribute("currentPage","shoping-cart");
        return "/autokid/shoping-cart";
    }

    @PostMapping("/get-cart-from-db")
    @ResponseBody
    public List<GioHangChiTietResponse> getCartFromDB(@RequestBody Map<String, Object> idKH) {
        Integer idKHInt = Integer.parseInt(idKH.get("idKH").toString());
        List<GioHangChiTietResponse> list = qlghService.getGioHang(idKHInt);
        return list;
    }
}
