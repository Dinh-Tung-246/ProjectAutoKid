package com.example.demo.response;

import com.example.demo.model.HoaDon;
import lombok.Data;

import java.sql.Date;

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
        this.tenKhachHang = h.getKhachHang().getTenKH();
        this.tenNhanVien = h.getNhanVien().getTenNV();
        this.tenPhuongThucThanhToan = h.getPhuongThucThanhToan().getTenPTTT();
        this.ngayTao = h.getNgayTao();
        this.tongTien = h.getTongTien();
        this.trangThaiHD = h.getTrangThaiHD();
    }
}
