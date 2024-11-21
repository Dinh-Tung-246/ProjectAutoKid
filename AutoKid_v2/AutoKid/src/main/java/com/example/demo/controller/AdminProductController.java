package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.QuanLySanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    QuanLySanPhamService service;

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
    public String addSPCT(@ModelAttribute SanPhamChiTiet sanPhamChiTiet){
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

    @PostMapping("/add/san-pham")
    public String addSanPham(@ModelAttribute("addSanPham") SanPham sanPham){
        service.addSanPham(sanPham);
        return "redirect:/admin/san-pham";
    }

    @PostMapping("/update/san-pham")
    public String updateProduct(@ModelAttribute("updateSanPham") SanPham sanPham) {
        if (sanPham.getId() != null) {
            service.updateSanPham(sanPham);
        }
        return "redirect:/admin/san-pham";
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
        model.addAttribute("ms", list);
        return "admin/mau-sac";
    }

    @PostMapping("/add/mau-sac")
    public String addMauSac(@ModelAttribute MauSac mauSac){
        service.addMauSac(mauSac);
        return "redirect:/admin/mau-sac";
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
        model.addAttribute("kichco", list);
        return "admin/kich-co";
    }


    @PostMapping("/add/kich-co")
    public String addKichCo(@ModelAttribute KichCo kichCo){
        service.addKichCo(kichCo);
        return "redirect:/admin/kich-co";
    }

    @PostMapping("/update/kich-co")
    public String updateKichCo(@ModelAttribute("updateKichCo") KichCo kichCo){
        service.updateKichCo(kichCo);
        return "redirect:/admin/kich-co";
    }

    @PostMapping("/add/thuong-hieu")
    public String addThuongHieu(@ModelAttribute ThuongHieu thuongHieu){
        service.AddThuongHieu(thuongHieu);
        return "redirect:/admin/thuong-hieu";
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
        model.addAttribute("cl", list);
        return "admin/chat-lieu";
    }

    @PostMapping("/add/chat-lieu")
    public String addChatLieu(@ModelAttribute ChatLieu chatLieu){
        service.addChatLieu(chatLieu);
        return "redirect:/admin/chat-lieu";
    }

    @PostMapping("/update/chat-lieu")
    public String updateChatLieu(@ModelAttribute("updateChatLieu") ChatLieu chatLieu){
        service.updateChatLieu(chatLieu);
        return "redirect:/admin/chat-lieu";
    }

    @GetMapping("/loai-san-pham")
    public String getAllLoaiSanPham(Model model) {
        model.addAttribute("lsps",service.getAllLoaiSanPham());
        model.addAttribute("updateLoaiSanPham", new LoaiSanPham());
        return "admin/loai-san-pham";
    }

    @PostMapping("/add/loai-san-pham")
    public String addLoaiSanPham(@ModelAttribute LoaiSanPham loaiSanPham){
        service.addLoaiSanPham(loaiSanPham);
        return "redirect:/admin/loai-san-pham";
    }

    @PostMapping("/update/loai-san-pham")
    public String updateLSP(@ModelAttribute("updateLoaiSanPham") LoaiSanPham loaiSanPham) {
        if (loaiSanPham.getIdLoaiSP() != null) {
            service.uodateLoaiSanPham(loaiSanPham);
        }
        return "redirect:/admin/loai-san-pham";
    }
}
