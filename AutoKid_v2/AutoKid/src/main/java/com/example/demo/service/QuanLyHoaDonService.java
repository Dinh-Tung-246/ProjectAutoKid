package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
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


    public List<HoaDon> getHoaDon(){
        return hoaDonRepo.findAll();
    }

    public void deleteInvoiceDetails(Integer id){
        hoaDonChiTietRepo.deleteById(id);
    }

    public void deleteInvoice(Integer id){
        hoaDonRepo.deleteById(id);
    }
    public List<HoaDon> getUnpaidHoaDons() {
        // Truy vấn cơ sở dữ liệu để lấy các hóa đơn có trạng thái "Chưa thanh toán"
        return hoaDonRepo.findByTrangThaiHD("Chưa thanh toán");
    }

    public HoaDon createInvoice(HoaDon hoaDon) {
        HoaDon createdInvoice = hoaDonRepo.save(hoaDon); // Lưu hóa đơn

        // Tạo hóa đơn chi tiết trống
        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        hoaDonChiTiet.setHoaDon(createdInvoice); // Liên kết hóa đơn chi tiết với hóa đơn mới tạo
        hoaDonChiTiet.setSanPhamChiTiet(null); // Không có sản phẩm chi tiết
        hoaDonChiTiet.setSoLuong(0); // Không có số lượng
        hoaDonChiTiet.setDonGia(0.0); // Không có đơn giá
        hoaDonChiTiet.setDonGiaSauGiam(0.0); // Không có giá sau giảm

        hoaDonChiTietRepo.save(hoaDonChiTiet); // Lưu hóa đơn chi tiết vào cơ sở dữ liệu

        return createdInvoice; // Trả về hóa đơn vừa tạo
    }



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
