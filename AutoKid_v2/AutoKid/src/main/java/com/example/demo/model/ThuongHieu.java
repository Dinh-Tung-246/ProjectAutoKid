package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "thuong_hieu")
public class ThuongHieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thuong_hieu")
    private Integer id;

    @Column(name = "ma_th")
    private String maTH;

    @Column(name = "ten_th")
    private String tenTH;

    @Column(name = "trang_thai_th")
    private String trangThaiTH;

    @Column(name = "dia_chi")
    private String diaChi;

}