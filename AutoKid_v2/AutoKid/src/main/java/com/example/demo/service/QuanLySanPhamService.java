package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.repository.SanPhamRepo;
import com.example.demo.response.SanPhamKhuyenMaiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    public void addSanPham(SanPham sanPham){
        sanPhamRepo.save(sanPham);
    }

    public void updateSanPham(SanPham sanPham){
        sanPhamRepo.save(sanPham);
    }

    public List<SanPhamChiTiet> getAllSanPham(){
        return sanPhamChiTietRepo.findAllByOrderByIdDesc();
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

    public void deleteChatLieu(Integer id){
        chatLieuRepo.deleteById(id);
    }

    public void addLoaiSanPham(LoaiSanPham loaiSanPham){
        loaiSanPhamRepo.save(loaiSanPham);
    }

    public void uodateLoaiSanPham(LoaiSanPham loaiSanPham){loaiSanPhamRepo.save(loaiSanPham);}

    public List<LoaiSanPham> getAllLoaiSanPham(){
        return loaiSanPhamRepo.findAll();
    }

    public List<SanPhamKhuyenMaiResponse> getAllSP(){
        List<SanPhamKhuyenMaiResponse> list = new ArrayList<>();
        for(SanPham sp: sanPhamRepo.findAll()){
            list.add(new SanPhamKhuyenMaiResponse(sp));
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

    public List<SanPhamKhuyenMaiResponse> getAllRelatedProduct(Integer idSP){
        SanPhamKhuyenMaiResponse sp = null;
        for(SanPham s: sanPhamRepo.findAll()){
            if(s.getId() == idSP){
                sp = new SanPhamKhuyenMaiResponse(s);
                break;
            }
        }
        Integer idSPKM = sp.getIdSP();
        List<SanPhamKhuyenMaiResponse> list = new ArrayList<>();
        for (SanPham s: sanPhamRepo.findAll()){
            if(s.getId() == idSPKM){
                list.add(new SanPhamKhuyenMaiResponse(s));
            }
        }
        return list;
    }

    // Bắt đầu Lọc theo giá
    public List<SanPhamKhuyenMaiResponse> searchSPByPrice(Double gia1, Double gia2){
        List<SanPhamKhuyenMaiResponse> list = new ArrayList<>();
        for(SanPham sp: sanPhamRepo.searchByPrice(gia1, gia2)){
            list.add(new SanPhamKhuyenMaiResponse(sp));
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
            l.add(new SanPhamKhuyenMaiResponse(sp));
        }
        return l;
    }

    // Lọc theo Loại Sản Phẩm
    public List<SanPhamKhuyenMaiResponse> searchByLoai(Integer idLoai) {
        List<SanPhamKhuyenMaiResponse> l = new ArrayList<>();
        for(SanPham sp: sanPhamRepo.findAll()) {
            if(sp.getLoaiSanPham().getIdLoaiSP() == idLoai) {
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
            list.add(new SanPhamKhuyenMaiResponse(sp));
        }
        return list;
    }
}
