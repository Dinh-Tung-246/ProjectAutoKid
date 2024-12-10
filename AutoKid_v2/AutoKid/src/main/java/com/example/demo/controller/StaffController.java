package com.example.demo.controller;

import com.example.demo.model.ChucVu;
import com.example.demo.model.NhanVien;
import com.example.demo.repository.ChucVuRepo;
import com.example.demo.service.QuanLyDatHangService;
import com.example.demo.service.QuanLyNhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/staff")
public class StaffController {
    @Autowired
    private QuanLyNhanVienService nhanVienService;
    @Autowired
    private ChucVuRepo chucVuRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    QuanLyDatHangService serviceQLDH;

    @GetMapping("/home")
    public String index(Model model) {
        model.addAttribute("danhSachChucVu" , chucVuRepo.findAll());
        model.addAttribute("namePage", "staff");
        model.addAttribute("donhang",serviceQLDH.getDonHang());
        model.addAttribute("int", serviceQLDH.getIndex());
        model.addAttribute("listStaff", nhanVienService.findAllStaff());
        return "/admin/staff";
    }

    @PostMapping("/save")
    public String addStaff(@RequestParam("maNV") String maNV,
                           @RequestParam("tenNV") String tenNV,
                           @RequestParam("gioiTinh") String gioiTinh,
                           @RequestParam("ngaySinh") String ngaySinh,
                           @RequestParam("ngayLamViec") String ngayLamViec,
                           @RequestParam("matKhau") String matKhau,
                           @RequestParam("sdt") String sdt,
                           @RequestParam("email") String email,
                           @RequestParam("diaChi") String diaChi,
                           @RequestParam("trangThai") Integer trangThai,
                           @RequestParam("chucVu.id") Integer chucVuId,
                           Model model) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ngaySinhLocal = LocalDate.parse(ngaySinh, formatter);
        LocalDate ngayLamViecLocal = LocalDate.parse(ngayLamViec, formatter);

        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNV(maNV);
        nhanVien.setTenNV(tenNV);
        nhanVien.setGioiTinh(gioiTinh);
        nhanVien.setNgaySinh(ngaySinhLocal);
        nhanVien.setNgayLamViec(ngayLamViecLocal);
        nhanVien.setMatKhau(passwordEncoder.encode(matKhau));
        nhanVien.setSdt(sdt);
        nhanVien.setEmail(email);
        nhanVien.setDiaChi(diaChi);
        nhanVien.setTrangThai(trangThai);

        ChucVu chucVu = chucVuRepo.findById(chucVuId).orElse(null);
        nhanVien.setChucVu(chucVu);

        nhanVienService.save(nhanVien);

        model.addAttribute("danhSachChucVu", chucVuRepo.findAll());
        return "redirect:/admin/staff/home";
    }


    @GetMapping("/edit/{id}")
    public String showStaff(@PathVariable Integer id, Model model) {
        NhanVien nhanVien = nhanVienService.findStaffbyId(id);
        model.addAttribute("danhSachChucVu", chucVuRepo.findAll());
        model.addAttribute("nhanVien", nhanVien);
        return "admin/updateStaff";
    }

    @PostMapping("/update")
    public String updateStaff(@ModelAttribute("nhanVien") NhanVien nhanVien) {
        nhanVienService.save(nhanVien);
        return "redirect:/admin/staff/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(@PathVariable int id) {
        nhanVienService.delete(id);
        return "redirect:/admin/staff/home";
    }

    @GetMapping("/search")
    public String searchStaff(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword == null || keyword.trim().isEmpty()) {
            model.addAttribute("listStaff", nhanVienService.findAllStaff());
        } else {
            model.addAttribute("listStaff", nhanVienService.sreachStaff(keyword));
        }
        return "/admin/staff";
    }

}
