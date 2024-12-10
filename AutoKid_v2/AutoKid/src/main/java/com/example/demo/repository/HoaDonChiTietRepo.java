package com.example.demo.repository;

import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepo extends JpaRepository<HoaDonChiTiet,Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM HoaDonChiTiet h WHERE h.sanPhamChiTiet.id = :sanPhamChiTietId")
    void deleteBySanPhamChiTietId(@Param("sanPhamChiTietId") Integer sanPhamChiTietId);

    List<HoaDonChiTiet> findByHoaDon(HoaDon hoaDon);
    List<HoaDonChiTiet> findByHoaDonId(Integer hoaDonId);
    List<HoaDonChiTiet> findAllByHoaDonId(Integer id);

}