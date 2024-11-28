package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.repository.SanPhamRepo;
import com.example.demo.response.SanPhamKhuyenMaiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuanLySanPhamService {
    @Autowired
    SanPhamChiTietRepo spctRepo;

    @Autowired
    SanPhamRepo sanPhamRepo;

    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;

    @Autowired
    ThuongHieuRepo thuongHieuRepo;

    @Autowired
    MauSacRepo mauSacRepo;

    @Autowired
    KichCoRepo kichCoRepo;

    @Autowired
    ChatLieuRepo chatLieuRepo;

    @Autowired
    LoaiSanPhamRepo loaiSanPhamRepo;

    public List<SanPham> DSSanPham(){
        return sanPhamRepo.findAllByOrderByIdDesc();
    }

    public boolean isMaSPExist(String maSP) {
        return sanPhamRepo.existsByMaSP(maSP);
    }
    public boolean isTenSPExist(String tenSP) {
        return sanPhamRepo.existsByTenSP(tenSP);
    }
    public void addSanPham(SanPham sanPham){
        sanPhamRepo.save(sanPham);
    }

    public void updateSanPham(SanPham sanPham){
        sanPhamRepo.save(sanPham);
    }

    public List<SanPhamChiTiet> getAllSanPham(){
        return sanPhamChiTietRepo.findAllByOrderByIdDesc();
    }

    public boolean isMaSPCTExist(String maSPCT) {
        return sanPhamChiTietRepo.existsByMaSPCT(maSPCT);
    }
    public void addSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTietRepo.save(sanPhamChiTiet);
    }

    public void updateSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet){
        sanPhamChiTietRepo.save(sanPhamChiTiet);
    }
    public List<ThuongHieu> getAllThuongHieu(){
        return thuongHieuRepo.findAllByOrderByIdDesc();
    }

    public void AddThuongHieu(ThuongHieu thuongHieu){
        thuongHieuRepo.save(thuongHieu);
    }
    public boolean isMaTHExist(String maTH) {
        return thuongHieuRepo.existsByMaTH(maTH);
    }
    public boolean isTenTHExist(String tenTH) {
        return thuongHieuRepo.existsByTenTH(tenTH);
    }
    public void updateThuongHieu(ThuongHieu thuongHieu){thuongHieuRepo.save(thuongHieu);}

    public List<ThuongHieu> searchTH(String tenTH){
        return  thuongHieuRepo.searchThuongHieu("%" + tenTH + "%");
    }

    public List<MauSac> getAllMauSac(){
        return mauSacRepo.findAll();
    }

    public void addMauSac(MauSac mauSac){
        mauSacRepo.save(mauSac);
    }

    public boolean isMaMSExist(String maMS) {
        return mauSacRepo.existsByMaMS(maMS);
    }
    public boolean isTenMSExist(String tenMS) {
        return mauSacRepo.existsByTenMS(tenMS);
    }
    public void updateMauSac(MauSac mauSac){mauSacRepo.save(mauSac);}

    public void deleteMauSac(Integer id){
        mauSacRepo.deleteById(id);
    }

    public void updateChatLieu(ChatLieu chatLieu){chatLieuRepo.save(chatLieu);}

    public void detailMauSac(Integer id){
        mauSacRepo.findById(id);
    }

    public List<KichCo> getAllKichCo(){
        return kichCoRepo.findAllByOrderByIdDesc();
    }

    public void addKichCo(KichCo kichCo){
        kichCoRepo.save(kichCo);
    }

    public boolean isMaKCExist(String maKC) {
        return kichCoRepo.existsByMaKC(maKC);
    }
    public boolean isTenKCExist(String tenKC) {
        return kichCoRepo.existsByTenKC(tenKC);
    }
    public void updateKichCo(KichCo kichCo){kichCoRepo.save(kichCo);}

    public void deleteKichCo(Integer id){
        kichCoRepo.deleteById(id);
    }

    public List<ChatLieu> getAllChatLieu(){
        return chatLieuRepo.findAllByOrderByIdDesc();
    }

    public void addChatLieu(ChatLieu chatLieu){
        chatLieuRepo.save(chatLieu);
    }

    public boolean isMaCLExist(String maCl) {
        return chatLieuRepo.existsByMaCl(maCl);
    }
    public boolean isTenCLExist(String tenCl) {
        return chatLieuRepo.existsByTenCl(tenCl);
    }
    public void deleteChatLieu(Integer id){
        chatLieuRepo.deleteById(id);
    }

    public void addLoaiSanPham(LoaiSanPham loaiSanPham){
        loaiSanPhamRepo.save(loaiSanPham);
    }

    public void uodateLoaiSanPham(LoaiSanPham loaiSanPham){loaiSanPhamRepo.save(loaiSanPham);}

    public boolean isMaLSPExist(String maLSP) {
        return loaiSanPhamRepo.existsByMaLSP(maLSP);
    }
    public boolean isTenLSPExist(String tenLoai) {
        return loaiSanPhamRepo.existsByTenLoai(tenLoai);
    }

    public List<LoaiSanPham> getAllLoaiSanPham(){
        return loaiSanPhamRepo.findAll();
    }

    public List<SanPhamKhuyenMaiResponse> getAllSP(){
        List<SanPhamKhuyenMaiResponse> list = new ArrayList<>();
        for(SanPham sp: sanPhamRepo.findAll()){
            if (sp.getTrangThaiSP().equals("Đang bán")) {
                list.add(new SanPhamKhuyenMaiResponse(sp));
            }
        }
        return list;
    }

    public SanPhamKhuyenMaiResponse getById(Integer idSP){
        SanPhamKhuyenMaiResponse sp = null;
        for(SanPham s: sanPhamRepo.findAll()){
            if(s.getId() == idSP){
                sp = new SanPhamKhuyenMaiResponse(s);
                break;
            }
        }
        System.out.println(sp.getMaSP());
        return sp;
    }

    public List<SanPhamKhuyenMaiResponse> getSPKM () {
        List<SanPhamKhuyenMaiResponse> list = new ArrayList<>();
        for (SanPham sp: sanPhamRepo.findAll()) {
            if(sp.getKhuyenMai() != null) {
                list.add(new SanPhamKhuyenMaiResponse(sp));
            }
        }
        return list;
    }

    public Integer countSP() {
        Integer count = 0;
        for (SanPham sp: sanPhamRepo.findAll()){
            count ++;
        }
        return count;
    }

    // Danh sách sản phẩm đề xuất
    public List<SanPhamKhuyenMaiResponse> getAllRelatedProduct(Integer idSP){
        SanPham sp = sanPhamRepo.findById(idSP).orElseThrow();

        List<SanPhamKhuyenMaiResponse> list = new ArrayList<>();
        for (SanPham s: sanPhamRepo.findAll()){
            if( (s.getLoaiSanPham().getIdLoaiSP() == sp.getLoaiSanPham().getIdLoaiSP() //sản phẩm cùng loại sản phẩm
                    || s.getChatLieu().getId() == sp.getChatLieu().getId() // Sản phẩm cùng chất liệu
                    || s.getKichCo().getId() == sp.getKichCo().getId()) // sản phẩm cùng kích cỡ
                    && s.getTrangThaiSP().equals("Đang bán")){ // các sản phẩm này vẫn đnag được kinh doanh
                list.add(new SanPhamKhuyenMaiResponse(s));
            }
        }
        return list;
    }

    // Bắt đầu Lọc theo giá
    public List<SanPhamKhuyenMaiResponse> searchSPByPrice(Double gia1, Double gia2){
        List<SanPhamKhuyenMaiResponse> list = new ArrayList<>();
        for(SanPham sp: sanPhamRepo.searchByPrice(gia1, gia2)){
            if (sp.getTrangThaiSP().equals("Đang bán")) {
                list.add(new SanPhamKhuyenMaiResponse(sp));
            }
        }
        return list;
    }

    public List<SanPhamKhuyenMaiResponse> filterByPrice(Integer value){
        if(value == 1){
            return searchSPByPrice(200000.0, 500000.0);
        } else if(value == 2) {
            return  searchSPByPrice(500000.0, 1000000.0);
        } else if(value == 3) {
            return searchSPByPrice(1000000.0, 1500000.0);
        } else if(value == 4) {
            return searchSPByPrice(1500000.0, 2500000.0);
        } else if(value == 5) {
            return searchSPByPrice(2500000.0, 500000.0);
        } else return getAllSP();
    }
    // Kết thúc Lọc theo giá

    // Lọc theo Thương Hiệu
    public List<SanPhamKhuyenMaiResponse> searchSPByBrands(List<Integer> list) {
        List<SanPhamKhuyenMaiResponse> l = new ArrayList<>();
        for(SanPham sp: sanPhamRepo.searchByBrands(list)) {
            if (sp.getTrangThaiSP().equals("Đang bán")) {
                l.add(new SanPhamKhuyenMaiResponse(sp));
            }
        }
        return l;
    }

    // Lọc theo Loại Sản Phẩm
    public List<SanPhamKhuyenMaiResponse> searchByLoai(Integer idLoai) {
        List<SanPhamKhuyenMaiResponse> l = new ArrayList<>();
        for(SanPham sp: sanPhamRepo.findAll()) {
            if(sp.getLoaiSanPham().getIdLoaiSP() == idLoai && sp.getTrangThaiSP().equals("Đang bán")) {
                l.add(new SanPhamKhuyenMaiResponse(sp));
            }
        }
        return l;
    }

    public List<SanPham> filterSP(String filter) {
        List<SanPham> sanPhams = sanPhamRepo.findAllSanPham();

        if ("asc".equalsIgnoreCase(filter)) {
            sanPhams.sort(Comparator.comparing(SanPham::getDonGia));
        } else if ("desc".equalsIgnoreCase(filter)) {
            sanPhams.sort(Comparator.comparing(SanPham::getDonGia).reversed());
        }
        return sanPhams;
    }

    public List<SanPhamKhuyenMaiResponse> filterAllSP(String filter){
        List<SanPhamKhuyenMaiResponse> list = new ArrayList<>();
        for(SanPham sp: filterSP(filter)) {
            if (sp.getTrangThaiSP().equals("Đang bán")) {
                list.add(new SanPhamKhuyenMaiResponse(sp));
            }
        }
        return list;
    }

    // Search product by name
    public List<SanPhamKhuyenMaiResponse> searchProductByName(String Name) {
        List<SanPhamKhuyenMaiResponse> list = new ArrayList<>();
        for (SanPham sp: sanPhamRepo.findAllByName(Name)) {
            if(sp.getTrangThaiSP().equals("Đang bán")) {
                list.add(new SanPhamKhuyenMaiResponse(sp));
            }
        }
        return list;
    }


    // Lấy ra các màu mà sản phẩm có
    public List<Map<String, Object>> getColorOfSP(Integer idSP) {
        List<Map<String, Object>> listColor = new ArrayList<>();
        SanPham sanPham = sanPhamRepo.findById(idSP).orElseThrow();
        if (sanPham.getSanPhamChiTiets().size() != 0) {
            for (SanPhamChiTiet spct: sanPham.getSanPhamChiTiets()){
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("idSPCT",spct.getId());
                map.put("idMS", spct.getMauSac().getId());
                map.put("tenMS", spct.getMauSac().getTenMS());
                map.put("soLuong", spct.getSoLuong());
                listColor.add(map);
            }
        }

        return listColor;
    }
}
