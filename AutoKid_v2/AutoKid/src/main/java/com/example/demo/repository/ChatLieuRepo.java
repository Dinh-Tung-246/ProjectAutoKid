package com.example.demo.repository;

import com.example.demo.model.ChatLieu;
import com.example.demo.model.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLieuRepo extends JpaRepository<ChatLieu,Integer> {
    List<ChatLieu> findAllByOrderByIdDesc();
    boolean existsByMaCl(String maCl);
}
