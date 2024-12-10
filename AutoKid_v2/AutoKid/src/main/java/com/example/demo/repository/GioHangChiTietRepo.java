package com.example.demo.repository;

import com.example.demo.model.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GioHangChiTietRepo extends JpaRepository<GioHangChiTiet,Integer> {
    @Query("SELECT ghct FROM GioHangChiTiet ghct" +
            " inner join GioHang gh on gh.idGioHang = ghct.gioHang.idGioHang" +
            " where gh.khachHang.id = :idKH")
    List<GioHangChiTiet> getAllGHCTByIdKH(@Param("idKH") Integer idKH);

    @Query("SELECT ghct FROM GioHangChiTiet ghct" +
            " WHERE ghct.gioHang.khachHang.id = :idKH" +
            " AND ghct.sanPhamChiTiet.id = :idSPCT")
    GioHangChiTiet getGHCT(@Param("idKH") Integer idKH, @Param("idSPCT") Integer idSPCT);

    @Modifying
    @Transactional
    @Query("UPDATE GioHangChiTiet ghct" +
            " SET ghct.soLuong = :soLuong, ghct.donGia = :donGia" +
            " WHERE ghct.gioHang.idGioHang = :idGH AND ghct.sanPhamChiTiet.id = :idSPCT")
    void updateSoLuongSPCT(@Param("soLuong") Integer soLuong, @Param("donGia") Double donGia, @Param("idGH") Integer idGH, @Param("idSPCT") Integer idSPCT);
}
