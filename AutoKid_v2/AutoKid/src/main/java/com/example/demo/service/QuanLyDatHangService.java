package com.example.demo.service;

import com.example.demo.config.WebSocketHandler;
import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import com.example.demo.model.KhachHang;
import com.example.demo.repository.HoaDonChiTietRepo;
import com.example.demo.repository.HoaDonRepo;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.SanPhamChiTietRepo;
import com.example.demo.response.DonHangResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuanLyDatHangService {

    private Logger logger = LoggerFactory.getLogger(QuanLyDatHangService.class);

    @Autowired
    WebSocketHandler webSocketHandler;

    @Autowired
    HoaDonRepo hoaDonRepo;

    @Autowired
    HoaDonChiTietRepo hdctRepo;

    @Autowired
    SanPhamChiTietRepo spctRepo;

    @Autowired
    KhachHangRepo khachHangRepo;

    // Tạo đơn hàng
    public void createHoaDon(HoaDon hoaDon) {
        hoaDonRepo.save(hoaDon);
        // Gui message cho client
        webSocketHandler.sendToClients("new order");
    }

    // Tạo đơn hàng chi tiết đồng thời cập nhật lại số lượng hàng trong kho
    @Transactional
    public void createHDCT(HoaDonChiTiet hdct, Integer soLuong, Integer idSPCT){
        spctRepo.updateSoLuongSPCT(soLuong, idSPCT);
        hdct.setHoaDon(hoaDonRepo.getHoaDon());
        hdctRepo.save(hdct);
    }

    // Thông báo các đơn hàng online cho admin
    public List<DonHangResponse> getDonHang(){
        List<DonHangResponse> list = new ArrayList<>();
        for (HoaDon h: hoaDonRepo.findAll()) {
            if(h.getTrangThaiHD().contains("chờ giao hàng")) {
                list.add(new DonHangResponse(h));
            }
        }
        return list;
    }

    public Integer getIndex(){
        Integer i = 0;
        for (HoaDon h: hoaDonRepo.findAll()) {
            if(h.getTrangThaiHD().contains("chờ giao hàng")) {
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

    // Lấy ra danh sách đơn hàng theo khách hàng
    public List<DonHangResponse> getDonHangOfKH(Integer idKH) {
        List<DonHangResponse> list = new ArrayList<>();

        for (HoaDon h: hoaDonRepo.getHDByIdKH(idKH)) {
            list.add(new DonHangResponse(h));
        }

        return list;
    }

    // lấy ra chi tiết của từng Đơn hàng
    public DonHangResponse getDetailDonHang(Integer idDH) {
        HoaDon hd = hoaDonRepo.findById(idDH).orElseThrow();
        DonHangResponse dhRes = new DonHangResponse(hd);
        return dhRes;
    }
}
