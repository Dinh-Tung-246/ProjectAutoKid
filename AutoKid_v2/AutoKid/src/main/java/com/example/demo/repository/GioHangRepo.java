package com.example.demo.repository;

import com.example.demo.model.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GioHangRepo extends JpaRepository<GioHang,Integer> {
    @Query("SELECT gh FROM GioHang gh" +
            " WHERE gh.khachHang.id = :idKH")
    GioHang getAllGHByKH(@Param("idKH") Integer idKH);
}
