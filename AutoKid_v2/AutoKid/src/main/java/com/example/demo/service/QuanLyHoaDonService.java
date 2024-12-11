package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.repository.HoaDonChiTietRepo;
import com.example.demo.repository.HoaDonHistoryRepo;
import com.example.demo.repository.HoaDonRepo;
import com.example.demo.repository.SanPhamChiTietRepo;
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

    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;


    @Autowired
    KhachHangRepo khachHangRepo;

    public List<KhachHang> searchBySDT(String sdt) {
        return khachHangRepo.findBySDT(sdt);
    }

    public boolean updateProductQuantity(String maSPCT, Integer soLuong) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findByMaSPCT(maSPCT);
        if (sanPhamChiTiet != null) {
            if (soLuong > sanPhamChiTiet.getSoLuong()) {
                return false;
            }
            if (sanPhamChiTiet.getSoLuong() - soLuong < 0) {
                return false;
            }
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
            sanPhamChiTietRepo.save(sanPhamChiTiet);
            return true;
        } else {
            return false;
        }
    }

    public Optional<SanPhamChiTiet> findOptionalByMaSPCT(String maSPCT) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findByMaSPCT(maSPCT);
        return Optional.ofNullable(sanPhamChiTiet); // Bọc kết quả trong Optional
    }


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

    public List<HoaDonResponse> searchInvoices(String maHd) {
        List<HoaDonResponse> list= new ArrayList<>();
        for(HoaDon h: hoaDonRepo.searchInvoices(maHd)){
            list.add(new HoaDonResponse(h));
        }
        return list;
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
            String currentStatus = hoaDon.getTrangThaiHD();

            if (!isValidStatusTransition(currentStatus, newStatus)) {
                return false; // Không cho phép chuyển trạng thái
            }

            if (newStatus.equals("Huỷ đơn hàng")) {
                for (HoaDonChiTiet hoaDonChiTiet : hoaDon.getHoaDonChiTiets()) {
                    SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
                    sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
                    sanPhamChiTietRepo.save(sanPhamChiTiet);
                }
            }

            if (!currentStatus.equals(newStatus)) {
                hoaDon.setTrangThaiHD(newStatus);
                hoaDonRepo.save(hoaDon);
                return true;
            }
        }
        return false;
    }
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        return switch (currentStatus) {
            case "Đã thanh toán, chờ giao hàng" -> // Trạng thái 1
                    newStatus.equals("Hoàn thành") ||
                            newStatus.equals("Đã thanh toán, đang giao hàng") ||
                            newStatus.equals("Huỷ đơn hàng");
            case "Chưa thanh toán, chờ giao hàng" -> // Trạng thái 2
                    newStatus.equals("Hoàn thành") ||
                            newStatus.equals("Chưa thanh toán, đang giao hàng") ||
                            newStatus.equals("Huỷ đơn hàng"); // Trạng thái 3
            case "Hoàn thành", "Huỷ đơn hàng" -> // Trạng thái 6
                    false; // Không cho phép cập nhật
            // Trạng thái 4
            case "Đã thanh toán, đang giao hàng", "Chưa thanh toán, đang giao hàng" -> // Trạng thái 5
                    newStatus.equals("Hoàn thành") ||
                            newStatus.equals("Huỷ đơn hàng");
            default -> false; // Trạng thái không hợp lệ
        };
    }

    public List<HoaDonResponse> findAllByPending(){
        List<HoaDonResponse> list= new ArrayList<>();
        for(HoaDon h: hoaDonRepo.findAllByPending()){
            list.add(new HoaDonResponse(h));
        }
        return list;
    }

    public List<HoaDonResponse> findAllByInProgress(){
        List<HoaDonResponse> list= new ArrayList<>();
        for(HoaDon h: hoaDonRepo.findAllByInProgress()){
            list.add(new HoaDonResponse(h));
        }
        return list;
    }
    public List<HoaDonResponse> findAllByCompleted(){
        List<HoaDonResponse> list= new ArrayList<>();
        for(HoaDon h: hoaDonRepo.findAllByCompleted()){
            list.add(new HoaDonResponse(h));
        }
        return list;
    }
    public List<HoaDonResponse> findAllByCanceled(){
        List<HoaDonResponse> list= new ArrayList<>();
        for(HoaDon h: hoaDonRepo.findAllByCanceled()){
            list.add(new HoaDonResponse(h));
        }
        return list;
    }

    public List<HoaDonResponse> searchHoaDonPending(String maHd) {
        List<HoaDonResponse> list= new ArrayList<>();
        for(HoaDon h: hoaDonRepo.searchHoaDonPending(maHd)){
            list.add(new HoaDonResponse(h));
        }
        return list;
    }

    public List<HoaDonResponse> searchHoaDonInProgress(String maHd) {
        List<HoaDonResponse> list= new ArrayList<>();
        for(HoaDon h: hoaDonRepo.searchHoaDonInProgress(maHd)){
            list.add(new HoaDonResponse(h));
        }
        return list;
    }

    public List<HoaDonResponse> searchHoaDonCompleted(String maHd) {
        List<HoaDonResponse> list= new ArrayList<>();
        for(HoaDon h: hoaDonRepo.searchHoaDonCompleted(maHd)){
            list.add(new HoaDonResponse(h));
        }
        return list;
    }

    public List<HoaDonResponse> searchHoaDonCanceled(String maHd) {
        List<HoaDonResponse> list= new ArrayList<>();
        for(HoaDon h: hoaDonRepo.searchHoaDonCanceled(maHd)){
            list.add(new HoaDonResponse(h));
        }
        return list;
    }



}
