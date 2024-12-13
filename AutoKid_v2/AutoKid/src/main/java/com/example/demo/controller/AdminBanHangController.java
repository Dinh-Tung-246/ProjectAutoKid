package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.response.ApiResponse;
import com.example.demo.response.KhachHangResponse;
import com.example.demo.response.SanPhamChiTietDTO;
import com.example.demo.service.QuanLyHoaDonService;
import com.example.demo.service.QuanLySanPhamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    PhuongThucThanhToanRepo phuongThucThanhToanRepo;


    @GetMapping("/home")
    public String products(Model model) {
        List<SanPhamChiTiet> sanPhamChiTiets = service.getAllSanPham();
        model.addAttribute("currentPage", "products");
        model.addAttribute("namePage", "ban-hang");
        model.addAttribute("spcts", sanPhamChiTiets);
        model.addAttribute("pttps", phuongThucThanhToanRepo.findAll());
        return "admin/ban-hang";
    }


    @GetMapping("/khachhang/search")
    public ResponseEntity<List<KhachHang>> searchKhachHangBySDT(@RequestParam("sdt") String sdt) {
        List<KhachHang> result = hoaDonService.searchBySDT(sdt);
        // Kiểm tra và loại bỏ vòng lặp đệ quy nếu cần
        for (KhachHang khachHang : result) {
            khachHang.setHoaDons(null); // Đảm bảo không trả về trường hoaDons lặp lại
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/khach-hang/add")
    public ResponseEntity<KhachHang> themKhachHang(@RequestBody KhachHang khachHang) {
        KhachHang savedKhachHang = khachHangRepo.save(khachHang);
        return ResponseEntity.ok(savedKhachHang);
    }

    @PostMapping("/set-session")
    @ResponseBody
    public ResponseEntity<?> setCustomerToSession(@RequestBody Map<String, String> request, HttpSession session) {
        String sdt = request.get("sdt");
        KhachHang khachHang = khachHangRepo.findBySdt(sdt);

        if (khachHang != null) {
            // Chuyển đổi KhachHang thành KhachHangResponse trước khi trả về
            KhachHangResponse khachHangResponse = new KhachHangResponse(khachHang);
            session.setAttribute("customer", khachHangResponse);
            return ResponseEntity.ok(khachHangResponse); // Spring sẽ tự động chuyển đổi sang JSON
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy khách hàng!");
        }
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
        int soLuong = request.getSoLuong();
        boolean success = hoaDonService.updateProductQuantity(maSPCT, soLuong);
        if (success) {
            return ResponseEntity.ok(new ApiResponse(true, "Cập nhật số lượng thành công"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Cập nhật số lượng thất bại"));
        }
    }





    @PostMapping("/create-invoice")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createInvoice(@RequestBody Map<String, Object> hoaDonRequest) {
        System.out.println("Received Invoice Request: " + hoaDonRequest);
        try {
            if (!(hoaDonRequest.get("nhanVien") instanceof Map)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Dữ liệu nhân viên không hợp lệ"));
            }
            if (!(hoaDonRequest.get("khachHang") instanceof Map)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Dữ liệu khách hàng không hợp lệ"));
            }
            Integer idNhanVien = (Integer) ((Map<String, Object>) hoaDonRequest.get("nhanVien")).get("id");
            Integer idKhachHang = (Integer) ((Map<String, Object>) hoaDonRequest.get("khachHang")).get("id");
            if (idNhanVien == null || idKhachHang == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin ID nhân viên hoặc khách hàng"));
            }
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) hoaDonRequest.get("cartItems");
            if (cartItems == null || cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Giỏ hàng không có sản phẩm"));
            }
            Optional<NhanVien> nhanVienOpt = nhanVienRepo.findById(idNhanVien);
            Optional<KhachHang> khachHangOpt = khachHangRepo.findById(idKhachHang);

            if (nhanVienOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy nhân viên với ID: " + idNhanVien));
            }
            if (khachHangOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy khách hàng với ID: " + idKhachHang));
            }
            NhanVien nhanVien = nhanVienOpt.get();
            KhachHang khachHang = khachHangOpt.get();

            // Lấy và kiểm tra thông tin tổng tiền, số lượng
            Integer totalAmount = (Integer) hoaDonRequest.get("totalAmount");
            Integer totalQuantity = (Integer) hoaDonRequest.get("totalQuantity");

            if (totalAmount == null || totalAmount <= 0 || totalQuantity == null || totalQuantity <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Tổng tiền hoặc số lượng không hợp lệ"));
            }
            String maHD = "HD" + UUID.randomUUID().toString();
            HoaDon hoaDon = new HoaDon();
            hoaDon.setMaHD(maHD);
            hoaDon.setKhachHang(khachHang);
            hoaDon.setNhanVien(nhanVien);
            hoaDon.setTongTien(Float.valueOf(totalAmount));
            hoaDon.setTrangThaiHD("Đã thanh toán");
            HoaDon savedInvoice = hoaDonService.save(hoaDon);
            for (Map<String, Object> item : cartItems) {
                String productId = item.get("productId").toString();
                Integer productQuantity = (Integer) item.get("productQuantity");
                Integer productPrice = (Integer) item.get("productPrice");
                if (productId == null || productQuantity == null || productQuantity <= 0 || productPrice == null || productPrice <= 0) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Chi tiết sản phẩm không hợp lệ"));
                }
                Optional<SanPhamChiTiet> productOpt = Optional.ofNullable(sanPhamChiTietRepo.findByMaSPCT(productId));
                if (productOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy sản phẩm với ID: " + productId));
                }
                SanPhamChiTiet product = productOpt.get();
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                hoaDonChiTiet.setHoaDon(savedInvoice);
                hoaDonChiTiet.setSanPhamChiTiet(product);
                hoaDonChiTiet.setSoLuong(productQuantity);
                hoaDonChiTiet.setDonGia(Double.valueOf(productPrice));

                hoaDonService.save(hoaDonChiTiet);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("invoiceId", savedInvoice.getId());
            response.put("invoiceCode", savedInvoice.getMaHD());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Lỗi trong quá trình xử lý yêu cầu"));
        }
    }

//    @GetMapping("/get-employee-and-customer-ids")
//    public ResponseEntity<Map<String, Object>> getEmployeeAndCustomerIds(HttpSession session) {
//        String tenNhanVien = (String) session.getAttribute("tenNV");
//        String tenKhachHang = (String) session.getAttribute("tenKH");
//
//        if (tenNhanVien == null || tenKhachHang == null) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Tên nhân viên hoặc khách hàng không có trong session"));
//        }
//
//        // Lấy ID của nhân viên và khách hàng từ CSDL
//        NhanVien nhanVien = nhanVienRepo.findByTenNV(tenNhanVien);
//        KhachHang khachHang = khachHangRepo.findByTenKH(tenKhachHang);
//
//        if (nhanVien == null || khachHang == null) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy nhân viên hoặc khách hàng"));
//        }
//
//        // Trả về ID của nhân viên và khách hàng
//        Map<String, Object> response = new HashMap<>();
//        response.put("idNV", nhanVien.getId());  // ID nhân viên
//        response.put("idKH", khachHang.getId()); // ID khách hàng
//
//
//        return ResponseEntity.ok(response);
//    }



}
