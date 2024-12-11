package com.example.demo.response;

import com.example.demo.model.HoaDonChiTiet;
import lombok.Data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Data
public class DonHangChiTietResponse {
    private Integer idHDCT;
    private Integer idHD;
    private String tenSPCT;
    private String donGia;
    private Integer soLuong;
    private String anhSPCT;
    private Integer idSPCT;

    private static String formatPrice(Double price){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm cho phân cách hàng nghìn

        //Định dạng không có phần thập phân
        DecimalFormat formater = new DecimalFormat("#,###", symbols);
        return formater.format(price);
    }

    public DonHangChiTietResponse(HoaDonChiTiet h){
        this.idHDCT = h.getId();
        this.idHD = h.getHoaDon().getId();
        this.tenSPCT = h.getSanPhamChiTiet().getSanPham().getTenSP() + "(" + h.getSanPhamChiTiet().getMauSac().getTenMS() + ")";
        this.donGia = formatPrice(h.getDonGiaSauGiam());
        this.soLuong = h.getSoLuong();
        this.anhSPCT = h.getSanPhamChiTiet().getAnh();
        this.idSPCT = h.getSanPhamChiTiet().getId();
    }
}
