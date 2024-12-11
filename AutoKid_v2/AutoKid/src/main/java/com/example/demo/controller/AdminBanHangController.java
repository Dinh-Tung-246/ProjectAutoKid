package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.response.ApiResponse;
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
    public String setCustomerToSession(@RequestBody Map<String, String> request, HttpSession session) throws JsonProcessingException {
        String sdt = request.get("sdt");
        KhachHang khachHang = khachHangRepo.findBySdt(sdt);

        if (khachHang != null) {
            // Lưu thông tin khách hàng vào session
            session.setAttribute("customer", khachHang);
            // Trả về thông tin khách hàng dưới dạng JSON
            return new ObjectMapper().writeValueAsString(khachHang);
        } else {
            return "Không tìm thấy khách hàng!";
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
        boolean success = hoaDonService.updateProductQuantity(maSPCT, request.getSoLuong());
        if (success) {
            return ResponseEntity.ok(new ApiResponse(true, "Cập nhật số lượng thành công"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Cập nhật số lượng thất bại"));
        }
    }



    @PostMapping("/create-invoice")
    public ResponseEntity<Map<String, Object>> createInvoice(@RequestBody HoaDon hoaDonRequest) {

        // Kiểm tra thông tin ID nhân viên và khách hàng từ request
        Integer idNhanVien = hoaDonRequest.getNhanVien().getId();
        Integer idKhachHang = hoaDonRequest.getKhachHang().getId();
        System.out.println("Khách hàng: " + hoaDonRequest.getKhachHang());
        System.out.println("NV: " + hoaDonRequest.getNhanVien());
        // Kiểm tra thiếu thông tin ID nhân viên hoặc khách hàng
        if (idNhanVien == null ) {
            return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin ID nhân viên "));
        }
        if (idKhachHang == null ) {
            return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin ID KH "));
        }

        // Kiểm tra thông tin giỏ hàng
        if (hoaDonRequest.getHoaDonChiTiets() == null || hoaDonRequest.getHoaDonChiTiets().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Giỏ hàng không có sản phẩm"));
        }

        // Tìm nhân viên và khách hàng trong cơ sở dữ liệu
        Optional<NhanVien> nhanVienOpt = nhanVienRepo.findById(idNhanVien);
        Optional<KhachHang> khachHangOpt = khachHangRepo.findById(idKhachHang);

        // Kiểm tra không tìm thấy nhân viên hoặc khách hàng
        if (nhanVienOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy nhân viên với ID: " + idNhanVien));
        }
        if (khachHangOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy khách hàng với ID: " + idKhachHang));
        }

        NhanVien nhanVien = nhanVienOpt.get();
        KhachHang khachHang = khachHangOpt.get();


        // Kiểm tra các trường thông tin hóa đơn
        if (hoaDonRequest.getTongTien() == null || hoaDonRequest.getTongTien() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tổng tiền hóa đơn phải lớn hơn 0"));
        }

        // Tạo mã hóa đơn duy nhất bằng UUID
        String maHD = "HD" + UUID.randomUUID().toString();

        // Khởi tạo hóa đơn
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD(maHD);
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setTongTien(hoaDonRequest.getTongTien());
        hoaDon.setTrangThaiHD("Đã thanh toán");
        hoaDon.setOnline(hoaDonRequest.isOnline());
        if (hoaDon.getNhanVien() != null) {
            Integer nhanVienId = hoaDon.getNhanVien().getId();
            // Tiếp tục các thao tác với nhanVienId (ví dụ lưu vào database, xử lý thêm thông tin)
            System.out.println("Nhân viên ID: " + nhanVienId);
        } else {
            // Nếu Nhân viên là null, xử lý thông báo lỗi hoặc hành động khác
            System.out.println("Nhân viên không được chỉ định trong hóa đơn.");
            // Bạn có thể trả về một thông báo lỗi hoặc xử lý theo logic khác ở đây

        }
        // Lưu hóa đơn
        HoaDon savedInvoice = hoaDonService.save(hoaDon);

        // Lưu chi tiết hóa đơn
        for (HoaDonChiTiet hoaDonChiTietRequest : hoaDonRequest.getHoaDonChiTiets()) {
            // Kiểm tra chi tiết hóa đơn
            if (hoaDonChiTietRequest.getSanPhamChiTiet() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Chi tiết hóa đơn thiếu thông tin sản phẩm"));
            }
            if (hoaDonChiTietRequest.getSoLuong() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Số lượng sản phẩm phải lớn hơn 0"));
            }
            if (hoaDonChiTietRequest.getDonGia() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Đơn giá sản phẩm phải lớn hơn 0"));
            }

            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setHoaDon(savedInvoice);
            hoaDonChiTiet.setSanPhamChiTiet(hoaDonChiTietRequest.getSanPhamChiTiet());
            hoaDonChiTiet.setSoLuong(hoaDonChiTietRequest.getSoLuong());
            hoaDonChiTiet.setDonGia(hoaDonChiTietRequest.getDonGia());
            hoaDonChiTiet.setDonGiaSauGiam(hoaDonChiTietRequest.getDonGiaSauGiam());

            hoaDonService.save(hoaDonChiTiet);
        }

        // Trả về kết quả thành công
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("invoiceId", savedInvoice.getId());
        response.put("invoiceCode", savedInvoice.getMaHD());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-employee-and-customer-ids")
    public ResponseEntity<Map<String, Object>> getEmployeeAndCustomerIds(HttpSession session) {
        String tenNhanVien = (String) session.getAttribute("tenNV");
        String tenKhachHang = (String) session.getAttribute("tenKH");

        if (tenNhanVien == null || tenKhachHang == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tên nhân viên hoặc khách hàng không có trong session"));
        }

        // Lấy ID của nhân viên và khách hàng từ CSDL
        NhanVien nhanVien = nhanVienRepo.findByTenNV(tenNhanVien);
        KhachHang khachHang = khachHangRepo.findByTenKH(tenKhachHang);

        if (nhanVien == null || khachHang == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy nhân viên hoặc khách hàng"));
        }

        // Trả về ID của nhân viên và khách hàng
        Map<String, Object> response = new HashMap<>();
        response.put("idNV", nhanVien.getId());  // ID nhân viên
        response.put("idKH", khachHang.getId()); // ID khách hàng


        return ResponseEntity.ok(response);
    }



}
