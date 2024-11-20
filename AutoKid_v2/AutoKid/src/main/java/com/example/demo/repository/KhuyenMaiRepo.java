package com.example.demo.repository;

import com.example.demo.model.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface KhuyenMaiRepo extends JpaRepository<KhuyenMai, Integer> {
    @Query("SELECT k FROM KhuyenMai k WHERE k.tenKM LIKE %:keyword%")
    List<KhuyenMai> searchByKeyword(@Param("keyword") String keyword);

}
