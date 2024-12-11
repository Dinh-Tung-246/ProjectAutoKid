package com.example.demo.repository;

import com.example.demo.model.KhachHang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepo extends JpaRepository<KhachHang,Integer> {
    // search
    @Query("SELECT k  FROM KhachHang k " +
            "WHERE k.tenKH LIKE %:ten% ")
    List<KhachHang> findByName(@Param("ten") String ten);

    // Create Account
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO khach_hang(ten_kh, email, mat_khau) VALUES (:ten, :email, :matKhau)",  nativeQuery = true)
    void createAccount(@Param("ten") String ten,
                       @Param("email") String email,
                       @Param("matKhau") String matKhau);

    @Query(value = "SELECT k  FROM KhachHang k " +
            "WHERE k.sdt LIKE %:sdt% ")
    List<KhachHang> findBySDT(@Param("sdt") String sdt);

    @Query(value = "SELECT TOP 1 * FROM khach_hang ORDER BY id_kh DESC", nativeQuery = true)
    KhachHang getKHByIdDESC();

    boolean existsBySdt(String sdt);

    KhachHang findBySdt(String sdt);
}
