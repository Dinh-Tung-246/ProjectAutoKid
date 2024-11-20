package com.example.demo.response;

import com.example.demo.model.KhachHang;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KhachHangResponse {
    private Integer idKH;
    private String tenKH;
    private String emailKH;
    private String sdtKH;
    private String diaChiKH;
    private String maTTVC;
    private String diaChiNhan;
    private String tenNguoiNhan;
    private String sdtNguoiNhan;
    private String matKhau;

    public KhachHangResponse(KhachHang k){
        this.idKH = k.getId();
        this.tenKH = k.getTenKH();
        this.emailKH = k.getEmail();
        this.sdtKH = k.getSdt();
        this.diaChiKH = k.getDiaChi();
        if(k.getThongTinVanChuyen() != null){
            this.tenNguoiNhan = k.getThongTinVanChuyen().getTenNguoiNhan();
            this.sdtNguoiNhan = k.getThongTinVanChuyen().getSdt();
            this.diaChiNhan = k.getThongTinVanChuyen().getDiaChi();
            this.maTTVC = k.getThongTinVanChuyen().getMaTTVC();
        } else {
            this.tenNguoiNhan = this.sdtNguoiNhan = this.diaChiNhan = this.maTTVC = null;
        }
        this.matKhau = k.getMatKhau();
    }
}
