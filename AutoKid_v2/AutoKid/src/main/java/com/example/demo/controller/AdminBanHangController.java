package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.NhanVienRepo;
import com.example.demo.repository.SanPhamChiTietRepo;
import com.example.demo.service.QuanLyHoaDonService;
import com.example.demo.service.QuanLySanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/create")
    @ResponseBody
    public boolean createInvoice(@RequestBody Map<String, Object> request) {
        System.out.println(request);

        // Lấy dữ liệu hóa đơn từ request
        Map<String, Object> hoaDonData = (Map<String, Object>) request.get("hoaDon");

        KhachHang khachHang = khachHangRepo.findBySdt((String)hoaDonData.get("customerPhone"));
        NhanVien nhanVien = nhanVienRepo.findByMaNV((String)hoaDonData.get("staffId"));
        // Tạo đối tượng HoaDon
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD((String) hoaDonData.get("invoiceCode"));
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setPhuongThucThanhToan(null);
        hoaDon.setNgayTao(new Date(System.currentTimeMillis()));
        hoaDon.setPhiShip(50000F);  // Phí vận chuyển cố định
        hoaDon.setHinhThucThanhToan(null);
        hoaDon.setTongTien(((Number) hoaDonData.get("totalAmount")).floatValue());
        hoaDon.setTrangThaiHD("Chưa thanh toán, chờ giao hàng");  // Nếu có


//         Lưu thông tin hóa đơn vào cơ sở dữ liệu
        hoaDon = hoaDonService.createHoaDon(hoaDon);
        // Lấy dữ liệu chi tiết hóa đơn từ request
        List<Map<String, Object>> hoaDonChiTietList = (List<Map<String, Object>>) hoaDonData.get("products");

        // Xử lý từng chi tiết hóa đơn (hdct)
        for (Map<String, Object> hdctData : hoaDonChiTietList) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            Integer soLuong = Integer.parseInt(hdctData.get("quantity").toString());
            String idSPCT = hdctData.get("productId").toString();
            double donGia = Double.parseDouble(hdctData.get("price").toString());
            Double donGiaSauGiam = Double.parseDouble(hdctData.get("totalPrice").toString());

            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findByMaSPCT(idSPCT);
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
            sanPhamChiTietRepo.save(sanPhamChiTiet);

            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);


            // Lấy giá sau khi giảm giá
            hoaDonChiTiet.setSoLuong(soLuong);
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setDonGia(donGia);
            hoaDonChiTiet.setDonGiaSauGiam(donGiaSauGiam);

            // Lưu thông tin chi tiết hóa đơn vào cơ sở dữ liệu
            hoaDonService.createHoaDonChiTiet(hoaDonChiTiet);
        }

        // Trả về thông báo thành công
        return true;
    }

}
