package com.example.demo.controller;

import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import com.example.demo.model.SanPhamChiTiet;
import com.example.demo.repository.HoaDonChiTietRepo;
import com.example.demo.repository.HoaDonRepo;
import com.example.demo.repository.SanPhamChiTietRepo;
import com.example.demo.response.HoaDonChiTietRequest;
import com.example.demo.service.QuanLyHoaDonService;
import com.example.demo.service.QuanLySanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/home")
    public String products(Model model) {
        List<SanPhamChiTiet> sanPhamChiTiets = service.getAllSanPham();
        List<HoaDon> list = hoaDonService.getHoaDon();
        model.addAttribute("currentPage", "products");
        model.addAttribute("namePage", "ban-hang");
        model.addAttribute("lhd", list);
        model.addAttribute("spcts", sanPhamChiTiets);
        return "admin/ban-hang";
    }
    @PostMapping("/create")
    public ResponseEntity<Integer> createInvoice(@RequestBody HoaDon hoaDon) {
        try {
            // Tạo hóa đơn và tự động tạo hóa đơn chi tiết trống
            HoaDon createdInvoice = hoaDonService.createInvoice(hoaDon);

            if (createdInvoice != null && createdInvoice.getId() != null) {
                System.out.println("Hóa đơn đã được tạo: " + createdInvoice);
                return ResponseEntity.ok(createdInvoice.getId());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo hóa đơn: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    // Thêm sản phẩm vào hóa đơn
    @PostMapping("/add-product-to-invoice")
    public ResponseEntity<?> addProductToInvoice(@RequestParam Integer hoaDonId,
                                                 @RequestParam Integer sanPhamChiTietId,
                                                 @RequestParam Integer soLuong,
                                                 @RequestParam Double donGia) {
        try {
            HoaDon hoaDon = hoaDonRepo.findById(hoaDonId)
                    .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(sanPhamChiTietId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setSoLuong(soLuong);
            hoaDonChiTiet.setDonGia(donGia);
            hoaDonChiTiet.setDonGiaSauGiam(donGia); // Có thể điều chỉnh nếu cần giảm giá

            hoaDonChiTietRepo.save(hoaDonChiTiet);

            return ResponseEntity.ok("Thêm sản phẩm vào hóa đơn thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }
    @GetMapping("/invoice-details")
    public ResponseEntity<List<HoaDonChiTiet>> getInvoiceDetails(@RequestParam Integer hoaDonId) {
        try {
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepo.findByHoaDonId(hoaDonId);
            return ResponseEntity.ok(hoaDonChiTietList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
