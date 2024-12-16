package com.example.demo.repository;

import com.example.demo.model.SanPham;
import com.example.demo.model.Voucher;
import com.example.demo.response.SanPhamKhuyenMaiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepo extends JpaRepository<SanPham,Integer> {

    List<SanPham> findAllByOrderByIdDesc();
    boolean existsByMaSP(String maSP);
    boolean existsByTenSP(String tenSP);
    @Query("SELECT s FROM SanPham s" +
            " WHERE s.donGia >= :gia1 AND s.donGia <= :gia2 ")
    List<SanPham> searchByPrice(@Param("gia1") Double gia1, @Param("gia2") Double gia2);

    @Query("SELECT s FROM SanPham s" +
            " INNER JOIN ThuongHieu t ON t.id = s.thuongHieu.id " +
            " WHERE t.id in :list")
    List<SanPham> searchByBrands(@Param("list") List<Integer> list);

    @Query("SELECT s FROM SanPham s ORDER BY s.donGia")
    List<SanPham> findAllSanPham();

    @Query("SELECT s FROM SanPham s " +
            " WHERE s.tenSP LIKE %:tenSanPham%")
    List<SanPham> findAllByName(@Param("tenSanPham") String tenSanPham);

    @Query("SELECT sp FROM SanPham sp WHERE " +
            "LOWER(sp.tenSP) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(sp.maSP) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SanPham> searchByKeyword(String keyword);

}
