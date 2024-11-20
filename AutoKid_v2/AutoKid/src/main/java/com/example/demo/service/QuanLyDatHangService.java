package com.example.demo.service;

import com.example.demo.config.WebSocketHandler;
import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import com.example.demo.repository.HoaDonChiTietRepo;
import com.example.demo.repository.HoaDonRepo;
import com.example.demo.response.DonHangResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuanLyDatHangService {

    @Autowired
    WebSocketHandler webSocketHandler;

    @Autowired
    HoaDonRepo hoaDonRepo;

    @Autowired
    HoaDonChiTietRepo hdctRepo;

    // Tạo đơn hàng
    public void createHoaDon(HoaDon hoaDon) {
        hoaDonRepo.save(hoaDon);
        // Gui message cho client
        webSocketHandler.sendToClients("new order");
    }

    // Tạo đơn hàng chi tiết
    public void createHDCT(HoaDonChiTiet hdct){
        hdct.setHoaDon(hoaDonRepo.getHoaDon());
        hdctRepo.save(hdct);
    }

    public List<DonHangResponse> getDonHang(){
        List<DonHangResponse> list = new ArrayList<>();
        for (HoaDon h: hoaDonRepo.findAll()) {
            if(h.getTrangThaiHD().equals("Chờ xác nhận")) {
                list.add(new DonHangResponse(h));
            }
        }
        return list;
    }

    public Integer getIndex(){
        Integer i = 0;
        for (HoaDon h: hoaDonRepo.findAll()) {
            if(h.getTrangThaiHD().equals("Chờ xác nhận")) {
                i ++;
            }
        }
        return i;
    }

    // Hàm update trạng thái đơn hàng
    @Transactional
    public String updateStatusHD(String trangThai, Integer idHD) {
        hoaDonRepo.updateHoaDon(trangThai, idHD);
        return "success";
    }
}
