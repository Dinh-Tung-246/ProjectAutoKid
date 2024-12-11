package com.example.demo.repository;
import com.example.demo.model.SanPhamChiTiet;
import com.example.demo.response.SanPhamChiTietDTO;
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


    @Query("SELECT new com.example.demo.response.SanPhamChiTietDTO(" +
            "spct.maSPCT, sp.tenSP, sp.donGia, spct.soLuong, " +
            "ms.tenMS, th.tenTH, cl.tenCl, kc.tenKC) " +
            "FROM SanPhamChiTiet spct " +
            "JOIN spct.sanPham sp " +
            "LEFT JOIN sp.ChatLieu cl " +
            "LEFT JOIN sp.thuongHieu th " +
            "LEFT JOIN spct.mauSac ms " +
            "LEFT JOIN sp.kichCo kc " +
            "WHERE sp.tenSP LIKE %:tenSP% OR spct.maSPCT LIKE %:maSPCT%")
    List<SanPhamChiTietDTO> findSanPhamChiTietBySanPham_TenSPOrMaSPCT(@Param("tenSP") String tenSP, @Param("maSPCT") String maSPCT);


    @Modifying
    @Query("UPDATE SanPhamChiTiet spct " +
            " SET spct.soLuong = spct.soLuong - :soLuong" +
            " WHERE spct.id = :idSPCT")
    void updateSoLuongSPCT(@Param("soLuong") Integer soLuong, @Param("idSPCT") Integer idSPCT);

}
