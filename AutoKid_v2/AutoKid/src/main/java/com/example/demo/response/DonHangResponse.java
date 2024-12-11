package com.example.demo.response;

import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import lombok.Data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

@Data
public class DonHangResponse {
    private Integer idDH;
    private String maDH;
    private Integer idKH;
    private String tenNguoiNhan;
    private String sdtNguoiNhan;
    private String diaChiNhan;
    private String tongTien;
    private String ngayMuaHang;
    private String trangThaiDH;
    private List<DonHangChiTietResponse> listdhct;

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
        this.trangThaiDH = h.getTrangThaiHD();
        this.ngayMuaHang = h.getNgayTao().toString();
        this.diaChiNhan = h.getDiaChiNguoiNhan();

        if (h.getKhachHang() != null) {
            this.idKH = h.getKhachHang().getId();
        }
        try {
            List<DonHangChiTietResponse> list = new ArrayList<>();
            for (HoaDonChiTiet hdct: h.getHoaDonChiTiets()){
                list.add(new DonHangChiTietResponse(hdct));
            }
            this.listdhct = list;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
