package com.example.demo.service;

import com.example.demo.model.KhuyenMai;
import com.example.demo.repository.KhuyenMaiRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuanLyKhuyenMaiService {

    @Autowired
    private KhuyenMaiRepo promotionRepository;

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
}
