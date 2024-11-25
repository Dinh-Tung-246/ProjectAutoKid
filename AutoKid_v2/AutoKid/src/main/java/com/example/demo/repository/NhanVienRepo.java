package com.example.demo.repository;

import com.example.demo.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhanVienRepo extends JpaRepository<NhanVien,Integer> {
    Optional<NhanVien> findByEmail(String email);

    List<NhanVien> findByTenNVContainingOrMaNVContaining(String tenNV, String maNV);
}
