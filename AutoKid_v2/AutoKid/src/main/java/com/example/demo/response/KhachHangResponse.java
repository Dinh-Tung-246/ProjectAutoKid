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
    private String matKhau;

    public KhachHangResponse(KhachHang k){
        this.idKH = k.getId();
        this.tenKH = k.getTenKH();
        this.emailKH = k.getEmail();
        this.sdtKH = k.getSdt();
        this.diaChiKH = k.getDiaChi();
        this.matKhau = k.getMatKhau();
    }
}
