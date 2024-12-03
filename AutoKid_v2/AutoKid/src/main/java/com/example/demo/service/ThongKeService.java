package com.example.demo.service;

import com.example.demo.dto.BestSellProduct;
import com.example.demo.dto.RevenueStatistic;
import com.example.demo.model.HoaDon;
import com.example.demo.model.SanPham;
import com.example.demo.repository.HoaDonRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThongKeService {
    private final HoaDonRepo hoaDonRepo;

    public double calculateTotalRevenue(){
        return hoaDonRepo.findAll()
                .stream()
                .mapToDouble(HoaDon::getTongTien)
                .sum();
    }
    public int getInvoiceCount(){
        return hoaDonRepo.findAll().size();
    }

    public List<RevenueStatistic> getOrderByMonthInYear(int year) {
        List<Object[]> results = hoaDonRepo.getOrderByMonthInYear(year);
        Map<Integer, Double> map = new HashMap<>();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Double totalPrice = (Double) result[1];
            map.put(month, totalPrice);
        }
        List<RevenueStatistic> list = new ArrayList<>();
        for(int month = 1 ; month <= 12 ; month++){
            Double totalPrice = map.getOrDefault(month, 0.0);
            list.add(new RevenueStatistic(month, totalPrice));
        }
        return list;
    }

    public List<RevenueStatistic> getOrderByDayInMonth(int month, int year) {
        List<Object[]> results = hoaDonRepo.getOrderByDayInMonth(month, year);
        YearMonth yearMonth = YearMonth.of(year, month);
        int dayInMonth = yearMonth.lengthOfMonth();
        Map<Integer, Double> map = new HashMap<>();
        for (Object[] result : results) {
            Integer day = (Integer) result[0];
            Double totalPrice = (Double) result[1];
            map.put(day, totalPrice);
        }
        List<RevenueStatistic> list = new ArrayList<>();
        for(int day = 1 ; day <= dayInMonth ; day++){
            Double totalPrice = map.getOrDefault(day, 0.0);
            list.add(new RevenueStatistic(day, totalPrice));
        }
        return list;
    }

    public List<BestSellProduct> findTop5BestSellingProducts(){
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        List<Object[]> data = hoaDonRepo.findTop5BestSellingProducts();

        List<BestSellProduct> list = new ArrayList<>();
        for (Object[] result : data) {
            double total = (double) result[3];
            BestSellProduct bestSellProduct = BestSellProduct.builder()
                    .maSp((String) result[0])
                    .tenSp((String) result[1])
                    .soLuong((long) result[2])
                    .doanhThu(decimalFormat.format(total))
                    .build();
            list.add(bestSellProduct);
        }
        return list;
    }
}
