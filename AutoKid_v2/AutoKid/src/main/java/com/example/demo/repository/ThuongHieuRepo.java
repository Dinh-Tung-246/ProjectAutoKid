package com.example.demo.repository;

import com.example.demo.model.SanPhamChiTiet;
import com.example.demo.model.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThuongHieuRepo extends JpaRepository<ThuongHieu,Integer> {
    List<ThuongHieu> findAllByOrderByIdDesc();

    boolean existsByMaTH(String maTH);

    @Query("select th from ThuongHieu th where th.tenTH  like ?1")
    List<ThuongHieu> searchThuongHieu(String tenTH);
}
