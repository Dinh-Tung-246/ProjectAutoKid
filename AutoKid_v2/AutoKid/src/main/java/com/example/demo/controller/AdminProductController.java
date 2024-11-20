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
    public String products(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 7;
        PageRequest pageable = PageRequest.of(page, pageSize);
        Page<SanPhamChiTiet> sanPhamChiTiets =  service.getAllSanPhamChiTiets(pageable);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sanPhamChiTiets.getTotalPages());
        model.addAttribute("namePage", "products");
        model.addAttribute("spcts", sanPhamChiTiets.getContent());
        return "admin/products";
    }


    @GetMapping("/statistical")
    public String statistical() {
        return "admin/statistical";
    }

    @GetMapping("/thuong-hieu")
    public String thuongHieu(Model model){
        List<ThuongHieu> list = service.getAllThuongHieu();
        model.addAttribute("namePage", "thuong-hieu");
        model.addAttribute("ths", list);
        return "admin/thuong-hieu";
    }

    @GetMapping("/mau-sac")
    public String mauSac(Model model){
        List<MauSac> list = service.getAllMauSac();
        model.addAttribute("namePage", "mau-sac");
        model.addAttribute("ms", list);
        return "admin/mau-sac";
    }

    @PostMapping("/add/mau-sac")
    public String addMauSac(@ModelAttribute MauSac mauSac){
        service.addMauSac(mauSac);
        return "redirect:/admin/mau-sac";
    }

    @GetMapping("/mau-sac/delete")
    public String deleteMauSac(@RequestParam("id") Integer id){
        service.deleteMauSac(id);
        return "redirect:/admin/mau-sac";
    }


    @GetMapping("/kich-co")
    public String kichCo(Model model){
        List<KichCo> list = service.getAllKichCo();
        model.addAttribute("namePage", "kich-co");
        model.addAttribute("kichco", list);
        return "admin/kich-co";
    }


    @PostMapping("/add/kich-co")
    public String addKichCo(@ModelAttribute KichCo kichCo){
        service.addKichCo(kichCo);
        return "redirect:/admin/kich-co";
    }

    @GetMapping("/kich-co/delete")
    public String deleteKichCo(@RequestParam("id") Integer id){
        service.deleteKichCo(id);
        return "redirect:/admin/kich-co";
    }

    @PostMapping("/add/thuong-hieu")
    public String addThuongHieu(@ModelAttribute ThuongHieu thuongHieu){
        service.AddThuongHieu(thuongHieu);
        return "redirect:/admin/thuong-hieu";
    }

    @GetMapping("/thuong-hieu/delete")
    public String deleteThuongHieu(@RequestParam("id") Integer id){
        service.DeleteThuongHieu(id);
        return "redirect:/admin/thuong-hieu";
    }

    @GetMapping("/thuong-hieu/detail")
    public String detailThuongHieu(@RequestParam("id") Integer id){
        service.DetailThuongHieu(id);
        return "admin/thuong-hieu";
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

    @GetMapping("/delete/chat-lieu")
    public String deleteChatLieu(@RequestParam("id") Integer id){
        service.deleteChatLieu(id);
        return "redirect:/admin/chat-lieu";
    }

    @GetMapping("/loai-san-pham")
    public String getAllLoaiSanPham(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 7;
        PageRequest pageable = PageRequest.of(page, pageSize);
        Page<LoaiSanPham> loaiSanPhams = service.getAllLoaiSanPham(pageable);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", loaiSanPhams.getTotalPages());
        model.addAttribute("lsps", loaiSanPhams.getContent());
        return "admin/loai-san-pham";
    }
}
