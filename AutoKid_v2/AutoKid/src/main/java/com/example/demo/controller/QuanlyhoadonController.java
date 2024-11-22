package com.example.demo.controller;

import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import com.example.demo.model.KhuyenMai;
import com.example.demo.repository.HoaDonHistoryRepo;
import com.example.demo.response.HoaDonResponse;
import com.example.demo.service.QuanLyHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/hoadon")
public class QuanlyhoadonController {
    @Autowired
    private QuanLyHoaDonService quanLyHoaDonService;

//    @GetMapping("")
//    public String getAll(@RequestParam(defaultValue = "0")Integer page,
//                         @RequestParam(defaultValue = "5") Integer size,
//                         Model model){
//        Page<HoaDon> HoaDonPage = quanLyHoaDonService.get(page,size);
//        model.addAttribute("list",HoaDonPage.getContent());
//        model.addAttribute("currentPage",page);
//        model.addAttribute("totalPages",HoaDonPage.getTotalPages());
//        model.addAttribute("totalItems",HoaDonPage.getTotalElements());
//        model.addAttribute("size", size);
//
//        return "admin/hoadon";
//    }

    @GetMapping("")
    public String ShowHD(Model model) {
        model.addAttribute("listhd", quanLyHoaDonService.fillAllHoaDon());
//        model.addAttribute("listhds", quanLyHoaDonService.fillAllHoaDonHistory());
        model.addAttribute("listhdct", quanLyHoaDonService.fillAllHoaDonChiTiet());
        model.addAttribute("namePage", "hoadon");
        return "admin/hoadon";
    }


    //    @GetMapping("/delete")
//    public String deleteThuongHieu(@RequestParam("id") Integer id){
//        quanLyHoaDonService.DeleteHoaDon(id);
//        return "redirect:/admin/hoadon";
//    }
    @PostMapping("/updateStatus")
    public String updateHoaDonStatus(@RequestParam("maHD") String maHD, @RequestParam("trangThai") String trangThai) {
        try {
            quanLyHoaDonService.updateHoaDonStatus(maHD, trangThai);
            return "redirect:/admin/hoadon";
        } catch (Exception e) {
            return "Lỗi";
        }
    }


//    @GetMapping("/seachhd")
//    public String seachHD(
//            @RequestParam(value = "maHD", required = false) String maHD,
//            Model model
//    ) {
//        if (maHD != null && maHD.trim().isEmpty()) maHD = null;
////        if (trangThaiHD != null && trangThaiHD.trim().isEmpty()) trangThaiHD = null;
//
//        // Tìm kiếm HoaDonChiTiet dựa trên maHD và trangThaiHD
//        List<HoaDonChiTiet> hoaDonChiTiets = quanLyHoaDonService.seach(maHD);
//        model.addAttribute("listhdct", hoaDonChiTiets);
//
//        return "admin/hoadon";
//    }
}



