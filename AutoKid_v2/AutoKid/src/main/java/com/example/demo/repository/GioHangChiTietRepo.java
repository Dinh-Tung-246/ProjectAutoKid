package com.example.demo.repository;

import com.example.demo.model.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GioHangChiTietRepo extends JpaRepository<GioHangChiTiet,Integer> {
//    @Query("select * from gio_hang_chi_tiet ghct join gio_hang gh on gh.id_gio_hang = ghct.id_gio_hang \n" +
//            "join khach_hang kh on kh.id_kh = gh.id_kh\n" +
//            "where kh.id_kh = 2")
}
