package com.example.demo.controller;

import com.example.demo.model.SanPhamChiTiet;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.SanPhamChiTietRepo;
import com.example.demo.model.KhachHang;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.NhanVienRepo;
import com.example.demo.response.KhachHangResponse;
import com.example.demo.service.QuanLyDatHangService;
import com.example.demo.service.QuanLyGioHangService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@Controller
public class ApiCustomController {
    private static Logger logger = LoggerFactory.getLogger(ApiCustomController.class);

    @Autowired
    QuanLyDatHangService serviceQLDH;

    @Autowired
    KhachHangRepo khachHangRepo;

    @Autowired
    NhanVienRepo nhanVienRepo;

    @Autowired
    QuanLyGioHangService qlghService;

    @Autowired
    SanPhamChiTietRepo spctRepo;

    @PostMapping("/update-status")
    @ResponseBody
    public String updateStatusHD(@RequestBody Map<String, Object> map) {
        String trangThai = map.get("trangThai").toString();
        Integer idHD = Integer.parseInt(map.get("idHD").toString());
        return serviceQLDH.updateStatusHD(trangThai, idHD);
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> payload) {
        try {
            Integer idKH = Integer.parseInt(payload.get("idKH").toString());
            Integer idSPCT = Integer.parseInt(payload.get("idSPCT").toString());
            Integer soLuong = Integer.parseInt(payload.get("soLuong").toString());
            System.out.println("===================" + soLuong);
            qlghService.addToCart(idKH, idSPCT, soLuong);

            return ResponseEntity.ok(Map.of("message", "Sản phẩm đã được thêm"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
      
    @GetMapping("/user/{sdt}")
    @ResponseBody
    public ResponseEntity<?> checkExists(@PathVariable String sdt){
        return ResponseEntity.ok().body(khachHangRepo.existsBySdt(sdt));
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody KhachHangResponse response){
        return ResponseEntity.ok().body(serviceQLDH.createUser(response));
    }

    @GetMapping("/staff/{maNV}")
    public ResponseEntity<?> checkExistStaff(@PathVariable String maNV){
        return ResponseEntity.ok().body(nhanVienRepo.existsByMaNV(maNV));
    }
  
    @PostMapping("/check-checkout")
    @ResponseBody
    public ResponseEntity<?> checkCheckout(@RequestBody List<Map<String, Object>> hdct) {
        int i = 1;
        for (Map<String, Object> item : hdct){
            Integer idSPCT = Integer.parseInt(item.get("idSPCT").toString());
            Integer soLuongMua = Integer.parseInt(item.get("soLuong").toString());
            SanPhamChiTiet spct = spctRepo.findById(idSPCT).orElseThrow();
            if (spct.getSoLuong() < soLuongMua) {
                i = 0;
                break;
            }
        }
        if (i == 1) {
            return ResponseEntity.ok().body("Thanh cong");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Loi");
        }
    }

    @PostMapping("/gen-cart")
    @ResponseBody
    public Map<String, Object> genCart(@RequestBody Map<String, Object> map) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        if (map.get("idKH") != null) {
            Integer idKH = Integer.parseInt(map.get("idKH").toString());
            map1.put("cartCount", qlghService.getSoLuongSPCTInCart(idKH));
            map1.put("totalPrice", qlghService.getTotalPrice(idKH));
        }
        return map1;
    }
}
