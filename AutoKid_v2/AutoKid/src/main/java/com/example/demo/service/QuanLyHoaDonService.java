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

    public boolean updateHoaDonStatus(String maHD, String newStatus) {
        Optional<HoaDon> hoaDonOptional = hoaDonRepo.findHoaDonByMaHD(maHD);
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
//    public Page<HoaDon> getAll(int pageNo, int pageSize){
//        Pageable pageable = PageRequest.of(pageNo,pageSize);
//        return hoaDonRepo.findAll(pageable);
//    }

}
