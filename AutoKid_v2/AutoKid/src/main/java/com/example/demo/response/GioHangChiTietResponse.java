package com.example.demo.response;

import com.example.demo.model.GioHangChiTiet;
import com.example.demo.model.KhuyenMai;
import lombok.Data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Data
public class GioHangChiTietResponse {
    private Integer idSP;
    private Integer idGioHang;
    private Integer idGHCT;
    private Integer soLuong;
    private String donGia;
    private Integer idSPCT;
    private String tenSPCT;
    private String anhSPCT;
    private String mauSac;

    private static String formatPrice(Double price){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm cho phân cách hàng nghìn

        //Định dạng không có phần thập phân
        DecimalFormat formater = new DecimalFormat("#,###", symbols);
        return formater.format(price);
    }

    public GioHangChiTietResponse(GioHangChiTiet ghct) {
        this.idSP = ghct.getSanPhamChiTiet().getSanPham().getId();
        this.idGioHang = ghct.getGioHang().getIdGioHang();
        this.soLuong = ghct.getSoLuong();
        this.idGHCT = ghct.getIdGioHangChiTiet();
        this.idSPCT = ghct.getSanPhamChiTiet().getId();
        this.tenSPCT = ghct.getSanPhamChiTiet().getSanPham().getTenSP();
        if (ghct.getSanPhamChiTiet().getSanPham().getKhuyenMai() != null) {
            KhuyenMai km = ghct.getSanPhamChiTiet().getSanPham().getKhuyenMai();
            this.donGia = formatPrice(ghct.getSanPhamChiTiet().getSanPham().getDonGia() * km.getGiaTri() / 100);
        } else {
            this.donGia = formatPrice(ghct.getSanPhamChiTiet().getSanPham().getDonGia());
        }
        this.anhSPCT = ghct.getSanPhamChiTiet().getAnh();
        this.mauSac = ghct.getSanPhamChiTiet().getMauSac().getTenMS();
    }
}
