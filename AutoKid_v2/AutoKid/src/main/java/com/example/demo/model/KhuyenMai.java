package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "khuyen_mai")
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_khuyen_mai")
    private Integer id;

    @Column(name = "ma_km")
    private String maKM;

    @Column(name = "ten_km")
    private String tenKM;

    @Column(name = "gia_tri")
    private Double giaTri;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "trang_thai")
    private String trangThaiKM;

//    @OneToMany(mappedBy = "khuyenMai", cascade = CascadeType.ALL)
//    private List<KhuyenMaiSanPham> khuyenMaiSanPham;

}