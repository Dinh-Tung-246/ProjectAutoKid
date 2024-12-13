package com.example.demo.controller;

import com.example.demo.model.HoaDon;
import com.example.demo.repository.HoaDonRepo;
import com.example.demo.repository.LoaiSanPhamRepo;
import com.example.demo.response.DonHangResponse;
import com.example.demo.service.QuanLyDatHangService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/autokid/account")
public class AutoKidAccountController {
    private static Logger logger = LoggerFactory.getLogger(AutoKidAccountController.class);

    @Autowired
    LoaiSanPhamRepo loaiSanPhamRepo;

    @Autowired
    QuanLyDatHangService qldhService;

    @Autowired
    HoaDonRepo hoaDonRepo;

    @GetMapping("")
    public String get(Model model){
        model.addAttribute("loaisp", loaiSanPhamRepo.findAll());
        return "/autokid/updateAccount";
    }

    @GetMapping("/order-tracking")
    public String show(@RequestParam Integer idKH, Model model){
        int i = 1;
        if (qldhService.getDonHangOfKH(idKH).size() == 0) {
            i = 0;
        }
        model.addAttribute("isDHExist", i);
        return "/autokid/order-tracking";
    }

    @PostMapping("/show-tracking")
    @ResponseBody
    public List<DonHangResponse> showTableDH(@RequestBody Map<String, Object> payload) {
        Integer idKH = Integer.parseInt(payload.get("idKH").toString());
        return qldhService.getDonHangOfKH(idKH);
    }

    @GetMapping("/detail-order")
    public String showDetail(@RequestParam Integer idDH, Model model){
        model.addAttribute("dh", qldhService.getDetailDonHang(idDH));
        return "/autokid/detail-order";
    }

    @GetMapping("/check-order")
    public String showSearch(Model model){
        model.addAttribute("currentPage","contact");
        return "/autokid/check-order";
    }

    @PostMapping("/search-order")
    @ResponseBody
    public Map<String, Object> searchOrderByMaHD(@RequestBody Map<String, Object> payload) {
        String maHD = payload.get("maHD").toString();
        Map<String, Object> map = new LinkedHashMap<>();
        logger.info("MaHD for search in AutoKidAccountController: {}", maHD);
        if (hoaDonRepo.getIdHDByMaHD(maHD) == null) {
            map.put("idHD", "null");
        } else {
            map.put("idHD", hoaDonRepo.getIdHDByMaHD(maHD));
        }

        return map;
    }
}
