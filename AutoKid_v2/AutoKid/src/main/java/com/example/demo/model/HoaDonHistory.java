package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "hoa_don_history")
public class HoaDonHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lich_su_hoa_don")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_hd")
    private HoaDon hoaDon;

    @Column(name = "ngay_thay_doi")
    private Date ngayThayDoi;

    @Column(name = "ngay_tao")
    private Date ngayTao;

    @Column(name = "trang_thai")
    private String trangThai;

}

