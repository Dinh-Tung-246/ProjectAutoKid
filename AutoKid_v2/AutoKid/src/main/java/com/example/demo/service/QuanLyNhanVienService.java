package com.example.demo.service;

import com.example.demo.model.NhanVien;
import com.example.demo.repository.ChucVuRepo;
import com.example.demo.repository.NhanVienRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuanLyNhanVienService {
    @Autowired
    private NhanVienRepo nhanVienRepo;

    @Autowired
    private ChucVuRepo chucVuRepo;

    public List<NhanVien> findAllStaff() {
        return nhanVienRepo.findAll();
    }

    public void save(NhanVien nhanVien) {
        nhanVienRepo.save(nhanVien);
    }

    public List<NhanVien> sreachStaff(String keyword){
        return nhanVienRepo.findByTenNVContainingOrMaNVContaining(keyword,keyword);
    }
    public void delete(Integer id) {
        if (nhanVienRepo.existsById(id)) {
            nhanVienRepo.deleteById(id);
        } else {
            System.out.println("Không tìm thấy nhân viên với ID: " + id);
        }
    }

    public NhanVien findStaffbyId(Integer id) {
        return nhanVienRepo.findById(id).orElse(null);
    }
}
