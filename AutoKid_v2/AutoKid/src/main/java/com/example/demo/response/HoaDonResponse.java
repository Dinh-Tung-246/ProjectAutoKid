package com.example.demo.response;

import com.example.demo.model.HoaDon;
import lombok.Data;

import java.sql.Date;
import java.util.Objects;

@Data
public class HoaDonResponse {
    private Integer id;
    private String maHD;
    private String tenKhachHang;
    private String tenNhanVien;
    private String tenPhuongThucThanhToan;
    private Date ngayTao;
    private Date ngayThayDoi;
    private Float tongTien;
    private String trangThaiHD;

    public HoaDonResponse(HoaDon h) {
        this.id = h.getId();
        this.maHD = h.getMaHD();
        this.tenKhachHang = Objects.toString(h.getKhachHang() != null ? h.getKhachHang().getTenKH() : "", "");
        this.tenNhanVien = Objects.toString(h.getNhanVien() != null ? h.getNhanVien().getTenNV() : "", "");
        this.tenPhuongThucThanhToan = Objects.toString(h.getPhuongThucThanhToan() != null ? h.getPhuongThucThanhToan().getTenPTTT() : "", "");
        this.ngayTao = h.getNgayTao();
        this.tongTien = h.getTongTien();
        this.trangThaiHD = h.getTrangThaiHD();
    }
}
