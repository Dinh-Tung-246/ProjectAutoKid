package com.example.demo.controller;


import com.example.demo.model.HoaDon;
import com.example.demo.model.HoaDonChiTiet;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.PhuongThucThanhToanRepo;
import com.example.demo.repository.SanPhamChiTietRepo;
import com.example.demo.service.QuanLyDatHangService;
import com.example.demo.service.VNPAYService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
@RequestMapping("/payment")
public class VNPAYController {
    private static Map<String, Object> KHACH_HANG;
    private static List<Map<String, Object>> HDCT_LIST;

    private Logger logger = LoggerFactory.getLogger(VNPAYController.class);

    @Autowired
    private VNPAYService vnPayService;

    @Autowired
    QuanLyDatHangService quanLyDatHangService;

    @Autowired
    KhachHangRepo khachHangRepo;

    @Autowired
    SanPhamChiTietRepo spctRepo;

    @Autowired
    PhuongThucThanhToanRepo phuongThucThanhToanRepo;

    @GetMapping("/")
    public String home() {
        return "/vnpay/createOrder";
    }

    //     Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") long orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam("vnpTxnRef") String vnpTxnRef,
                              @RequestParam("idKH") String idKH,
                              @RequestParam("hdct") String hdct, // chuỗi JSON
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl, vnpTxnRef);
        ObjectMapper objectMapper =  new ObjectMapper();
        System.out.println("=====================" + orderTotal);
        List<Map<String, Object>> hdctlist = new ArrayList<>();
        try {
            hdctlist = objectMapper.readValue(hdct, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> infoKH = new LinkedHashMap<>();
        try {
            infoKH = objectMapper.readValue(idKH, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        KHACH_HANG = infoKH;
        HDCT_LIST = hdctlist;
        System.out.println(HDCT_LIST);
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String vnpTxnRef = request.getParameter("vnp_TxnRef");

        StringBuilder maHD = new StringBuilder();
        maHD.append("HD");
        maHD.append(vnpTxnRef);
        Integer idKH = null;
        if (KHACH_HANG.get("idKH") != null) {
            idKH = Integer.parseInt(KHACH_HANG.get("idKH").toString());
        }
        String tenNN = KHACH_HANG.get("tenNN").toString();
        String diaChiNN = KHACH_HANG.get("diaChiNN").toString();
        String sdtNN = KHACH_HANG.get("sdt").toString();

        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD(maHD.toString());
        if (idKH != null) {
            hoaDon.setKhachHang(khachHangRepo.findById(idKH).orElseThrow());
        }
        hoaDon.setPhuongThucThanhToan(phuongThucThanhToanRepo.findById(2).orElseThrow());
        hoaDon.setPhiShip(0.0F);
        hoaDon.setTongTien(Float.parseFloat(totalPrice)/100F);
        hoaDon.setTrangThaiHD("Đã thanh toán");
        hoaDon.setTenNguoiNhan(tenNN);
        hoaDon.setDiaChiNguoiNhan(diaChiNN);
        hoaDon.setSdtNguoiNhan(sdtNN);
        quanLyDatHangService.createHoaDon(hoaDon);

        logger.info("Data : {}", HDCT_LIST);

        for (Map<String, Object> map : HDCT_LIST) {
            HoaDonChiTiet hdct = new HoaDonChiTiet();
            if (map.get("idSPCT") != null) {
                String idSPCT = map.get("idSPCT").toString();
                hdct.setSanPhamChiTiet(spctRepo.findById(Integer.parseInt(idSPCT)).orElseThrow());
            }
            hdct.setSoLuong(Integer.parseInt(map.get("quantity").toString()));
            String donGiaSauGiam = map.get("totalPrice").toString();
            donGiaSauGiam = donGiaSauGiam.replace(".", "");
            hdct.setDonGiaSauGiam(Double.parseDouble(donGiaSauGiam));
            Integer soLuong = Integer.parseInt(map.get("quantity").toString());
            Integer idSPCT = Integer.parseInt(map.get("idSPCT").toString());

            quanLyDatHangService.createHDCT(hdct, soLuong, idSPCT);
        }

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", vnPayService.formatPrice(totalPrice));
        model.addAttribute("paymentTime", vnPayService.formatDateTime(paymentTime));
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "/vnpay/ordersuccess" : "/vnpay/orderfail";
    }
}