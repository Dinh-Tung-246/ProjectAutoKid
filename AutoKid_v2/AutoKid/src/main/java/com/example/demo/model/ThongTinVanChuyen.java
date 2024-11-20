package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "thong_tin_van_chuyen")
public class ThongTinVanChuyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ttvc")
    private Integer id;

    @Column(name = "ma_ttvc")
    private String maTTVC;

//    @Column(name = "tinh")
//    private String tinh;
//
//    @Column(name = "huyen")
//    private String huyen;
//
//    @Column(name = "xa")
//    private String xa;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Column(name = "sdt")
    private String sdt;

}