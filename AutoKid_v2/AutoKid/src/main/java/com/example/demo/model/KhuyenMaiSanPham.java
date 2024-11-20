package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "khuyen_mai_san_pham")
public class KhuyenMaiSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "khuyen_mai_id")
    private KhuyenMai khuyenMai;

    @ManyToOne
    @JoinColumn(name = "san_pham_id")
    private SanPham sanPham;

    public KhuyenMaiSanPham() {}

    public KhuyenMaiSanPham(KhuyenMai khuyenMai, SanPham sanPham) {
        this.khuyenMai = khuyenMai;
        this.sanPham = sanPham;
    }
}
