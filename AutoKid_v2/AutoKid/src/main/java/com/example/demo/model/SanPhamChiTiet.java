package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_san_pham_chi_tiet")
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "id_san_pham")
    @JsonBackReference
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "id_mau_sac")
    private MauSac mauSac;

    @Column(name = "ma_spct")
    private String maSPCT;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "anh")
    private String anh;

    @Column(name = "trang_thai_spct")
    private String trangThaiSPCT;

}