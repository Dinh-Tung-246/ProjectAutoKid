package com.example.demo.config;

import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.NhanVien;
import com.example.demo.repository.NhanVienRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomAuthenticatuonsuccessHandler implements AuthenticationSuccessHandler {
    private static Logger logger = LoggerFactory.getLogger(CustomAuthenticatuonsuccessHandler.class);

    @Autowired
    NhanVienRepo nhanVienRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        NhanVien nv = nhanVienRepo.findById(userDetails.getId()).orElseThrow();

        // chuyển đối tượng NhanVien sang JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Sử dụng định dạng ISO-8601
        String jsonNhanVien = objectMapper.writeValueAsString(nv);
        logger.info("=================== {}", jsonNhanVien);
        // Lưu thông tin user vào cookie
        Cookie userCookie = new Cookie("infoNV", URLEncoder.encode(jsonNhanVien, StandardCharsets.UTF_8));
        userCookie.setHttpOnly(false); // Để cho phép JS truy cập
        userCookie.setPath("/");
        userCookie.setMaxAge(24 * 60 * 60); // 24h

        response.addCookie(userCookie);

        String redirrectUrl = "http://localhost:8080/admin/customer-management/";

        response.sendRedirect(redirrectUrl);
    }
}
