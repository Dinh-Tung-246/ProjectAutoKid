package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BestSellProduct {
    private String maSp;
    private String tenSp;
    private long soLuong;
    private String doanhThu;
}
