package com.example.demo.controller;

import com.example.demo.model.KhachHang;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.response.KhachHangResponse;
import com.example.demo.service.QuanLyDatHangService;
import com.example.demo.service.QuanLyKhachHangService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/customer-management")
public class CustomerManagementController {
    private static Logger logger = LoggerFactory.getLogger(CustomerManagementController.class);

    @Autowired
    QuanLyKhachHangService serviceQLKH;

    @Autowired
    KhachHangRepo khachHangRepo;

    @Autowired
    QuanLyDatHangService serviceQLDH;

    @GetMapping("/")
    public String getKhachHangPage(Model model) {
        List<KhachHang> kh = khachHangRepo.findAll();
        model.addAttribute("namePage", "customer-mng");
        model.addAttribute("khachhang", kh);
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        return "/admin/customerManagement";
    }

    @GetMapping("/order-fragment")
    public String getOrderFragment(Model model) {
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());

        return "fragments/header_admin :: header";
    }

    @GetMapping("/search-customer")
    public String searchCustomer(@RequestParam("ten") String ten, Model model) {
        model.addAttribute("khachhang", serviceQLKH.searchCustomer(ten));
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        return "/admin/customerManagement";
    }

    // Update info customer in customer autokid web
    @PostMapping("/update")
    public ResponseEntity<?> updateInfo(@RequestBody Map<String, Object> requset) {
        Map<String, Object> khachHangData = (Map<String, Object>) requset.get("khachHang");

        KhachHang khachHang = new KhachHang();

        khachHang.setId(Integer.parseInt(khachHangData.get("idKH").toString()));
        khachHang.setTenKH(khachHangData.get("tenKH").toString());
        khachHang.setEmail(khachHangData.get("emailKH").toString());
        khachHang.setSdt(khachHangData.get("sdtKH").toString());
        khachHang.setMatKhau(khachHangData.get("matKhau").toString());
        khachHang.setDiaChi(khachHangData.get("diaChiKH").toString());
        serviceQLKH.updateKhachHang(khachHang);

        return ResponseEntity.ok("success");
    }

    // thêm khách hàng mới tại quầy
    @PostMapping("/create-customer")
    @ResponseBody
    public String insertCustomer(@RequestBody KhachHangResponse khres) {
        logger.info("khachHangResponse: {}", khres);
        String result = serviceQLKH.insertKhachHang(khres);
        return result;
    }
}
