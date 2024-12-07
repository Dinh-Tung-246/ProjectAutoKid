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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/ctht/{id}")
    @ResponseBody
    public ResponseEntity<?> getChiTietHoaDon(@PathVariable Integer id){
        HoaDon hoaDon = quanLyHoaDonService.findHoaDonByMaHD(id);
        HoaDonResponse hoaDonResponse = new HoaDonResponse(hoaDon);
        return ResponseEntity.ok().body(hoaDonResponse);

    }

    @GetMapping("/search")
    public String searchInvoices(@RequestParam(required = false) String maHd,
                                 Model model) {
        if(maHd.equals("")) {
            return "redirect:/admin/hoadon";
        }
        List<HoaDonResponse> filteredInvoices = quanLyHoaDonService.searchInvoices(maHd);
        model.addAttribute("listhd", filteredInvoices);
        return "admin/hoadon";
    }


    @PostMapping("/updateStatus")
    @ResponseBody
    public ResponseEntity<?> updateHoaDonStatus(@RequestParam("maHD") Integer maHD, @RequestParam("trangThai") String trangThai) {

        System.out.println(maHD);
        System.out.println(trangThai);
        boolean check = quanLyHoaDonService.updateHoaDonStatus(maHD, trangThai);
        return ResponseEntity.ok().body(check);
    }

    @GetMapping("/pending")
    public String HoaDonPending(Model model) {
        model.addAttribute("listhd", quanLyHoaDonService.findAllByPending());
        model.addAttribute("namePage", "pending");
        return "admin/hoadon-pending";
    }

    @GetMapping("/in-progress")
    public String HoaDonInProgress(Model model) {
        model.addAttribute("listhd", quanLyHoaDonService.findAllByInProgress());
        model.addAttribute("namePage", "in-progress");
        return "admin/hoadon-in-progress";
    }

    @GetMapping("/completed")
    public String HoaDonCompleted(Model model) {
        model.addAttribute("listhd", quanLyHoaDonService.findAllByCompleted());
        model.addAttribute("namePage", "completed");
        return "admin/hoadon-completed";
    }

    @GetMapping("/canceled")
    public String HoaDonCanceled(Model model) {
        model.addAttribute("listhd", quanLyHoaDonService.findAllByCanceled());
        model.addAttribute("namePage", "canceled");
        return "admin/hoadon-canceled";
    }


    @GetMapping("/search-pending")
    public String searchHoaDonPending(@RequestParam(required = false) String maHd,
                                 Model model) {
        if(maHd.equals("")) {
            return "redirect:/admin/hoadon/pending";
        }
        List<HoaDonResponse> filteredInvoices = quanLyHoaDonService.searchHoaDonPending(maHd);
        model.addAttribute("listhd", filteredInvoices);
        return "admin/hoadon-pending";
    }
    @GetMapping("/search-in-progress")
    public String searchHoaDonInProgress(@RequestParam(required = false) String maHd,
                                      Model model) {
        if(maHd.equals("")) {
            return "redirect:/admin/hoadon/in-progress";
        }
        List<HoaDonResponse> filteredInvoices = quanLyHoaDonService.searchHoaDonInProgress(maHd);
        model.addAttribute("listhd", filteredInvoices);
        return "admin/hoadon-in-progress";
    }
    @GetMapping("/search-completed")
    public String searchHoaDonCompleted(@RequestParam(required = false) String maHd,
                                      Model model) {
        if(maHd.equals("")) {
            return "redirect:/admin/hoadon/completed";
        }
        List<HoaDonResponse> filteredInvoices = quanLyHoaDonService.searchHoaDonCompleted(maHd);
        model.addAttribute("listhd", filteredInvoices);
        return "admin/hoadon-completed";
    }
    @GetMapping("/search-canceled")
    public String searchHoaDonCanceled(@RequestParam(required = false) String maHd,
                                      Model model) {
        if(maHd.equals("")) {
            return "redirect:/admin/hoadon/canceled";
        }
        List<HoaDonResponse> filteredInvoices = quanLyHoaDonService.searchHoaDonCanceled(maHd);
        model.addAttribute("listhd", filteredInvoices);
        return "admin/hoadon-canceled";
    }

}



