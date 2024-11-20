package com.example.demo.controller;

import com.example.demo.repository.LoaiSanPhamRepo;
import com.example.demo.repository.ThuongHieuRepo;
import com.example.demo.response.SanPhamKhuyenMaiResponse;
import com.example.demo.service.QuanLySanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/autokid/shop")
public class AutokidShopController {

    @Autowired
    QuanLySanPhamService qlspService;

    @Autowired
    LoaiSanPhamRepo lspRepo;

    @Autowired
    ThuongHieuRepo thuongHieuRepo;

    @GetMapping("")
    public String showShop(Model model){
        model.addAttribute("currentPage","shop");
        model.addAttribute("sanpham", qlspService.getAllSP());
        model.addAttribute("spkm", qlspService.getSPKM());
        model.addAttribute("countSP", qlspService.countSP());
        model.addAttribute("loaisp",lspRepo.findAll());
        model.addAttribute("thuonghieu", thuongHieuRepo.findAll());
        return "/autokid/shop-grid";
    }

    @GetMapping("/category")
    public String filterByCategory(@RequestParam Integer idLSP, Model model) {
        model.addAttribute("sanpham", qlspService.searchByLoai(idLSP));
        model.addAttribute("spkm", qlspService.getSPKM());
        model.addAttribute("countSP", qlspService.countSP());
        model.addAttribute("loaisp",lspRepo.findAll());
        model.addAttribute("thuonghieu", thuongHieuRepo.findAll());
        model.addAttribute("currentPage","shop");
        return "/autokid/shop-grid";
    }

    @GetMapping("/price")
    @ResponseBody
    public List<SanPhamKhuyenMaiResponse> filterByPrice(@RequestParam Integer value, Model model) {
        return qlspService.filterByPrice(value);
    }

    @GetMapping("/filter")
    @ResponseBody
    public List<SanPhamKhuyenMaiResponse> filterByPrice2(@RequestParam String filter, Model model) {
        return qlspService.filterAllSP(filter);
    }

    @GetMapping("/brand")
    @ResponseBody
    public List<SanPhamKhuyenMaiResponse> filterByBrands(@RequestParam List<Integer> list){
        if(list == null || list.isEmpty() || list.size() == 0){
            return qlspService.getAllSP();
        } else {
            return qlspService.searchSPByBrands(list);
        }
    }
}
