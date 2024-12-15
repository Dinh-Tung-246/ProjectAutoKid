package com.example.demo.service;

import com.example.demo.model.Voucher;
import com.example.demo.repository.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuanLyVoucherService {

    @Autowired
    private VoucherRepo voucherRepo;

    public List<Voucher> getAll(){
        return voucherRepo.findAll();
    }

    public void saveVoucher(Voucher voucher){
        voucherRepo.save(voucher);
    }

    public void updateVoucher(Voucher voucher) {
        voucherRepo.save(voucher);
    }

    public List<Voucher> searchVouchers(String keyword) {
        return voucherRepo.searchByKeyword(keyword);
    }

    public List<Voucher> getVouchersByStatus(Integer status) {
        return voucherRepo.findByTrangThai(status);
    }

    public void deleteVoucher(Integer id){
        voucherRepo.deleteById(id);
    }

    public Voucher findCode(String ma) {
        return voucherRepo.findByMa(ma);
    }

    public boolean checkMa(String ma){
        boolean result = true;
        for (Voucher v : voucherRepo.findAll()) {
            if (v.getMa().equals(ma.trim())){
                result = false;
                break;
            }
        }
        return result;
    }

    public double applyVoucher(Voucher voucher, double tongHoaDon){
        LocalDate now = LocalDate.now();

        if (voucher.getTrangThai() != 1) {
            throw new IllegalArgumentException("Đang không hoạt động nha !");
        }

        if(now.isBefore(voucher.getNgayBatDau()) || now.isAfter(voucher.getNgayKetThuc())) {
            throw new IllegalArgumentException("Voucher không nằm trong thời hạn áp dụng");
        }

        if(tongHoaDon < voucher.getDieuKien()) {
            throw new IllegalArgumentException("Tổng đơn hàng không đủ điều kiện");
        }

        double discout;
        if (voucher.getLoaiVoucher() == 1) {
            discout = tongHoaDon * (voucher.getGiaTri() / 100);
        } else {
            discout = voucher.getGiaTri();
        }

        if (voucher.getGiaTriToiDa() != null) {
            discout = Math.min(discout, voucher.getGiaTriToiDa());
        }
        return discout;
    }
}
