package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "gio_hang")
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gio_hang")
    private Integer idGioHang;

    @Column(name = "trang_thai")
    private String trangThai;

//    @Column(name = "ngay_tao")
//    private Date ngayTao;

    @ManyToOne
    @JoinColumn(name = "id_kh")
    private KhachHang khachHang;
}