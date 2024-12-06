package com.example.demo.model;

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

    @Column(name = "ma_voucher")
    private String ma;

    @Column(name = "ten_voucher")
    private String ten;

    @Column(name = "loai_voucher")
    private int loaiVoucher;

    @Column(name = "dieu_kien")
    private Float dieuKien;

    @Column(name = "gia_tri")
    private Double giaTri;

    @Column(name = "gia_tri_toi_da")
    private Double giaTriToiDa;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "trang_thai")
    private int trangThai;

    @Column(name = "mo_ta")
    private String moTa;

    public String getFormattedgiaTri() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(giaTri).replace("₫", "VND");
    }
    public String getFormattedgiaTriToiDa() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(giaTri).replace("₫", "VND");
    }
}
