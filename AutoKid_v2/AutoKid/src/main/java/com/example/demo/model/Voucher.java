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
    private double dieuKien;

    @Column(name = "gia_tri")
    private double giaTri;

    @Column(name = "gia_tri_toi_da")
    private Double giaTriToiDa;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "trang_thai")
    private int trangThai;

//    public String getFormattedgiaTri() {
//        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        return formatter.format(giaTri).replace("₫", "");
//    }
//    public String getFormattedgiaTriToiDa() {
//        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        return formatter.format(giaTriToiDa).replace("₫", "VND");
//    }
}
