package com.example.demo.repository;

import com.example.demo.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VoucherRepo extends JpaRepository<Voucher, Integer> {
    Voucher findByMa(String ma);

    @Query("SELECT v FROM Voucher v" +
            " WHERE v.trangThai = 1 " +
            " AND v.ngayBatDau < :today" +
            " AND v.ngayKetThuc > :today")
    List<Voucher> getAllVoucherisAction(@Param("today") LocalDate today);
}
