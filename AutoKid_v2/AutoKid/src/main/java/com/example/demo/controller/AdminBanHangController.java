package com.example.demo.controller;

import com.example.demo.model.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepo;
import com.example.demo.service.QuanLyHoaDonService;
import com.example.demo.service.QuanLySanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;

import org.springframework.web.bind.annotation.*;

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
    public String createInvoice(@RequestBody Map<String, Object> request) {
        System.out.println(request);

        // Lấy dữ liệu hóa đơn từ request
        Map<String, Object> hoaDonData = (Map<String, Object>) request.get("hoaDon");

        // Tạo đối tượng HoaDon
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD((String) hoaDonData.get("invoiceCode"));
        hoaDon.setTenNguoiNhan((String) hoaDonData.get("customerName"));
        hoaDon.setSdtNguoiNhan((String) hoaDonData.get("customerPhone"));  // Nếu có
        hoaDon.setDiaChiNguoiNhan((String) hoaDonData.get("customerAddress"));  // Nếu có
        hoaDon.setTongTien(((Number) hoaDonData.get("totalAmount")).floatValue());
        hoaDon.setPhiShip(50000F);  // Phí vận chuyển cố định
        hoaDon.setTrangThaiHD((String) hoaDonData.get("status"));  // Nếu có

        // Lưu thông tin hóa đơn vào cơ sở dữ liệu
        hoaDon = hoaDonService.createHoaDon(hoaDon);

        // Lấy dữ liệu chi tiết hóa đơn từ request
        List<Map<String, Object>> hoaDonChiTietList = (List<Map<String, Object>>) request.get("hdct");

        // Xử lý từng chi tiết hóa đơn (hdct)
        for (Map<String, Object> hdctData : hoaDonChiTietList) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            // Lấy số lượng sản phẩm
            Integer soLuong = Integer.parseInt(hdctData.get("quantity").toString());

            // Lấy ID sản phẩm chi tiết và tìm kiếm trong cơ sở dữ liệu
            Object idSPCT = hdctData.get("productId");

            if (idSPCT != null) {
                Integer idSP = Integer.parseInt(idSPCT.toString());
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(idSP)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết: " + idSP));
                hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            }

            // Lấy giá sau khi giảm giá
            hoaDonChiTiet.setSoLuong(soLuong);
            hoaDonChiTiet.setHoaDon(hoaDon);

            // Lưu thông tin chi tiết hóa đơn vào cơ sở dữ liệu
            hoaDonService.createHoaDonChiTiet(hoaDonChiTiet);
        }

        // Trả về thông báo thành công
        return "Đặt hàng thành công";
    }

}
