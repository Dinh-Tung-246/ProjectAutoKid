package com.example.demo.repository;

import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepo extends JpaRepository<HoaDonChiTiet,Integer> {
    List<HoaDonChiTiet> findByHoaDon(HoaDon hoaDon);
    List<HoaDonChiTiet> findByHoaDonId(Integer hoaDonId);
}
