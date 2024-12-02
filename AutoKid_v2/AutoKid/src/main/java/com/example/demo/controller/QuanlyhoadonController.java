package com.example.demo.controller;

import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import com.example.demo.model.KhuyenMai;
import com.example.demo.repository.HoaDonHistoryRepo;
import com.example.demo.repository.HoaDonRepo;
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

    @Autowired
    private HoaDonRepo hoaDonRepo;

    @GetMapping("")
    public String ShowHD(Model model) {
        model.addAttribute("listhd", quanLyHoaDonService.fillAllHoaDon());
//        model.addAttribute("listhds", quanLyHoaDonService.fillAllHoaDonHistory());
        model.addAttribute("listhdct", quanLyHoaDonService.fillAllHoaDonChiTiet());
        model.addAttribute("namePage", "hoadon");
        return "admin/hoadon";
    }

    @GetMapping("/chitiethoadon/{maHD}")
    public String showInvoiceDetail(@PathVariable("maHD") Integer maHD, Model model) {
        HoaDon hoaDon = quanLyHoaDonService.findHoaDonByMaHD(maHD);
        List<HoaDonChiTiet> hoaDonChiTiet = quanLyHoaDonService.findHoaDonChiTietByMaHD(maHD);

        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("hoaDonChiTiet", hoaDonChiTiet);

        return "admin/hoadonchitiet";
    }

    @GetMapping("/search")
    public String searchInvoices(@RequestParam(required = false) String maHd,
                                 Model model) {
        if(maHd.equals("")) {
            return "redirect:/admin/hoadon";
        }
        List<HoaDon> filteredInvoices = quanLyHoaDonService.searchInvoices(maHd);
        model.addAttribute("listhd", filteredInvoices);
        return "admin/hoadon";
    }


    @PostMapping("/updateStatus")
    @ResponseBody
    public String updateHoaDonStatus(@RequestParam("maHD") Integer maHD, @RequestParam("trangThai") String trangThai) {

        System.out.println(maHD);
        System.out.println(trangThai);
        try {
            quanLyHoaDonService.updateHoaDonStatus(maHD, trangThai);
            return "{\"status\": \"success\", \"message\": \"Cập nhật trạng thái thành công\"}";
        } catch (Exception e) {
            // Return error message if something goes wrong
            return "{\"status\": \"error\", \"message\": \"Cập nhật trạng thái lỗi\"}";
        }
    }


}



