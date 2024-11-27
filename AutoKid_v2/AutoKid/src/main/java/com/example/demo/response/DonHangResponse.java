package com.example.demo.response;

import com.example.demo.model.HoaDon;
import lombok.Data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Data
public class DonHangResponse {
    private Integer idDH;
    private String maDH;
    private String tenNguoiNhan;
    private String sdtNguoiNhan;
    private String tongTien;
    private String trangThaiHD;

    public static String formatPrice(Float price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(price);
    }

    public DonHangResponse(HoaDon h) {
        this.idDH = h.getId();
        this.maDH = h.getMaHD();
        this.tenNguoiNhan = h.getTenNguoiNhan();
        this.sdtNguoiNhan = h.getSdtNguoiNhan();
        this.tongTien = formatPrice(h.getTongTien());
        this.trangThaiHD = h.getTrangThaiHD();
    }
}
