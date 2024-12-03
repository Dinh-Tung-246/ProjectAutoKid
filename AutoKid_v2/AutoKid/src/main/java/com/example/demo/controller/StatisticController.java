package com.example.demo.controller;

import com.example.demo.service.ThongKeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final ThongKeService thongKeService;

    @GetMapping("/revenue/yearly/{year}")
    public ResponseEntity<?> getOrderByMonthInYear(@PathVariable int year){
        return ResponseEntity.ok().body(thongKeService.getOrderByMonthInYear(year));
    }

    @GetMapping("/revenue/yearly/{year}/monthly/{month}")
    public ResponseEntity<?> getOrderByMonthInYear(@PathVariable("year") int year,
                                                   @PathVariable("month") int month){
        return ResponseEntity.ok().body(thongKeService.getOrderByDayInMonth(month, year));
    }

}
