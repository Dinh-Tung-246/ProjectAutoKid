package com.example.demo.controller;

import com.example.demo.model.KhuyenMai;
import com.example.demo.model.SanPham;
import com.example.demo.response.SanPhamKhuyenMaiResponse;
import com.example.demo.service.QuanLyKhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/promotion")
public class PromotionController {

    @Autowired
    private QuanLyKhuyenMaiService service;

    @GetMapping("/index")
    public String homeController(Model model){
        model.addAttribute("promotions", service.getAll());
        model.addAttribute("addPromotion", new KhuyenMai());
        model.addAttribute("updatePromotion", new KhuyenMai());
        model.addAttribute("namePage", "promotion");
        return "/admin/promotion";
    }

    @GetMapping("/search")
    public String searchPromotions(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("addPromotion", new KhuyenMai());
        model.addAttribute("updatePromotion", new KhuyenMai());
        model.addAttribute("namePage", "promotion");
        List<KhuyenMai> promotions;
        if (keyword == null || keyword.isEmpty()) {
            promotions = service.getAll();
        } else {
            promotions = service.searchByKeyword(keyword);
        }
        model.addAttribute("promotions", promotions);
        return "/admin/promotion";
    }

    @PostMapping("/save")
    public String addController(@ModelAttribute("promotion") KhuyenMai khuyenMai){
        service.save(khuyenMai);
        return "redirect:/admin/promotion/index";
    }

    @GetMapping("/delete/{id}")
    public String deletePromotion(@PathVariable Integer id){
        service.deleteById(id);
        return "redirect:/admin/promotion/index";
    }

    @PostMapping("/update")
    public String updatePromotion(@ModelAttribute("updatePromotion") KhuyenMai promotion){
        service.updatePromotion(promotion);
        return "redirect:/admin/promotion/index";
    }

    @GetMapping("/product-sale")
    public String getProductPromotion(Model model) {
        List<SanPhamKhuyenMaiResponse> listProductPromotion = service.getSanPhamKhuyenMaiList();
        model.addAttribute("ProductList", listProductPromotion);
        List<SanPham> listProductNoPromotion = service.getProductNoPromotion();
        model.addAttribute("NoPromotionList", listProductNoPromotion);
        model.addAttribute("promotion", service.getPromotionNoApply());
        return "/admin/promotionProduct";
    }

    @PostMapping("/addPromotionInProduct")
    public ResponseEntity<?> addPromotionInProduct(
            @RequestParam("promotionId") Integer promotionId,
            @RequestParam("productIds") List<Integer> productIds){
        for (Integer productId : productIds) {
            service.applyPromotionToProduct(promotionId, productId);
        }
        return ResponseEntity.ok("Khuyến mãi đã được áp dụng thành công!");
    }


}
