package com.example.demo.service;

import com.example.demo.model.KhachHang;
import com.example.demo.model.ThongTinVanChuyen;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.ThongTinVanChuyenRepo;
import com.example.demo.response.KhachHangResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuanLyKhachHangService {
    private static String EMAIL = null;

    @Autowired
    KhachHangRepo khachHangRepo;

    @Autowired
    ThongTinVanChuyenRepo ttvcRepo;

    private static final Logger logger = LoggerFactory.getLogger(QuanLyKhachHangService.class);

    //đổ toàn bộ khách hàng ra table
    public List<KhachHangResponse> getAllKH() {
        List<KhachHangResponse> list = new ArrayList<>();
        for (KhachHang k : khachHangRepo.findAll()) {
            list.add(new KhachHangResponse(k));
        }
        return list;
    }

    // tìm kiếm khách hàng theo tên gần đúng
    public List<KhachHang> searchCustomer(String ten) {
        List<KhachHang> list = new ArrayList<>();
        try {
            for (KhachHang k : khachHangRepo.findByName(ten.trim())) {
                list.add(k);
            }
        } catch (Exception error) {
            logger.error("Error occurred while searching for customers: ", error);
        }
        return list;
    }

    public List<KhachHangResponse> deleteCustomer(Integer idKH) {
        List<KhachHangResponse> list = new ArrayList<>();
        for (KhachHang kh : khachHangRepo.findAll()) {
            if (kh.getId() == idKH) {
                khachHangRepo.deleteById(idKH);
            } else list.add(new KhachHangResponse(kh));
        }
        return list;
    }

    // Mã hóa mật khẩu bằng Bcryt
    public static String encryptPassword(String password) {
        // fixed salt cố định gồm 29 ký tự dùng để mã hóa
        String fixedSalt = "$2a$10$N8DdTVZJkMd6e8KAgJeU9eDdbsJ3G9aM/.0COu/Z4U7/lkK5tH2ja";

        //Mã hóa mật khẩu theo salt
        String hashPassWord = BCrypt.hashpw(password, fixedSalt);
        return hashPassWord;
    }

    //check mail đã tồn tại chưa
    public boolean checkMail(String email) {
        boolean result = true;
        for (KhachHang kh : khachHangRepo.findAll()) {
            if (kh.getEmail().equals(email.trim())) { // nếu mail đã tồn tại
                result = false;
                break;
            }
        }
        return result;
    }

    public String registerAccount(String hoTen, String email, String matKhau, String confirmPass) {
        String result;

        //Biểu thức regex chỉ cho phép các chữ cái Unicode và khoảng trắng để check tên
        String regexName = "^[\\p{L}\\s]+$";

        // Biểu thức regex kiểm tra định dạng email chuẩn
        String regexMail = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

        // Biểu thức regex kiểm tra mật khẩu bắt đầu bằng chữ hoa
        String regexPass = "^[A-Z][\\w\\W]*$";

        if (hoTen.trim().isEmpty() || hoTen.trim().equals("")
                || email.trim().isEmpty() || email.trim().equals("")
                || matKhau.trim().isEmpty() || matKhau.trim().equals("")) {
            result = "Bạn không được để trống các trường!!!";
        } else if (!hoTen.matches(regexName)) {
            result = "Bạn cần nhập tên đúng định dạng, không chứa ký tự đặc biệt!!!";
        } else if (!email.matches(regexMail) || !email.trim().endsWith(".com")) {
            result = "Email cần dúng định dạng (email@example.com)!!!";
        } else if (!checkMail(email)) {
            result = "Email đã được sử dụng vui lòng dùng email khác !!!";
        } else if (!matKhau.trim().matches(regexPass)
                || matKhau.trim().length() < 6
                || matKhau.trim().equalsIgnoreCase(" ")) {
            result = "Mật khẩu phải bắt đầu bằng chữ cái in hoa, độ dài phải trên 6 ký tự và không được chứa khoảng trắng!!!";
        } else if (!matKhau.trim().equals(confirmPass.trim())) {
            result = "Mật khẩu xác nhận không trùng khớp với mật khẩu đã tạo";
        } else {
            khachHangRepo.createAccount(hoTen, email, encryptPassword(matKhau));
            result = "sc";
        }
        return result;
    }

    public boolean checkMatKhau(String taiKhoan, String matKhau) {
        Boolean result = true;
        for (KhachHang kh : khachHangRepo.findAll()) {
            if (kh.getEmail().equals(taiKhoan.trim())) {
                if (kh.getMatKhau() == null || !kh.getMatKhau().equals(encryptPassword(matKhau))) {
                    result = false;
                }
                break;
            }
        }
        return result;
    }

    public Map<String, Object> checkLogin(String taiKhoan, String matKhau) {
        Map<String, Object> map = new LinkedHashMap<>();

        if (taiKhoan.trim().isEmpty()
                || taiKhoan.trim().equals("")
                || matKhau.trim().isEmpty()
                || matKhau.trim().equals("")) {
            map.put("result", "Bạn không được để trống các trường!!!");
        } else if (checkMail(taiKhoan)) {
            map.put("result", "Email bạn nhập không chính xác hoặc tài khoản không tồn tại!!!");
        } else if (!checkMatKhau(taiKhoan, matKhau)) {
            map.put("result", "Mật khẩu không đúng hoặc tài khoản chưa được đăng ký!!!");
        } else {
            map.put("result", "sc");
            for (KhachHang kh : khachHangRepo.findAll()) {
                if (kh.getEmail().equals(taiKhoan.trim())) {
                    map.put("idKH", kh.getId());
                    map.put("tenKH", kh.getTenKH());
                    map.put("emailKH", kh.getEmail());
                    map.put("pass", kh.getMatKhau());
                    if (kh.getSdt() != null && kh.getDiaChi() != null) {
                        map.put("sdtKH", kh.getSdt());
                        map.put("diaChiKH", kh.getDiaChi());
                    }
                    if (kh.getThongTinVanChuyen() != null) {
                        map.put("tenNN", kh.getThongTinVanChuyen().getTenNguoiNhan());
                        map.put("sdtNN", kh.getThongTinVanChuyen().getSdt());
                        map.put("diaChiNN", kh.getThongTinVanChuyen().getDiaChi());
                    }
                }
            }
        }

        return map;
    }

    public KhachHangResponse getInfoCustomer(Integer id) {
        return new KhachHangResponse(khachHangRepo.findById(id).orElseThrow());
    }

    // insert thông tin vận chuyển mới
    public void insertThongTinVanChuyen(ThongTinVanChuyen ttvc) {
        ttvcRepo.save(ttvc);
    }

    // update  thông tin khách hàng
    public void updateKhachHang(KhachHang khachHang) {
        khachHang.setThongTinVanChuyen(ttvcRepo.getTTVC());
        khachHangRepo.save(khachHang);
    }

    // insert thông tin khách hàng
    public String insertKhachHang(KhachHangResponse khres) {
        String result = null;
        //Biểu thức regex chỉ cho phép các chữ cái Unicode và khoảng trắng để check tên
        String regexName = "^[\\p{L}\\s]+$";

        // Biểu thức regex kiểm tra định dạng email chuẩn
        String regexMail = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

        ThongTinVanChuyen ttvc = new ThongTinVanChuyen();
        ttvc.setMaTTVC("TTVC" + new Date().getTime());
        ttvc.setTenNguoiNhan(khres.getTenNguoiNhan());
        ttvc.setSdt(khres.getSdtNguoiNhan());
        ttvc.setDiaChi(khres.getDiaChiNhan());

        KhachHang khachHang = new KhachHang();
        khachHang.setTenKH(khres.getTenKH());
        khachHang.setEmail(khres.getEmailKH());
        khachHang.setSdt(khres.getSdtKH());
        khachHang.setDiaChi(khres.getDiaChiKH());

        if (khachHang.getTenKH() == null || khachHang.getTenKH().trim().isEmpty()
                || khachHang.getDiaChi() == null || khachHang.getDiaChi().trim().isEmpty()
                || khachHang.getSdt() == null || khachHang.getSdt().trim().isEmpty()
                || khachHang.getEmail() == null || khachHang.getEmail().trim().isEmpty()
                || ttvc.getDiaChi() == null || ttvc.getDiaChi().trim().isEmpty()
                || ttvc.getSdt() == null || ttvc.getSdt().trim().isEmpty()
                || ttvc.getTenNguoiNhan() == null || ttvc.getTenNguoiNhan().trim().isEmpty()) {
            result = "Bạn không được để trống thông tin khách hàng!";
        } else if (!khachHang.getTenKH().matches(regexName)
                || !ttvc.getTenNguoiNhan().matches(regexName)) {
            result = "Bạn cần nhập đúng tên!";
        } else if (!khachHang.getEmail().matches(regexMail)) {
            result = "Bạn cần nhập đúng định dạng gmail!";
        } else if (khachHang.getSdt().trim().length() != 10
                || !khachHang.getSdt().trim().startsWith("+84")
                && !khachHang.getSdt().trim().startsWith("09")
                && !khachHang.getSdt().trim().startsWith("03")) {
            result = "Bạn cần nhập đúng số điện thoại không chứa chữ hoặc ký tự, có độ dài 9 hoặc 10 số!";
        } else if (ttvc.getSdt().trim().length() != 10
                || !ttvc.getSdt().trim().startsWith("+84")
                && !ttvc.getSdt().trim().startsWith("09")
                && !ttvc.getSdt().trim().startsWith("03")) {
            result = "Bạn cần nhập đúng số điện thoại không chứa chữ hoặc ký tự, có độ dài 9 hoặc 10 số!";
        } else if (!checkMail(khachHang.getEmail())) {
            result = "Email này đang được sử dụng!";
        } else {
            insertThongTinVanChuyen(ttvc);
            khachHang.setThongTinVanChuyen(ttvcRepo.getTTVC());
            khachHangRepo.save(khachHang);
            result = "sc";
        }
        return result;
    }
}
