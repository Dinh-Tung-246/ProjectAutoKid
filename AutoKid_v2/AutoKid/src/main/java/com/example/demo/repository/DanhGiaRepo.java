package com.example.demo.repository;

import com.example.demo.model.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhGiaRepo extends JpaRepository<DanhGia,Integer> {
}
