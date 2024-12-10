package com.example.demo.service;

import com.example.demo.model.KhuyenMai;
import com.example.demo.model.SanPham;
import com.example.demo.repository.KhuyenMaiRepo;
import com.example.demo.repository.SanPhamRepo;
import com.example.demo.response.SanPhamKhuyenMaiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuanLyKhuyenMaiService {

    @Autowired
    private KhuyenMaiRepo promotionRepository;

    @Autowired
    private SanPhamRepo productRepository;

    public List<KhuyenMai> getAll(){
        return promotionRepository.findAll();
    }

    public void deleteById(Integer id){
        promotionRepository.deleteById(id);
    }

    public void save(KhuyenMai khuyenMai){
        promotionRepository.save(khuyenMai);
    }

    public void getById(Integer id){
        promotionRepository.findById(id);
    }

    public List<KhuyenMai> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return promotionRepository.findAll();
        }
        return promotionRepository.searchByKeyword(keyword);
    }

    public void updatePromotion(KhuyenMai promotion) {
        promotionRepository.save(promotion);
    }

    public List<SanPhamKhuyenMaiResponse> getSanPhamKhuyenMaiList() {
        return productRepository.findAll().stream()
                .map(SanPhamKhuyenMaiResponse::new)
                .collect(Collectors.toList());
    }

    public List<SanPham> getProductNoPromotion() {
        List<SanPham> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(product -> product.getKhuyenMai() == null)
                .collect(Collectors.toList());
    }

    public List<KhuyenMai> getPromotionNoApply() {
        List<KhuyenMai> allPromotion = promotionRepository.findAll();
        return allPromotion.stream()
                .filter(khuyenMai -> khuyenMai.getTrangThaiKM() != 0)
                .collect(Collectors.toList());
    }

    public void applyPromotionToProduct(Integer promotionId, Integer productIds) {
        // Tìm sản phẩm
        SanPham product = productRepository.findById(productIds)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));

        // Tìm khuyến mãi
        KhuyenMai promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Khuyến mãi không tồn tại!"));

        // Áp dụng logic khuyến mãi (ví dụ: giảm giá)
        product.setKhuyenMai(promotion);
        productRepository.save(product);
    }
}
