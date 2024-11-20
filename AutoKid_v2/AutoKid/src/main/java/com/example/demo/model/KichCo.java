package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "kich_co")
public class KichCo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kich_co")
    private Long id;

    @Column(name = "ma_kc")
    private String maKC;

    @Column(name = "ten_kc")
    private String tenKC;

    @Column(name = "trang_thai_kc")
    private String trangThaiKC;

    @Column(name = "mo_ta")
    private String moTa;

}