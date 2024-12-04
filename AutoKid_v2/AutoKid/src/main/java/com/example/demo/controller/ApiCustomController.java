package com.example.demo.controller;

import com.example.demo.model.KhachHang;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.response.KhachHangResponse;
import com.example.demo.service.QuanLyDatHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api")
@Controller
public class ApiCustomController {
    @Autowired
    QuanLyDatHangService serviceQLDH;
    @Autowired
    KhachHangRepo khachHangRepo;

    @PostMapping("/update-status")
    @ResponseBody
    public String updateStatusHD(@RequestBody Map<String, Object> map) {
        String trangThai = map.get("trangThai").toString();
        Integer idHD = Integer.parseInt(map.get("idHD").toString());
        return serviceQLDH.updateStatusHD(trangThai, idHD);
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
}
