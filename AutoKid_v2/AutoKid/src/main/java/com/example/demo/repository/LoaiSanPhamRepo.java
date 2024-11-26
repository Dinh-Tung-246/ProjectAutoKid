package com.example.demo.repository;

import com.example.demo.model.KichCo;
import com.example.demo.model.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaiSanPhamRepo extends JpaRepository<LoaiSanPham,Integer> {

}
