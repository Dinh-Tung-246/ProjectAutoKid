package com.example.demo.response;

import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Objects;

@Data
public class HoaDonChiTietRP {
    private String name;
    private Integer soLuong;
    private Double donGia;
    private Double thanhTien;

    public HoaDonChiTietRP(HoaDonChiTiet hdct) {
        this.name = hdct.getSanPhamChiTiet().getSanPham().getTenSP();
        this.soLuong = hdct.getSoLuong();
        this.donGia = hdct.getDonGia();
        this.thanhTien = hdct.getSoLuong() * hdct.getDonGia();
    }
}
