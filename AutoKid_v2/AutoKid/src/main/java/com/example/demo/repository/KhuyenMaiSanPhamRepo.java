package com.example.demo.repository;

import com.example.demo.model.KhuyenMaiSanPham;
import com.example.demo.response.KhuyenMaiSanPhamRespone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhuyenMaiSanPhamRepo extends JpaRepository<KhuyenMaiSanPham, Integer> {
    @Query("SELECT new com.example.demo.response.KhuyenMaiSanPhamRespone(" +
            "sp.id, sp.tenSP, sp.donGia, " +
            "CAST(sp.donGia - (sp.donGia * km.giaTri / 100.0) AS integer), km.maKM) " +  // Làm tròn kết quả về kiểu Integer
            "FROM KhuyenMaiSanPham kmp " +
            "JOIN kmp.sanPham sp " +
            "JOIN kmp.khuyenMai km " +
            "WHERE km.trangThaiKM = '1' " +
            "AND CURRENT_DATE BETWEEN km.ngayBatDau AND km.ngayKetThuc")
    List<KhuyenMaiSanPhamRespone> findActivePromotionsWithDiscountedPrice();
}
