package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.response.HoaDonChiTietRequest;
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
        List<HoaDon> list = hoaDonService.getUnpaidHoaDons();
        model.addAttribute("currentPage", "products");
        model.addAttribute("namePage", "ban-hang");
        model.addAttribute("lhd", list);
        model.addAttribute("spcts", sanPhamChiTiets);
        return "admin/ban-hang";
    }
    @PostMapping("/create")
    public ResponseEntity<Integer> createInvoice(@RequestBody HoaDon hoaDon) {
        try {

//            KhachHang khachHang = khachHangRepo.findBySdt(hoaDon.getKhachHang().getSdt());
            NhanVien nhanVien = nhanVienRepo.findByMaNV(hoaDon.getNhanVien().getMaNV());

            // Cập nhật thông tin cho hóa đơn
//            hoaDon.setKhachHang(khachHang);
            hoaDon.setNhanVien(nhanVien);
            hoaDon.setNgayTao((java.sql.Date) new Date(System.currentTimeMillis()));
            hoaDon.setTrangThaiHD("Chưa thanh toán, chờ giao hàng"); // Nếu cần
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
            // Lấy thông tin hóa đơn
            HoaDon hoaDon = hoaDonRepo.findById(hoaDonId)
                    .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

            // Lấy thông tin sản phẩm chi tiết
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(sanPhamChiTietId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Kiểm tra xem số lượng có đủ trong kho không
            if (sanPhamChiTiet.getSoLuong() < soLuong) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Số lượng sản phẩm không đủ trong kho!");
            }

            // Cập nhật số lượng sản phẩm còn lại
            int newQuantity = sanPhamChiTiet.getSoLuong() - soLuong;

            // Nếu số lượng sau khi trừ không hợp lệ (nhỏ hơn 0), trả về lỗi
            if (newQuantity < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Số lượng sau khi trừ không hợp lệ (nhỏ hơn 0)!");
            }

            // Cập nhật lại số lượng sản phẩm trong kho
            sanPhamChiTiet.setSoLuong(newQuantity);
            sanPhamChiTietRepo.save(sanPhamChiTiet);

            // Tạo chi tiết hóa đơn
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setSoLuong(soLuong);
            hoaDonChiTiet.setDonGia(donGia);
            hoaDonChiTiet.setDonGiaSauGiam(donGia); // Có thể điều chỉnh nếu cần giảm giá

            // Lưu chi tiết hóa đơn vào cơ sở dữ liệu
            hoaDonChiTietRepo.save(hoaDonChiTiet);

            return ResponseEntity.ok("Thêm sản phẩm vào hóa đơn thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/invoice-details")
    public ResponseEntity<?> getInvoiceDetails(@RequestParam Integer hoaDonId) {
        try {
            // Lấy chi tiết hóa đơn
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepo.findByHoaDonId(hoaDonId);
            if (hoaDonChiTietList == null || hoaDonChiTietList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có chi tiết hóa đơn");
            }

            // Chuyển đổi dữ liệu thành một dạng JSON hợp lệ
            List<Map<String, Object>> response = new ArrayList<>();
            for (HoaDonChiTiet chiTiet : hoaDonChiTietList) {
                // Kiểm tra nếu sanPhamChiTiet hoặc sanPham là null
                if (chiTiet.getSanPhamChiTiet() != null && chiTiet.getSanPhamChiTiet().getSanPham() != null) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("tenSP", chiTiet.getSanPhamChiTiet().getSanPham().getTenSP());
                    product.put("soLuong", chiTiet.getSoLuong());
                    product.put("donGia", chiTiet.getDonGia());
                    product.put("sanPhamChiTiet", chiTiet.getSanPhamChiTiet().getId());
                    product.put("tenMS", chiTiet.getSanPhamChiTiet().getMauSac().getTenMS());
                    product.put("tenTH", chiTiet.getSanPhamChiTiet().getSanPham().getThuongHieu().getTenTH());
                    product.put("tenKC", chiTiet.getSanPhamChiTiet().getSanPham().getKichCo().getTenKC());
                    product.put("tenCL", chiTiet.getSanPhamChiTiet().getSanPham().getChatLieu().getTenCl());
                    response.add(product);
                } else {
                    // Nếu sanPhamChiTiet hoặc sanPham là null, có thể thêm thông báo hoặc bỏ qua
                    Map<String, Object> product = new HashMap<>();
                }
            }

            return ResponseEntity.ok(response);  // Trả về dữ liệu dưới dạng JSON hợp lệ
        } catch (Exception e) {
            e.printStackTrace(); // In ra thông báo lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy chi tiết hóa đơn");
        }
    }

//    @DeleteMapping("/delete-product-from-invoice")
//    public ResponseEntity<?> deleteProductFromInvoice(@RequestParam Integer sanPhamChiTietId) {
//        System.out.println("Xóa tất cả chi tiết hóa đơn của sản phẩm: SanPhamChiTietId = " + sanPhamChiTietId);
//
//        // Xóa tất cả các chi tiết hóa đơn của sản phẩm
////        int deletedCount = hoaDonChiTietRepo.deleteBySanPhamChiTietId(sanPhamChiTietId);
//
////        if (deletedCount == 0) {
////            System.out.println("Không tìm thấy chi tiết hóa đơn cho sản phẩm này!");
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy chi tiết hóa đơn cho sản phẩm này!");
////        }
////
////        System.out.println("Đã xóa " + deletedCount + " chi tiết hóa đơn của sản phẩm!");
////        return ResponseEntity.ok("Tất cả chi tiết hóa đơn của sản phẩm đã được xóa!");
//    }


    @DeleteMapping("/delete-details")
    public ResponseEntity<?> deleteInvoiceDetails(@RequestParam Integer id) {
        try {
            hoaDonService.deleteInvoiceDetails(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể xóa chi tiết hóa đơn.");
        }
    }

    // Xóa hóa đơn
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInvoice(@RequestParam Integer id) {
        try {
            hoaDonService.deleteInvoice(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể xóa hóa đơn.");
        }
    }

    @GetMapping("/khachhang/search")
    public ResponseEntity<List<KhachHang>> searchKhachHangBySDT(@RequestParam("sdt") String sdt) {
        List<KhachHang> result = hoaDonService.searchBySDT(sdt);
        return ResponseEntity.ok(result);
    }

}
