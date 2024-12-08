package com.example.demo.model;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

@Data
@Entity
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voucher")
    private Integer id;

    @Column(name = "ma_voucher", nullable = false, unique = true)
    @NotBlank(message = "Mã voucher không được để trống")
    @Pattern(regexp = "^[A-Z0-9_-]{5,50}$", message = "Mã voucher chỉ chứa chữ in hoa, số, dấu gạch ngang (-) hoặc gạch dưới (_), độ dài từ 5 đến 50 ký tự")
    private String ma;

    @Column(name = "ten_voucher", nullable = false)
    @NotBlank(message = "Tên voucher không được để trống")
    @Size(max = 100, message = "Tên voucher không được dài hơn 100 ký tự")
    private String ten;

    @Column(name = "loai_voucher", nullable = false)
    @Min(value = 1, message = "Loại voucher phải có giá trị từ 1")
    @Max(value = 3, message = "Loại voucher phải có giá trị tối đa là 3")
    private int loaiVoucher;

    @Column(name = "dieu_kien", nullable = false)
    @Positive(message = "Điều kiện phải là số dương")
    @DecimalMin(value = "0.1", inclusive = true, message = "Điều kiện phải lớn hơn hoặc bằng 0.1")
    private double dieuKien;

    @Column(name = "gia_tri", nullable = false)
    @Positive(message = "Giá trị phải là số dương")
    @DecimalMax(value = "100.0", inclusive = true, message = "Giá trị không được vượt quá 100%")
    private double giaTri;

    @Column(name = "gia_tri_toi_da")
    @PositiveOrZero(message = "Giá trị tối đa phải là số dương hoặc 0")
    private Double giaTriToiDa;

    @Column(name = "ngay_bat_dau", nullable = false)
    @NotNull(message = "Ngày bắt đầu không được để trống")
    @PastOrPresent(message = "Ngày bắt đầu phải là ngày hiện tại hoặc trong quá khứ")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc", nullable = false)
    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải là một ngày trong tương lai")
    private LocalDate ngayKetThuc;

    @Column(name = "trang_thai", nullable = false)
    @Min(value = 0, message = "Trạng thái phải là 0 (không hoạt động)")
    @Max(value = 1, message = "Trạng thái phải là 1 (đang hoạt động)")
    private int trangThai;

    public String getFormattedgiaTri() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(giaTri).replace("₫", "");
    }
    public String getFormattedgiaTriToiDa() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(giaTriToiDa).replace("₫", "VND");
    }
}
