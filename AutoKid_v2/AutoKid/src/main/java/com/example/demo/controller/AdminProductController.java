package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.HoaDonRepo;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.NhanVienRepo;
import com.example.demo.repository.SanPhamRepo;
import com.example.demo.service.QuanLyDatHangService;
import com.example.demo.service.QuanLySanPhamService;

import com.example.demo.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    QuanLySanPhamService service;

    @Autowired
    private Environment env;

    @Autowired
    private SanPhamRepo sanPhamRepo;

    @Autowired
    private KhachHangRepo khachHangRepo;

    @Autowired
    private NhanVienRepo nhanVienRepo;

    @Autowired
    private HoaDonRepo hoaDonRepo;

    @Autowired
    private ThongKeService thongKeService;

    @Autowired
    QuanLyDatHangService serviceQLDH;

//    @GetMapping("/statistical")
//    public String statistical(Model model) {
//        long numberOfOrders = hoaDonRepo.count();
//        long numberOfCustomers = khachHangRepo.count();
//        long numberOfEmployees = nhanVienRepo.count();
//        long numberOfProducts = sanPhamRepo.count();
//
//        model.addAttribute("numberOfOrders", numberOfOrders);
//        model.addAttribute("numberOfCustomers", numberOfCustomers);
//        model.addAttribute("numberOfEmployees", numberOfEmployees);
//        model.addAttribute("numberOfProducts", numberOfProducts);
//
//
//        return "admin/statistical-year";
//    }
    @GetMapping("/statistical-year")
    public String statisticalYear(Model model) {

        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        long numberOfEmployees = nhanVienRepo.count();
        long numberOfProducts = sanPhamRepo.count();

        model.addAttribute("totalRevenue", decimalFormat.format(thongKeService.calculateTotalRevenue()));
        model.addAttribute("invoiceCount", thongKeService.getInvoiceCount());
        model.addAttribute("numberOfEmployees", numberOfEmployees);
        model.addAttribute("numberOfProducts", numberOfProducts);
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("namePage", "statistical");

        return "admin/statistical-year";
    }
    @GetMapping("/statistical-month")
    public String statisticalMonth(Model model) {

        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        long numberOfEmployees = nhanVienRepo.count();
        long numberOfProducts = sanPhamRepo.count();

        model.addAttribute("totalRevenue", decimalFormat.format(thongKeService.calculateTotalRevenue()));
        model.addAttribute("invoiceCount", thongKeService.getInvoiceCount());
        model.addAttribute("numberOfEmployees", numberOfEmployees);
        model.addAttribute("numberOfProducts", numberOfProducts);
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("namePage", "statistical");

        return "admin/statistical-month";
    }
    @GetMapping("/statistical-product")
    public String statisticalProduct(Model model) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        long numberOfEmployees = nhanVienRepo.count();
        long numberOfProducts = sanPhamRepo.count();
        model.addAttribute("totalRevenue", decimalFormat.format(thongKeService.calculateTotalRevenue()));
        model.addAttribute("invoiceCount", thongKeService.getInvoiceCount());
        model.addAttribute("data", thongKeService.findTop5BestSellingProducts());
        model.addAttribute("numberOfEmployees", numberOfEmployees);
        model.addAttribute("numberOfProducts", numberOfProducts);
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("namePage", "statistical");

        return "admin/statistical-product";
    }


    @GetMapping("/thuong-hieu")
    public String thuongHieu(Model model){
        List<ThuongHieu> list = service.getAllThuongHieu();
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("namePage", "statistical");
        model.addAttribute("updateThuongHieu", new ThuongHieu());
        model.addAttribute("ths", list);
        return "admin/thuong-hieu";
    }

    @PostMapping("/update/thuong-hieu")
    public String updateThuongHieu(@ModelAttribute("updateThuongHieu") ThuongHieu thuongHieu){
        if (thuongHieu.getId() != null){
            service.updateThuongHieu(thuongHieu);
        }
        return "redirect:/admin/thuong-hieu";
    }

    @GetMapping("/mau-sac")
    public String mauSac(Model model){
        List<MauSac> list = service.getAllMauSac();
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("namePage", "statistical");
        model.addAttribute("updateMauSac", new MauSac());
        model.addAttribute("mss", list);
        return "admin/mau-sac";
    }

    @PostMapping("/add/mau-sac")
    @ResponseBody
    public ResponseEntity<?> addMauSac(@ModelAttribute MauSac mauSac) {
        try {
            if (service.isMaMSExist(mauSac.getMaMS())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã màu sắc đã tồn tại.");
            }
            if (service.isTenMSExist(mauSac.getTenMS())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên màu sắc đã tồn tại.");
            }
            service.addMauSac(mauSac);
            return ResponseEntity.ok("Thêm màu sắc thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi thêm màu sắc.");
        }
    }


    @GetMapping("/mau-sac/list")
    @ResponseBody
    public List<MauSac> getDanhSacMauSac() {
        return service.getAllMauSac();
    }


    @PostMapping("/update/mau-sac")
    public String updateMauSac(@ModelAttribute("updateMauSac") MauSac mauSac){
        service.updateMauSac(mauSac);
        return "redirect:/admin/mau-sac";
    }



    @GetMapping("/kich-co")
    public String kichCo(Model model){
        List<KichCo> list = service.getAllKichCo();
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("namePage", "statistical");
        model.addAttribute("updateKichCo", new KichCo());
        model.addAttribute("kcs", list);
        return "admin/kich-co";
    }


    @PostMapping("/add/kich-co")
    @ResponseBody
    public ResponseEntity<?> addKichCo(@ModelAttribute KichCo kichCo) {
        try {
            if (service.isMaKCExist(kichCo.getMaKC())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã kích cỡ đã tồn tại.");
            }
            if (service.isTenKCExist(kichCo.getTenKC())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên kích cỡ đã tồn tại.");
            }
            service.addKichCo(kichCo);
            return ResponseEntity.ok("Thêm kích cỡ thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi thêm kích cỡ.");
        }
    }
    @PostMapping("/update/kich-co")
    public String updateKichCo(@ModelAttribute("updateKichCo") KichCo kichCo){
        service.updateKichCo(kichCo);
        return "redirect:/admin/kich-co";
    }

    @GetMapping("/kich-co/list")
    @ResponseBody
    public List<KichCo> getDanhSacKichCo() {
        return service.getAllKichCo();
    }


    @PostMapping("/add/thuong-hieu")
    @ResponseBody
    public ResponseEntity<?> addThuongHieu(@ModelAttribute ThuongHieu thuongHieu) {
        try {
            if (service.isMaTHExist(thuongHieu.getMaTH())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã thương hiệu đã tồn tại.");
            }
            if (service.isTenTHExist(thuongHieu.getTenTH())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên thương hiệu đã tồn tại.");
            }
            service.AddThuongHieu(thuongHieu);
            return ResponseEntity.ok("Thêm thương hiệu thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi thêm thương hiệu.");
        }
    }

    @GetMapping("/thuong-hieu/list")
    @ResponseBody
    public List<ThuongHieu> getDanhSachThuongHieu() {
        return service.getAllThuongHieu();
    }



    @GetMapping("/thuong-hieu/search")
    public String searchTH(@RequestParam("tenTH") String tenTH , Model model){
        List<ThuongHieu> list = service.searchTH("%" + tenTH +"%");
        model.addAttribute("ths", list);
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("namePage", "statistical");
        return "admin/thuong-hieu";
    }

    @GetMapping("/chat-lieu")
    public String chatLieu(Model model){
        List<ChatLieu> list = service.getAllChatLieu();
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("namePage", "statistical");
        model.addAttribute("updateChatLieu", new ChatLieu());
        model.addAttribute("cls", list);
        return "admin/chat-lieu";
    }

    @PostMapping("/add/chat-lieu")
    @ResponseBody
    public ResponseEntity<?> addChatLieu(@ModelAttribute ChatLieu chatLieu) {
        try {
            if (service.isMaCLExist(chatLieu.getMaCl())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã chất liệu đã tồn tại.");
            }
            if (service.isTenCLExist(chatLieu.getTenCl())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên chất liệu đã tồn tại.");
            }
            service.addChatLieu(chatLieu);
            return ResponseEntity.ok("Thêm chất liệu thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi thêm chất liệu.");
        }
    }

    @PostMapping("/update/chat-lieu")
    public String updateChatLieu(@ModelAttribute("updateChatLieu") ChatLieu chatLieu){
        service.updateChatLieu(chatLieu);
        return "redirect:/admin/chat-lieu";
    }
    @GetMapping("/chat-lieu/list")
    @ResponseBody
    public List<ChatLieu> getDanhSacChatLieu() {
        return service.getAllChatLieu();
    }


    @GetMapping("/loai-san-pham")
    public String getAllLoaiSanPham(Model model) {
        model.addAttribute("lsps",service.getAllLoaiSanPham());
        model.addAttribute("updateLoaiSanPham", new LoaiSanPham());
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("namePage", "statistical");
        return "admin/loai-san-pham";
    }

    @PostMapping("/add/loai-san-pham")
    @ResponseBody
    public ResponseEntity<?> addLoaiSanPham(@ModelAttribute LoaiSanPham loaiSanPham) {
        try {
            if (service.isMaLSPExist(loaiSanPham.getMaLSP())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã loại sản phẩm đã tồn tại.");
            }
            if (service.isTenLSPExist(loaiSanPham.getTenLoai())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên loại sản phẩm đã tồn tại.");
            }
            service.addLoaiSanPham(loaiSanPham);
            return ResponseEntity.ok("Thêm loại sản phẩm thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi thêm loại sản phẩm.");
        }
    }

    @GetMapping("/loai-san-pham/list")
    @ResponseBody
    public List<LoaiSanPham> getDanhSacLoaiSanPham() {
        return service.getAllLoaiSanPham();
    }


    @PostMapping("/update/loai-san-pham")
    public String updateLSP(@ModelAttribute("updateLoaiSanPham") LoaiSanPham loaiSanPham) {
        if (loaiSanPham.getIdLoaiSP() != null) {
            service.uodateLoaiSanPham(loaiSanPham);
        }
        return "redirect:/admin/loai-san-pham";
    }
}
