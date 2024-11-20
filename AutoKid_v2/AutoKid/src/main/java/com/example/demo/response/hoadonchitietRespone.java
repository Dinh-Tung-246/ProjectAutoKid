package com.example.demo.response;

import com.example.demo.model.HoaDonChiTiet;
import lombok.Data;

import java.sql.Date;

@Data
public class hoadonchitietRespone {
    private Integer id;
    private String maHD;
    private String tenKhachHang;
    private String tenNhanVien;
    private String tenSP;
    private String tenPhuongThucThanhToan;
    private Date ngayTao;
    private Float tongTien;
    private Integer soLuong;
    private Double donGiaSauGiam;
    private String trangThaiHD;

    public hoadonchitietRespone(HoaDonChiTiet hdct){
        this.id = hdct.getId();
        this.maHD = hdct.getHoaDon().getMaHD();
        this.tenKhachHang = hdct.getHoaDon().getKhachHang().getTenKH();
        this.tenNhanVien = hdct.getHoaDon().getNhanVien().getTenNV();
        this.tenSP = hdct.getSanPhamChiTiet().getSanPham().getTenSP();
        this.tenPhuongThucThanhToan = hdct.getHoaDon().getPhuongThucThanhToan().getTenPTTT();
        this.ngayTao = hdct.getHoaDon().getNgayTao();
        this.tongTien = hdct.getHoaDon().getTongTien();
        this.soLuong = hdct.getSoLuong();
        this.donGiaSauGiam = hdct.getDonGiaSauGiam();
        this.trangThaiHD = hdct.getHoaDon().getTrangThaiHD();

    }
}
