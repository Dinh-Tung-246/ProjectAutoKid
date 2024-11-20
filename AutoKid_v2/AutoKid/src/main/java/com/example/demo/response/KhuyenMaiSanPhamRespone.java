package com.example.demo.response;


public class KhuyenMaiSanPhamRespone {
    private Integer id;
    private String tenSP;
    private Double donGia;
    private Integer giaSauKhiGiam;
    private String maKM;

    // Constructor
    public KhuyenMaiSanPhamRespone(Integer id, String tenSP, Double donGia, Integer giaSauKhiGiam, String maKM) {
        this.id = id;
        this.tenSP = tenSP;
        this.donGia = donGia;
        this.giaSauKhiGiam = giaSauKhiGiam;
        this.maKM = maKM;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public String getTenSP() {
        return tenSP;
    }

    public Double getDonGia() {
        return donGia;
    }

    public Integer getGiaSauKhiGiam() {
        return giaSauKhiGiam;
    }

    public String getMaKM() {
        return maKM;
    }
}
