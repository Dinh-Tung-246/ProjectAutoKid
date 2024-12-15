package com.example.demo.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
    public class SanPhamChiTietDTO {
        private String maSPCT;
        private String tenSP;
        private Double donGia;
        private Integer soLuong;
        private String chatLieu;
        private String thuongHieu;
        private String mauSac;
        private String kichCo;
    }

