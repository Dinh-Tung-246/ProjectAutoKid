package com.example.demo.response;

import com.example.demo.model.HoaDonHistory;
import lombok.Data;

import java.sql.Date;

@Data

public class HoadonhistoryRespone {
    private Integer id;
    private String maHD;
    private Date ngayThayDoi;
    private Date ngayTao;
    private String trangThai;

    public HoadonhistoryRespone(HoaDonHistory hds){
        this.id = hds.getId();
        this.maHD = hds.getHoaDon().getMaHD();
        this.ngayThayDoi = hds.getNgayThayDoi();
        this.ngayTao = hds.getHoaDon().getNgayTao();
        this.trangThai = hds.getTrangThai();
    }
}