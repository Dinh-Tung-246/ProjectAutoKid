package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.LoaiSanPhamRepo;
import com.example.demo.repository.PhuongThucThanhToanRepo;
import com.example.demo.repository.SanPhamChiTietRepo;
import com.example.demo.service.QuanLyDatHangService;
import com.example.demo.service.QuanLySanPhamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(AutokidCheckoutController.class);

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
    public String showCheckout(Model model) {
        model.addAttribute("currentPage", "checkout");
        model.addAttribute("loaisp", loaiSanPhamRepo.findAll());
        model.addAttribute("pttt", ptttRepo.findAll());
        return "/autokid/checkout";
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> request) {

        // Lấy danh sách HoaDonChiTiet
        List<Map<String, Object>> hoaDonChiTietList = (List<Map<String, Object>>) request.get("hdct");
        // Lấy thông tin HoaDon
        Map<String, Object> hoaDonData = (Map<String, Object>) request.get("hoaDon");
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD((String) hoaDonData.get("maHD"));
        hoaDon.setTenNguoiNhan((String) hoaDonData.get("tenNguoiNhan"));
        hoaDon.setSdtNguoiNhan((String) hoaDonData.get("sdtNguoiNhan"));
        hoaDon.setDiaChiNguoiNhan((String) hoaDonData.get("diaChiNguoiNhan"));
//            hoaDon.setNgayTao(Date.valueOf((String) hoaDonData.get("ngayTao")));
        hoaDon.setTongTien(((Number) hoaDonData.get("tongTien")).floatValue());
        hoaDon.setPhiShip(((Number) hoaDonData.get("phiShip")).floatValue());
        hoaDon.setTrangThaiHD((String) hoaDonData.get("trangThaiHD"));
        hoaDon.setPhiShip(50000F);
        hoaDon.setOnline(true);

        PhuongThucThanhToan pttt = ptttRepo.findById(Integer.parseInt(hoaDonData.get("idPttt").toString())).orElseThrow();
        hoaDon.setPhuongThucThanhToan(pttt);

        // khách hàng Object
        Object idKHO = hoaDonData.get("idKH");
        if (idKHO != null) {
            Integer idKH = Integer.parseInt(idKHO.toString());
            KhachHang kh = khachHangRepo.findById(idKH).orElseThrow();
            hoaDon.setKhachHang(kh);
        }

        // Lưu HoaDon
        qldhService.createHoaDon(hoaDon);

        logger.info("Data: {}", hoaDonChiTietList);
        for (Map<String, Object> hdctData : hoaDonChiTietList) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            Integer soLuong = Integer.parseInt(hdctData.get("soLuong").toString());
            Object idSPCT = hdctData.get("idSPCT");
            if (idSPCT != null) {
                // Ánh xạ sản phẩm chi tiết dựa trên tên (hoặc ID nếu có)
                Integer idSP = Integer.parseInt(idSPCT.toString());
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(idSP)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + idSP));
                hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            }
            hoaDonChiTiet.setSoLuong(Integer.parseInt((String) hdctData.get("soLuong")));
//            hoaDonChiTiet.setDonGia(((Number) hdctData.get("donGia")).doubleValue());
            hoaDonChiTiet.setDonGiaSauGiam(Double.parseDouble(String.valueOf((Integer) hdctData.get("donGiaSauGiam"))));

            // Lưu HoaDonChiTiet
            qldhService.createHDCT(hoaDonChiTiet, soLuong, Integer.parseInt(idSPCT.toString()));
        }

        return ResponseEntity.ok("Đặt hàng thành công");

    }
}
