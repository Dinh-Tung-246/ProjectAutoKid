package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/autokid/shoping-cart")
public class AutokidShoppingCartController {
    @GetMapping("")
    public String showShoppingCart(HttpSession session, Model model){
        session.setAttribute("isLoggedIn", false);
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");

        if(isLoggedIn){
            // Nếu đã đăng nhập, lấy giỏ hàng từ server-side (ví dụ: từ DB)
            List<Map<String, Object>> cartItems = List.of(
                    Map.of("idSPCT", "100", "tenSPCT", "Xe điện", "donGia", 820000, "soLuong", 2)
            );
            model.addAttribute("cartItems", cartItems);
        }
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("currentPage","shoping-cart");
        return "/autokid/shoping-cart";
    }
}
