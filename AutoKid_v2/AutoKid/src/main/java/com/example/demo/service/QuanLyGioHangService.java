package com.example.demo.service;

import com.example.demo.model.GioHang;
import com.example.demo.model.GioHangChiTiet;
import com.example.demo.model.KhuyenMai;
import com.example.demo.model.SanPhamChiTiet;
import com.example.demo.repository.GioHangChiTietRepo;
import com.example.demo.repository.GioHangRepo;
import com.example.demo.repository.SanPhamChiTietRepo;
import com.example.demo.response.GioHangChiTietResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuanLyGioHangService {
    private static Logger logger = LoggerFactory.getLogger(QuanLyGioHangService.class);

    @Autowired
    GioHangRepo gioHangRepo;

    @Autowired
    GioHangChiTietRepo ghctRepo;

    @Autowired
    SanPhamChiTietRepo spctRepo;

    private static String formatPrice(Double price){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm cho phân cách hàng nghìn

        //Định dạng không có phần thập phân
        DecimalFormat formater = new DecimalFormat("#,###", symbols);
        return formater.format(price);
    }

    // lấy danh sách giỏ hàng theo idKH
    public List<GioHangChiTietResponse> getGioHang(Integer idKH) {
        List<GioHangChiTietResponse> list = new ArrayList<>();
        GioHang gh = gioHangRepo.getAllGHByKH(idKH);//
        if (gh.getGioHangChiTiets().size() != 0) {
            for (GioHangChiTiet ghct: gh.getGioHangChiTiets()) {
                list.add(new GioHangChiTietResponse(ghct));
            }
        }
        return list;
    }

    // lấy số lượng sản phẩm tổng thể trong giỏ hàng theo idKH
    public Integer getSoLuongSPCTInCart(Integer idKH) {
        Integer count = 0;
        GioHang gh = gioHangRepo.getAllGHByKH(idKH);//
        if (gh.getGioHangChiTiets().size() != 0) {
            for (GioHangChiTiet ghct: gh.getGioHangChiTiets()){
                count += ghct.getSoLuong();
            }
        }
        return count;
    }

    // Lấy ra tổng giá tiền
    public String getTotalPrice(Integer idKH){
        Double totalPrice = 0.0;
        GioHang gh = gioHangRepo.getAllGHByKH(idKH);
        if (gh.getGioHangChiTiets().size() != 0) {
            for (GioHangChiTiet ghct: gh.getGioHangChiTiets()) {
                totalPrice += ghct.getDonGia();
            }
        }

        return formatPrice(totalPrice);
    }

    // Thêm sẩn phẩm vào giỏ hàng
    public void addToCart(Integer idKh, Integer idSPCT, Integer soLuong) {
        GioHang gh = gioHangRepo.getAllGHByKH(idKh); // lay gio hang theo idKH
        Integer idGH = gh.getIdGioHang();
        Double giaSauGiam = null;
        Double donGia = spctRepo.findById(idSPCT).orElseThrow().getSanPham().getDonGia();
        KhuyenMai khuyenMai = spctRepo.findById(idSPCT).orElseThrow().getSanPham().getKhuyenMai();
        if (khuyenMai != null) {
            giaSauGiam = donGia * khuyenMai.getGiaTri() / 100;
        } else {
            giaSauGiam = donGia;
        }
        int i = 0;
        logger.info("idGH: {}", idGH);
        logger.info("idSPCT: {}", idSPCT);
        // Đã có sản phẩm trong giỏ hàng
        for (GioHangChiTiet ghct: ghctRepo.getAllGHCTByIdKH(idKh)) {
            if (ghct.getSanPhamChiTiet().getId() == idSPCT) {
                i = 1;
            }
        }
        if (i == 1) { // sản phẩm đã có trong giỏ hàng
            Integer soLuongSPCT = ghctRepo.getGHCT(idKh, idSPCT).getSoLuong();
            Integer sl = soLuongSPCT + soLuong;
            ghctRepo.updateSoLuongSPCT(sl, sl * giaSauGiam, idGH, idSPCT);
        }
        else { // Chưa có sản phẩm trong giỏ thì sẽ thêm mới
            GioHangChiTiet ghct = new GioHangChiTiet();
            ghct.setGioHang(gioHangRepo.findById(idGH).orElseThrow());
            ghct.setSanPhamChiTiet(spctRepo.findById(idSPCT).orElseThrow());
            ghct.setSoLuong(soLuong);
            ghct.setDonGia(giaSauGiam);
            ghctRepo.save(ghct);
        }
    }

    // Thay doi so luong trong gio
    public void updateSoLuongSPCT(Integer idKH, Integer idSPCT, Integer soLuong) {
        GioHangChiTiet ghct = ghctRepo.getGHCT(idKH, idSPCT);
        SanPhamChiTiet spct = spctRepo.findById(idSPCT).orElseThrow();
        Double donGia = spct.getSanPham().getDonGia() * soLuong;
        ghctRepo.updateSoLuongSPCTinGHCT(soLuong, donGia, ghct.getIdGioHangChiTiet());
    }

    public void removeGHCT(Integer idKH, Integer idSPCT) {
        GioHangChiTiet ghct = ghctRepo.getGHCT(idKH, idSPCT);
        ghctRepo.delete(ghct);
    }

    public Map<String, Object> checkSoLuong(Integer idSPCT, Integer soLuong) {
        Map<String, Object> map = new LinkedHashMap<>();
        SanPhamChiTiet spct = spctRepo.findById(idSPCT).orElseThrow();
        if(spct.getSoLuong() < soLuong) {
            map.put("result", "Sản phẩm " + spct.getSanPham().getTenSP() + "(" + spct.getMauSac().getTenMS() + ") " + "vượt quá số lượng trong kho");
        } else {
            map.put("result", "ok");
        }
        return map;
    }
    // Xóa sản phẩm khỏi giỏ hoặc giảm số lượng
//    public void reduceSPCT(Integer idGHCT, Integer soLuong) {
//        if(soLuong != null) { // giảm số lượng sản phẩm trong giỏ
//            ghctRepo.updateSoLuongSPCT(soLuong, idGHCT);
//        } else {
//            ghctRepo.delete(ghctRepo.findById(idGHCT).orElseThrow());
//        }
//    }
}
