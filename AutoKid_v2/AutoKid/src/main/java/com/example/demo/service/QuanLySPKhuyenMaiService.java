package com.example.demo.service;


import com.example.demo.repository.KhuyenMaiSanPhamRepo;
import com.example.demo.response.KhuyenMaiSanPhamRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuanLySPKhuyenMaiService {

    @Autowired
    private KhuyenMaiSanPhamRepo khuyenMaiSanPhamRepo;

    public List<KhuyenMaiSanPhamRespone> getActivePromotionsWithDiscountedPrice() {
        return khuyenMaiSanPhamRepo.findActivePromotionsWithDiscountedPrice();
    }
}
