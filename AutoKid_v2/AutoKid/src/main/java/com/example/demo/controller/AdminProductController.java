package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.HoaDonRepo;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.NhanVienRepo;
import com.example.demo.repository.SanPhamRepo;
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



    @GetMapping("/home")
    public String home(){
        return "admin/products";
    }

    @GetMapping("/products")
    public String getAllproducts(Model model) {
        List<SanPhamChiTiet> sanPhamChiTiets = service.getAllSanPham();
        model.addAttribute("dsMauSac", service.getAllMauSac());
        model.addAttribute("dsSanPham", service.DSSanPham());
        model.addAttribute("addSPCT", new SanPhamChiTiet());
        model.addAttribute("updateSPCT", new SanPhamChiTiet());
        model.addAttribute("spct", sanPhamChiTiets);
        return "admin/products";
    }

    @RequestMapping(value = "/add/products", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addSPCT(@ModelAttribute SanPhamChiTiet sanPhamChiTiet) {
        try {
            if (service.isMaSPCTExist(sanPhamChiTiet.getMaSPCT())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã sản phẩm chi tiết đã tồn tại.");
            }
            service.addSanPhamChiTiet(sanPhamChiTiet);
            return ResponseEntity.ok("Thêm sản phẩm chi tiết thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi thêm sản phẩm chi tiết.");
        }
    }
    @GetMapping("/products/list")
    @ResponseBody
    public List<SanPhamChiTiet> getDanhSachSanPhamChiTiet() {
        return service.getAllSanPham();
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
        binder.registerCustomEditor(String.class, "anhSPMau", new PropertyEditorSupport() {
            @Override
            public void setValue(Object value) {
                if (value instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) value;
                    if (!file.isEmpty()) {
                        String originalFilename = file.getOriginalFilename();
                        if (originalFilename != null) {
                            String fileName = System.currentTimeMillis() + "-" + originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                            try {
                                String uploadDir = "C:\\Users\\admin\\ProjectAutoKid\\AutoKid_v2\\AutoKid\\src\\main\\resources\\static\\img\\categories";
                                Path uploadPath = Paths.get(uploadDir);
                                if (!Files.exists(uploadPath)) {
                                    Files.createDirectories(uploadPath);
                                }
                                Path filePath = uploadPath.resolve(fileName);
                                try (InputStream inputStream = file.getInputStream()) {
                                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                                }
                                super.setValue(fileName);
                            } catch (IOException e) {
                                e.printStackTrace();
                                super.setValue(null);
                            }
                        } else {
                            super.setValue(null);
                        }
                    } else {
                        super.setValue(null);
                    }
                } else {
                    super.setValue(value);
                }
            }
        });
        binder.registerCustomEditor(String.class, "anh", new PropertyEditorSupport() {
            @Override
            public void setValue(Object value) {
                if (value instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) value;
                    if (!file.isEmpty()) {
                        String originalFilename = file.getOriginalFilename();
                        if (originalFilename != null) {
                            String fileName = System.currentTimeMillis() + "-" + originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                            try {
                                String uploadDir = "C:\\Users\\admin\\ProjectAutoKid\\AutoKid_v2\\AutoKid\\src\\main\\resources\\static\\img\\categories";
                                Path uploadPath = Paths.get(uploadDir);
                                if (!Files.exists(uploadPath)) {
                                    Files.createDirectories(uploadPath);
                                }
                                Path filePath = uploadPath.resolve(fileName);
                                try (InputStream inputStream = file.getInputStream()) {
                                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                                }
                                super.setValue(fileName);
                            } catch (IOException e) {
                                e.printStackTrace();
                                super.setValue(null);
                            }
                        } else {
                            super.setValue(null);
                        }
                    } else {
                        super.setValue(null);
                    }
                } else {
                    super.setValue(value);
                }
            }
        });
    }

    @GetMapping("/img/categories/{fileName}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable String fileName) throws IOException {
        String imagePath = "C:/Users/admin/ProjectAutoKid/AutoKid_v2/AutoKid/src/main/resources/static/img/categories/" + fileName;
        File imageFile = new File(imagePath);
        InputStream inputStream = new FileInputStream(imageFile);
        byte[] imageBytes = inputStream.readAllBytes();
        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        MediaType mediaType = fileExtension.equals("jpg") || fileExtension.equals("jpeg") ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PostMapping("/add/san-pham")
    @ResponseBody
    public ResponseEntity<?> addSanPham(@ModelAttribute SanPham sanPham) {
        try {
            if (service.isMaSPExist(sanPham.getMaSP())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã sản phẩm đã tồn tại.");
            }
            if (service.isTenSPExist(sanPham.getTenSP())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên sản phẩm đã tồn tại.");
            }
            service.addSanPham(sanPham);
            return ResponseEntity.ok("Thêm sản phẩm thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi thêm sản phẩm.");
        }
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
        return service.DSSanPham();
    }

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

        return "admin/statistical-product";
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
        model.addAttribute("namePage", "kich-co");
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
