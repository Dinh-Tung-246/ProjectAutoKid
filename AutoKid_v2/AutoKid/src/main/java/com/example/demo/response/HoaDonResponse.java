package com.example.demo.response;

import com.example.demo.model.HoaDon;
import lombok.Data;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private boolean isOnline;
    private List<HoaDonChiTietRP> hoaDonChiTietRPS;
    private boolean confirmPayment; // Xác nhận thanh toán

    public HoaDonResponse(HoaDon h) {
        this.id = h.getId();
        this.maHD = h.getMaHD();
        this.tenKhachHang = Objects.toString(h.getTenNguoiNhan());
        this.tenNhanVien = Objects.toString(h.getNhanVien() != null ? h.getNhanVien().getTenNV() : "", "");
        this.tenPhuongThucThanhToan = Objects.toString(h.getPhuongThucThanhToan() != null ? h.getPhuongThucThanhToan().getTenPTTT() : "", "");
        this.ngayTao = h.getNgayTao();
        this.tongTien = h.getTongTien();
        this.trangThaiHD = h.getTrangThaiHD();
        this.isOnline = h.isOnline();
        this.hoaDonChiTietRPS = h.getHoaDonChiTiets()
                .stream()
                .map(HoaDonChiTietRP::new).collect(Collectors.toList());
    }
}
