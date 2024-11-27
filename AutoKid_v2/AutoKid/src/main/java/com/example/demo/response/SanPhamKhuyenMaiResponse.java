package com.example.demo.response;

import com.example.demo.model.SanPham;
import com.example.demo.model.SanPhamChiTiet;
import lombok.Data;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;

@Data
public class SanPhamKhuyenMaiResponse {
    private Integer idSP;
    private String maSP;
    private String tenSP;
    private String giaNhap;
    private String donGia;
    private String loaiSP;
    private String anhSPMau;
    private String tenThuongHieu;
    private String kichCo;
    private String chatLieu;
    private String trangThai;
    private String maKM;
    private String tenKM;
    private String giaSauGiam;
    private String trangThaiKM;
    private Integer giaTriGiam;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    public static String formatPrice(Double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm cho phân cách hàng nghìn

        //Định dạng không có phần thập phân
        DecimalFormat formater = new DecimalFormat("#,###", symbols);
        return formater.format(price);
    }

    // Constructor
    public SanPhamKhuyenMaiResponse(SanPham s) {
        this.idSP = s.getId();
        this.maSP = s.getMaSP();
        this.tenSP = s.getTenSP();
        this.giaNhap = formatPrice(s.getGiaNhap());
        this.donGia = formatPrice(s.getDonGia());
        this.loaiSP = s.getLoaiSanPham().getTenLoai();
        this.anhSPMau = s.getAnhSPMau();
        this.tenThuongHieu = s.getThuongHieu().getTenTH();
        this.chatLieu = s.getChatLieu().getTenCl();
        this.kichCo = s.getKichCo().getTenKC();
        this.trangThai = s.getTrangThaiSP();
        if (s.getKhuyenMai() != null) {
            this.maKM = s.getKhuyenMai().getMaKM();
            this.tenKM = s.getKhuyenMai().getTenKM();
            this.giaSauGiam = formatPrice(s.getDonGia() - (s.getKhuyenMai().getGiaTri() * s.getDonGia()) / 100.00);
            this.ngayBatDau = s.getKhuyenMai().getNgayBatDau();
            this.ngayKetThuc = s.getKhuyenMai().getNgayKetThuc();
            this.giaTriGiam = s.getKhuyenMai().getGiaTri().intValue();
        } else {
            this.maKM = "Chưa áp mã";
            this.tenKM = "";
            this.giaSauGiam = "";
//            this.giaSauGiam = formatPrice(s.getDonGia());
        }
    }

}
