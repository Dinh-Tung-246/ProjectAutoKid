package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.QuanLySanPhamService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    QuanLySanPhamService service;

    @Autowired
    private Environment env;



    @GetMapping("/home")
    public String home(){
        return "admin/products";
    }

    @GetMapping("/products")
    public String getAllproducts(Model model) {
        List<SanPhamChiTiet> sanPhamChiTiets = service.getAllSanPham();
        model.addAttribute("dsMauSac", service.getAllMauSac());
        model.addAttribute("dsSanPham", service.DSSanPham());
        model.addAttribute("updateSPCT", new SanPhamChiTiet());
        model.addAttribute("spct", sanPhamChiTiets);
        return "admin/products";
    }

    @PostMapping("/add/san-pham-chi-tiet")
    public String addSPCT(@ModelAttribute SanPhamChiTiet sanPhamChiTiet) {
       service.addSanPhamChiTiet(sanPhamChiTiet);
       return "redirect:/admin/products";
    }

    @PostMapping("/update/products")
    public String updateSPCT(@ModelAttribute("updateSPCT") SanPhamChiTiet sanPhamChiTiet) {
        if (sanPhamChiTiet.getId() != null) {
            service.updateSanPhamChiTiet(sanPhamChiTiet);
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/san-pham")
    public String san_pham(Model model){
        List<SanPham> sanPhams = service.DSSanPham();
        model.addAttribute("dsChatLieu", service.getAllChatLieu());
        model.addAttribute("dsThuongHieu", service.getAllThuongHieu());
        model.addAttribute("dsKichCo", service.getAllKichCo());
        model.addAttribute("dsLoaiSanPham", service.getAllLoaiSanPham());
        model.addAttribute("addSanPham", new SanPham());
        model.addAttribute("updateSanPham", new SanPham());
        model.addAttribute("sps", sanPhams);

        return "admin/san-pham";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Custom editor để xử lý MultipartFile và chuyển đổi sang String (tên file)
        binder.registerCustomEditor(String.class, "anhSPMau", new PropertyEditorSupport() {
            @Override
            public void setValue(Object value) {
                if (value instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) value;

                    // Kiểm tra nếu file không rỗng
                    if (!file.isEmpty()) {
                        String originalFilename = file.getOriginalFilename();
                        if (originalFilename != null) {
                            // Tạo tên file mới, loại bỏ ký tự không hợp lệ và thêm timestamp để tránh trùng lặp
                            String fileName = System.currentTimeMillis() + "-" + originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

                            try {
                                // Đường dẫn lưu file
                                String uploadDir = "C:\\Users\\admin\\Downloads\\ProjectAutoKid\\ProjectAutoKid\\ProjectAutoKid\\AutoKid_v2\\AutoKid\\src\\main\\resources\\static\\img\\categories";
                                Path uploadPath = Paths.get(uploadDir);

                                // Tạo thư mục nếu chưa tồn tại
                                if (!Files.exists(uploadPath)) {
                                    Files.createDirectories(uploadPath);
                                }

                                // Lưu file vào thư mục
                                Path filePath = uploadPath.resolve(fileName);
                                try (InputStream inputStream = file.getInputStream()) {
                                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                                }

                                // Sau khi file được lưu, gán tên file vào model
                                super.setValue(fileName); // Lưu tên file vào đối tượng model
                            } catch (IOException e) {
                                e.printStackTrace();
                                super.setValue(null); // Gán null nếu có lỗi khi lưu file
                            }
                        } else {
                            super.setValue(null); // Gán null nếu không có tên file
                        }
                    } else {
                        super.setValue(null); // Gán null nếu file rỗng
                    }
                } else {
                    super.setValue(value); // Nếu không phải MultipartFile, chỉ trả về giá trị gốc
                }
            }
        });
    }

    @PostMapping("/add/san-pham")
    public String addSanPham(@ModelAttribute SanPham sanPham) {
        try {
            // Sau khi tên file được gán vào sanPham, bạn có thể lưu sanPham vào cơ sở dữ liệu
            service.addSanPham(sanPham);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/san-pham?error=database_save_failed";
        }
        return "redirect:/admin/san-pham";
    }
    @PostMapping("/update/san-pham")
    public String updateProduct(@ModelAttribute("updateSanPham") SanPham sanPham) {
        if (sanPham.getId() != null) {
            service.updateSanPham(sanPham);
        }
        return "redirect:/admin/san-pham";
    }

    @GetMapping("/san-pham/list")
    @ResponseBody
    public List<SanPham> getDanhSachSanPham() {
        // Trả về danh sách sản phẩm dưới dạng JSON
        return service.DSSanPham();
    }
    @GetMapping("/statistical")
    public String statistical() {
        return "admin/statistical";
    }

    @GetMapping("/thuong-hieu")
    public String thuongHieu(Model model){
        List<ThuongHieu> list = service.getAllThuongHieu();
        model.addAttribute("namePage", "thuong-hieu");
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
        model.addAttribute("namePage", "mau-sac");
        model.addAttribute("updateMauSac", new MauSac());
        model.addAttribute("mss", list);
        return "admin/mau-sac";
    }

    @PostMapping("/add/mau-sac")
    public String addMauSac(@ModelAttribute MauSac mauSac){
        service.addMauSac(mauSac);
        return "redirect:/admin/mau-sac";
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
        model.addAttribute("namePage", "kich-co");
        model.addAttribute("updateKichCo", new KichCo());
        model.addAttribute("kcs", list);
        return "admin/kich-co";
    }


    @PostMapping("/add/kich-co")
    @ResponseBody
    public ResponseEntity<?> addKichCo(@ModelAttribute KichCo kichCo) {
        try {
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
        return "admin/thuong-hieu";
    }

    @GetMapping("/chat-lieu")
    public String chatLieu(Model model){
        List<ChatLieu> list = service.getAllChatLieu();
        model.addAttribute("namePage", "chat-lieu");
        model.addAttribute("updateChatLieu", new ChatLieu());
        model.addAttribute("cls", list);
        return "admin/chat-lieu";
    }

    @PostMapping("/add/chat-lieu")
    @ResponseBody // Thêm annotation này để trả về JSON
    public ResponseEntity<?> addChatLieu(@ModelAttribute ChatLieu chatLieu) {
        try {
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
        return "admin/loai-san-pham";
    }

    @PostMapping("/add/loai-san-pham")
    @ResponseBody
    public ResponseEntity<?> addLoaiSanPham(@ModelAttribute LoaiSanPham loaiSanPham) {
        try {
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
