package com.example.demo.repository;

import com.example.demo.model.HoaDon;
import com.example.demo.model.ThongTinVanChuyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThongTinVanChuyenRepo extends JpaRepository<ThongTinVanChuyen,Integer> {
    @Query(value = "SELECT TOP 1 * " +
            "FROM thong_tin_van_chuyen ttvc" +
            " ORDER BY ttvc.id_ttvc DESC", nativeQuery = true)
    ThongTinVanChuyen getTTVC();
}
