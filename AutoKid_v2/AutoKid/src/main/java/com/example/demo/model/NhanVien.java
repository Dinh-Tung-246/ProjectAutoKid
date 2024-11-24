package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Entity
@Data
@Table(name = "nhan_vien")
public class NhanVien implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nv")
    private Integer id;

    @Column(name = "ma_nv", nullable = false, unique = true)
    private String maNV;

    @Column(name = "ten_nv", nullable = false)
    private String tenNV;

    @Column(name = "gioi_tinh")
    private Integer gioiTinh;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ngay_lam_viec")
    private LocalDate ngayLamViec;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "sdt", nullable = false)
    private String sdt;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "trang_thai_nv")
    private Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "id_chuc_vu", nullable = false)
    private ChucVu chucVu;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//        List< GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + chucVu));
        return Collections.singleton(new SimpleGrantedAuthority("admin"));
    }

    @Override
    public String getPassword() {
        return matKhau;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
