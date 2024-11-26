package com.example.demo.repository;

import com.example.demo.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HoaDonRepo extends JpaRepository<HoaDon,Integer> {
    @Query(value = "SELECT TOP 1 * " +
            "FROM hoa_don h" +
            " ORDER BY h.id_hd DESC", nativeQuery = true)
    HoaDon getHoaDon();

    @Modifying
    @Query(value = "UPDATE hoa_don SET trang_thai_hd = :trangThai" +
            " WHERE id_hd = :idHD ", nativeQuery = true)
    void updateHoaDon(@Param("trangThai") String trangThai,@Param("idHD") Integer idHD);

    Optional findHoaDonByMaHD(String maHD);
}
