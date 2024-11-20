package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name = "nhan_vien")
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nv")
    private Integer id;

    @Column(name = "ma_nv")
    private String maNV;

    @Column(name = "ten_nv")
    private String tenNV;

    @Column(name = "gioi_tinh")
    private String gioiTinh;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ngay_lam_viec")
    private LocalDate ngayLamViec;

    @Column(name = "mat_khau")
    private String matKhau;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "email")
    private String email;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "trang_thai_nv")
    private Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "id_chuc_vu")
    ChucVu chucVu;

}