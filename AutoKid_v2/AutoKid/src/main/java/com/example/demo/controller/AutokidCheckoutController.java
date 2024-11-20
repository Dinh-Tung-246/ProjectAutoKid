package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.LoaiSanPhamRepo;
import com.example.demo.repository.PhuongThucThanhToanRepo;
import com.example.demo.repository.SanPhamChiTietRepo;
import com.example.demo.service.QuanLyDatHangService;
import com.example.demo.service.QuanLySanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/autokid/checkout")
public class AutokidCheckoutController {

    @Autowired
    QuanLySanPhamService qlspService;

    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;

    @Autowired
    LoaiSanPhamRepo loaiSanPhamRepo;

    @Autowired
    PhuongThucThanhToanRepo ptttRepo;

    @Autowired
    KhachHangRepo khachHangRepo;

    @Autowired
    QuanLyDatHangService qldhService;

    @GetMapping("")
    public String showCheckout(Model model){
        model.addAttribute("currentPage","checkout");
        model.addAttribute("lsp", loaiSanPhamRepo.findAll());
        model.addAttribute("pttt", ptttRepo.findAll());
        return "/autokid/checkout";
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> request) {
        // Lấy thông tin HoaDon
        Map<String, Object> hoaDonData = (Map<String, Object>) request.get("hoaDon");
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD((String) hoaDonData.get("maHD"));
        hoaDon.setTenNguoiNhan((String) hoaDonData.get("tenNguoiNhan"));
        hoaDon.setSdtNguoiNhan((String) hoaDonData.get("sdtNguoiNhan"));
        hoaDon.setDiaChiNguoiNhan((String) hoaDonData.get("diaChiNguoiNhan"));
//        hoaDon.setNgayTao(Date.valueOf((String) hoaDonData.get("ngayTao")));
        hoaDon.setTongTien(((Number) hoaDonData.get("tongTien")).floatValue());
        hoaDon.setPhiShip(((Number) hoaDonData.get("phiShip")).floatValue());
        hoaDon.setTrangThaiHD((String) hoaDonData.get("trangThaiHD"));

        PhuongThucThanhToan pttt = ptttRepo.findById(Integer.parseInt(hoaDonData.get("idPttt").toString())).orElseThrow();
        hoaDon.setPhuongThucThanhToan(pttt);

        Object idKHO = hoaDonData.get("idKH");
        if(idKHO != null) {
        Integer idKH = Integer.parseInt(idKHO.toString());
            KhachHang kh = khachHangRepo.findById(idKH).orElseThrow();
            hoaDon.setKhachHang(kh);
        }

        // Lưu HoaDon
        qldhService.createHoaDon(hoaDon);

        // Lấy danh sách HoaDonChiTiet
        List<Map<String, Object>> hoaDonChiTietList = (List<Map<String, Object>>) request.get("hdct");
        for (Map<String, Object> hdctData : hoaDonChiTietList) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            // Ánh xạ sản phẩm chi tiết dựa trên tên (hoặc ID nếu có)
            Integer idSP = Integer.parseInt(hdctData.get("idSPCT").toString()) ;
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(idSP)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + idSP));
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);

            hoaDonChiTiet.setSoLuong(((Number) hdctData.get("soLuong")).intValue());
//            hoaDonChiTiet.setDonGia(((Number) hdctData.get("donGia")).doubleValue());
            hoaDonChiTiet.setDonGiaSauGiam(((Number) hdctData.get("donGiaSauGiam")).doubleValue());

            // Lưu HoaDonChiTiet
            qldhService.createHDCT(hoaDonChiTiet);
        }

        return ResponseEntity.ok("Đặt hàng thành công");
    }
}
