package com.example.demo.repository;

import com.example.demo.model.KichCo;
import com.example.demo.model.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KichCoRepo extends JpaRepository<KichCo,Integer> {
    List<KichCo> findAllByOrderByIdDesc();
    boolean existsByMaKC(String maKC);
    boolean existsByTenKC(String tenKC);
}
