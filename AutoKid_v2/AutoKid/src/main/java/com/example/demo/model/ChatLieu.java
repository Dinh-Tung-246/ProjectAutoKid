package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "chat_lieu")
public class ChatLieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chat_lieu")
    private Integer id;

    @Column(name = "ma_cl")
    private String maCl;

    @Column(name = "ten_cl")
    private String tenCl;

    @Column(name = "trang_thai_cl")
    private String trangThaiCl;

}