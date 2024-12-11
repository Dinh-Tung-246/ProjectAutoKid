package com.example.demo.response;

import com.example.demo.model.Voucher;
import lombok.Data;

import java.text.DecimalFormat;

@Data
public class VoucherResponse {

    private int id;
    private String ma;
    private String ten;
    private int loaiVoucher;
    private String giaTri;
    private String giaTriToiDa;
    private String dieuKien;
    private String ngayBatDau;
    private String ngayKetThuc;
    private int trangThai;

    public VoucherResponse(Voucher voucher) {
        this.id = voucher.getId();
        this.ma = voucher.getMa();
        this.ten = voucher.getTen();
        this.loaiVoucher = voucher.getLoaiVoucher();
        this.dieuKien = formatCurrency(voucher.getDieuKien());
        this.giaTri = formatCurrency(voucher.getGiaTri());
        this.giaTriToiDa = formatCurrency(voucher.getGiaTriToiDa());
        this.ngayBatDau = voucher.getNgayBatDau().toString();
        this.ngayKetThuc = voucher.getNgayKetThuc().toString();
        this.trangThai = voucher.getTrangThai();
    }

    private String formatCurrency(Double value) {
        if (value == null) {
            return "Trống";
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(value);
    }
}
