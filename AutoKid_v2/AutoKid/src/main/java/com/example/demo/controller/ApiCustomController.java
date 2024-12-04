package com.example.demo.controller;

import com.example.demo.service.QuanLyDatHangService;
import com.example.demo.service.QuanLyGioHangService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("/api")
@Controller
public class ApiCustomController {
    private static Logger logger = LoggerFactory.getLogger(ApiCustomController.class);

    @Autowired
    QuanLyDatHangService serviceQLDH;

    @Autowired
    QuanLyGioHangService qlghService;

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
}
