package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "san_pham")
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_san_pham")
    private Integer id;

    @Column(name = "ma_sp")
    private String maSP;

    @Column(name = "ten_sp")
    private String tenSP;

    @Column(name = "gia_nhap")
    private Double giaNhap;

    @Column(name = "gia_ban")
    private Double donGia;

    @Column(name = "ngay_tao")
    private String ngayTao;

    @Column(name = "trang_thai_sp")
    private String trangThaiSP;

    @Column(name = "anh_sp_mau")
    private String anhSPMau;

    @Column(name = "mo_ta")
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "id_chat_lieu")
    private ChatLieu ChatLieu;

    @ManyToOne
    @JoinColumn(name = "id_kich_co")
    private KichCo kichCo;

    @ManyToOne
    @JoinColumn(name = "id_thuong_hieu")
    private ThuongHieu thuongHieu;


    @ManyToOne
    @JoinColumn(name = "id_loai_sp")
    private LoaiSanPham loaiSanPham;

    @ManyToOne
    @JoinColumn(name = "id_km")
    private KhuyenMai khuyenMai;

    @OneToMany(mappedBy = "sanPham")
    private List<SanPhamChiTiet> sanPhamChiTiets;

//    @OneToMany(mappedBy = "khuyenMai", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<SP> sanPhams; // Danh sách sản phẩm liên kết với khuyến mãi
}