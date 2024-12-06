package com.example.demo.service;

import com.example.demo.model.Voucher;
import com.example.demo.repository.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuanLyVoucherService {

    @Autowired
    private VoucherRepo voucherRepo;

    public List<Voucher> getAll(){
        return voucherRepo.findAll();
    }
}
