package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "loai_sp")
public class LoaiSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loai_sp")
    private Integer idLoaiSP;

    @Column(name = "ma_lsp")
    private String maLSP;

    @Column(name = "ten_loai")
    private String tenLoai;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "mo_ta")
    private String moTa;

}