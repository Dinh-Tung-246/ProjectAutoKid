package com.example.demo.response;

import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Objects;

@Data
public class HoaDonChiTietRP {
    private String name;
    private String kichCo;
    private String mauSac;
    private String chatLieu;
    private Integer soLuong;
    private Double donGia;
    private Double thanhTien;

    public HoaDonChiTietRP(HoaDonChiTiet hdct) {
        this.name = hdct.getSanPhamChiTiet().getSanPham().getTenSP();
        this.soLuong = hdct.getSoLuong();
        this.donGia = hdct.getDonGia();
        this.thanhTien = hdct.getSoLuong() * hdct.getDonGia();
        this.kichCo = hdct.getSanPhamChiTiet().getSanPham().getKichCo().getTenKC();
        this.chatLieu = hdct.getSanPhamChiTiet().getSanPham().getChatLieu().getTenCl();
        this.mauSac = hdct.getSanPhamChiTiet().getMauSac().getTenMS();
    }
}
