package com.example.demo.model;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

@Data
@Entity
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voucher")
    private Integer id;

    @Column(name = "ma_voucher", nullable = false, unique = true)
    private String ma;

    @Column(name = "ten_voucher", nullable = false)
    private String ten;

    @Column(name = "loai_voucher", nullable = false)
    private int loaiVoucher;

    @Column(name = "dieu_kien", nullable = false)
    private Double dieuKien;

    @Column(name = "gia_tri", nullable = false)
    private Double giaTri;

    @Column(name = "gia_tri_toi_da")
    private Double giaTriToiDa;

    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Column(name = "trang_thai", nullable = false)
    private Integer trangThai;

}
