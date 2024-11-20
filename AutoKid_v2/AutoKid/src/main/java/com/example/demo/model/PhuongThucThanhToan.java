package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "phuong_thuc_thanh_toan")
public class PhuongThucThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pttt")
    private Integer id;

    @Column(name = "ma_pttt")
    private String maPTTT;

    @Column(name = "ten_pttt")
    private String tenPTTT;

    @Column(name = "ma_code")
    private String maCode;

    @Column(name = "thong_tin_thanh_toan")
    private String thongTinThanhToan;

    @Column(name = "trang_thai")
    private Integer trangThai;

}