package com.example.demo.service;

import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import com.example.demo.model.HoaDonHistory;
import com.example.demo.repository.HoaDonChiTietRepo;
import com.example.demo.repository.HoaDonHistoryRepo;
import com.example.demo.repository.HoaDonRepo;
import com.example.demo.response.HoaDonResponse;
import com.example.demo.response.HoadonhistoryRespone;
import com.example.demo.response.hoadonchitietRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuanLyHoaDonService {
    @Autowired
    private HoaDonRepo hoaDonRepo;

    @Autowired
    private HoaDonHistoryRepo hoaDonHistoryRepo;

    @Autowired
    private HoaDonChiTietRepo hoaDonChiTietRepo;

    public List<HoaDonResponse> fillAllHoaDon(){
        List<HoaDonResponse> list= new ArrayList<>();
        for(HoaDon h: hoaDonRepo.findAll()){
            list.add(new HoaDonResponse(h));
        }
        return list;
    }

    // Tạo hóa đơn
    public HoaDon createHoaDon(HoaDon hoaDon) {
        return hoaDonRepo.save(hoaDon);
    }

    // Tạo chi tiết hóa đơn
    public void createHoaDonChiTiet(HoaDonChiTiet hoaDonChiTiet) {
        hoaDonChiTietRepo.save(hoaDonChiTiet);
    }

    public List<HoaDon> searchInvoices(String maHd) {
        List<HoaDon> hoaDons = hoaDonRepo.searchInvoices(maHd);
        return hoaDons;
    }

    public HoaDon findHoaDonByMaHD(Integer maHD) {
        return hoaDonRepo.findById(maHD).orElse(null);
    }

    public List<HoaDonChiTiet> findHoaDonChiTietByMaHD(Integer maHD) {
        return hoaDonChiTietRepo.findAllByHoaDonId(maHD);
    }

    public List<HoadonhistoryRespone> fillAllHoaDonHistory(){
        List<HoadonhistoryRespone> listhdh= new ArrayList<>();
        for(HoaDonHistory hds: hoaDonHistoryRepo.findAll()){
            listhdh.add(new HoadonhistoryRespone(hds));
        }
        return listhdh;
    }
    public List<hoadonchitietRespone> fillAllHoaDonChiTiet(){
        List<hoadonchitietRespone> listhdct= new ArrayList<>();
        for(HoaDonChiTiet hdct: hoaDonChiTietRepo.findAll()){
            listhdct.add(new hoadonchitietRespone(hdct));
        }
        return listhdct;
    }

    public boolean updateHoaDonStatus(Integer maHD, String newStatus) {
        Optional<HoaDon> hoaDonOptional = hoaDonRepo.findById(maHD);
        if (hoaDonOptional.isPresent()) {
            HoaDon hoaDon = hoaDonOptional.get();
            if (!hoaDon.getTrangThaiHD().equals(newStatus)) {
                hoaDon.setTrangThaiHD(newStatus);
                hoaDonRepo.save(hoaDon);
                return true;
            }
        }
        return false;
    }


}
