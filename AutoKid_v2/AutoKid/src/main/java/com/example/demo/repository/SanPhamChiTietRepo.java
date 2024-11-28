package com.example.demo.repository;

import com.example.demo.model.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamChiTietRepo extends JpaRepository<SanPhamChiTiet,Integer> {
    List<SanPhamChiTiet> findAllByOrderByIdDesc();
    SanPhamChiTiet findByMaSPCT(String maSPCT);
    boolean existsByMaSPCT(String maSPCT);

    @Modifying
    @Query("UPDATE SanPhamChiTiet spct " +
            " SET spct.soLuong = spct.soLuong - :soLuong" +
            " WHERE spct.id = :idSPCT")
    void updateSoLuongSPCT(@Param("soLuong") Integer soLuong, @Param("idSPCT") Integer idSPCT);
}
