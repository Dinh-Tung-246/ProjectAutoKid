package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.response.ApiResponse;
import com.example.demo.response.SanPhamChiTietDTO;
import com.example.demo.service.QuanLyHoaDonService;
import com.example.demo.service.QuanLySanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/ban-hang")
public class AdminBanHangController {

    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;

    @Autowired
    QuanLySanPhamService service;

    @Autowired
    QuanLyHoaDonService hoaDonService;

    @Autowired
    HoaDonRepo hoaDonRepo;

    @Autowired
    HoaDonChiTietRepo hoaDonChiTietRepo;

    @Autowired
    KhachHangRepo khachHangRepo;

    @Autowired
    NhanVienRepo nhanVienRepo;


    @GetMapping("/home")
    public String products(Model model) {
        List<SanPhamChiTiet> sanPhamChiTiets = service.getAllSanPham();
        model.addAttribute("currentPage", "products");
        model.addAttribute("namePage", "ban-hang");
        model.addAttribute("spcts", sanPhamChiTiets);
        return "admin/ban-hang";
    }


    @GetMapping("/khachhang/search")
    public ResponseEntity<List<KhachHang>> searchKhachHangBySDT(@RequestParam("sdt") String sdt) {
        List<KhachHang> result = hoaDonService.searchBySDT(sdt);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/khach-hang/add")
    public ResponseEntity<KhachHang> themKhachHang(@RequestBody KhachHang khachHang) {
        KhachHang savedKhachHang = khachHangRepo.save(khachHang);
        return ResponseEntity.ok(savedKhachHang);
    }
    @GetMapping("/san-pham-chi-tiet/search")
    public ResponseEntity<List<SanPhamChiTietDTO>> searchSPCTByTenSPOrMaSPCT(@RequestParam("tenSP") String tenSP, @RequestParam("maSPCT") String maSPCT) {
        List<SanPhamChiTietDTO> result = sanPhamChiTietRepo.findSanPhamChiTietBySanPham_TenSPOrMaSPCT(tenSP, maSPCT);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/san-pham-chi-tiet/{maSPCT}/get-quantity")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProductQuantity(@PathVariable String maSPCT) {
        Optional<SanPhamChiTiet> sanPham = hoaDonService.findOptionalByMaSPCT(maSPCT);
        if (sanPham.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("soLuong", sanPham.get().getSoLuong());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Sản phẩm không tồn tại!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    @PutMapping("/san-pham-chi-tiet/{maSPCT}/update-quantity")
    public ResponseEntity<?> updateProductQuantity(@PathVariable("maSPCT") String maSPCT, @RequestBody SanPhamChiTietDTO request) {
        boolean success = hoaDonService.updateProductQuantity(maSPCT, request.getSoLuong());
        if (success) {
            return ResponseEntity.ok(new ApiResponse(true, "Cập nhật số lượng thành công"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Cập nhật số lượng thất bại"));
        }
    }

}
