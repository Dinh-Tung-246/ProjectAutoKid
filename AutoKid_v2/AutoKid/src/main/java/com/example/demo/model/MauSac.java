package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "mau_sac")
public class MauSac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mau_sac")
    private Integer id;

    @Column(name = "ma_ms")
    private String maMS;

    @Column(name = "ten_ms")
    private String tenMS;

    @Column(name = "trang_thai_ms")
    private String trangThaiMS;

}