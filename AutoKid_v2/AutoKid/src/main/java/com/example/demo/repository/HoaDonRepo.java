package com.example.demo.repository;

import com.example.demo.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepo extends JpaRepository<HoaDon,Integer> {
    List<HoaDon> findByTrangThaiHD(String trangThaiHD);

    @Query(value = "SELECT TOP 1 * " +
            "FROM hoa_don h" +
            " ORDER BY h.id_hd DESC", nativeQuery = true)
    HoaDon getHoaDon();

    @Modifying
    @Query(value = "UPDATE hoa_don SET trang_thai_hd = :trangThai" +
            " WHERE id_hd = :idHD ", nativeQuery = true)
    void updateHoaDon(@Param("trangThai") String trangThai,@Param("idHD") Integer idHD);

    Optional findHoaDonByMaHD(String maHD);

    @Query("SELECT h FROM HoaDon h WHERE h.khachHang.id = :idKH ORDER BY h.ngayTao ASC ")
    List<HoaDon> getHDByIdKH(@Param("idKH") Integer idKH);

    @Query("SELECT h FROM HoaDon h WHERE " +
            "(COALESCE(:maHd, '') = '' OR h.maHD LIKE %:maHd%)")
    List<HoaDon> searchInvoices(String maHd);


    // Đơn hàng đã thanh toán, chờ giao hàng hoặc chưa thanh toán, chờ giao hàng
    @Query(value = "SELECT * FROM hoa_don WHERE trang_thai_hd = N'Đã thanh toán, chờ giao hàng' OR trang_thai_hd = N'Chưa thanh toán, chờ giao hàng'", nativeQuery = true)
    List<HoaDon> findAllByPending();

    // Đơn hàng đã thanh toán, đang giao hàng hoặc chưa thanh toán, đang giao hàng
    @Query(value = "SELECT * FROM hoa_don WHERE trang_thai_hd = N'Đã thanh toán, đang giao hàng' OR trang_thai_hd = N'Chưa thanh toán, đang giao hàng'", nativeQuery = true)
    List<HoaDon> findAllByInProgress();

    // Đơn hàng đã hoàn thành
    @Query(value = "SELECT * FROM hoa_don WHERE trang_thai_hd = N'Hoàn thành'", nativeQuery = true)
    List<HoaDon> findAllByCompleted();

    // Đơn hàng đã bị hủy
    @Query(value = "SELECT * FROM hoa_don WHERE trang_thai_hd = N'Huỷ đơn hàng'", nativeQuery = true)
    List<HoaDon> findAllByCanceled();

    @Query(value = "SELECT * FROM hoa_don h WHERE " +
            "(COALESCE(:maHd, '') = '' OR h.ma_hd LIKE %:maHd%) AND " +
            "(h.trang_thai_hd IN (N'Đã thanh toán, chờ giao hàng', N'Chưa thanh toán, chờ giao hàng'))",
            nativeQuery = true)
    List<HoaDon> searchHoaDonPending(@Param("maHd") String maHd);

    @Query(value = "SELECT * FROM hoa_don h WHERE " +
            "(COALESCE(:maHd, '') = '' OR h.ma_hd LIKE %:maHd%) AND " +
            "(h.trang_thai_hd IN (N'Đã thanh toán, đang giao hàng', N'Chưa thanh toán, đang giao hàng'))",
            nativeQuery = true)
    List<HoaDon> searchHoaDonInProgress(@Param("maHd") String maHd);

    @Query(value = "SELECT * FROM hoa_don h WHERE " +
            "(COALESCE(:maHd, '') = '' OR h.ma_hd LIKE %:maHd%) AND " +
            "h.trang_thai_hd IN (N'Hoàn thành')",
            nativeQuery = true)
    List<HoaDon> searchHoaDonCompleted(@Param("maHd") String maHd);

    @Query(value = "SELECT * FROM hoa_don h WHERE " +
            "(COALESCE(:maHd, '') = '' OR h.ma_hd LIKE %:maHd%) AND " +
            "h.trang_thai_hd IN (N'Huỷ đơn hàng')",
            nativeQuery = true)
    List<HoaDon> searchHoaDonCanceled(@Param("maHd") String maHd);

    @Query("select month(o.ngayTao), sum(o.tongTien) " +
            "from HoaDon o " +
            "where year(o.ngayTao) = ?1 " +
            "group by month(o.ngayTao) " +
            "order by month(o.ngayTao)")
    List<Object[]> getOrderByMonthInYear(int year);

    @Query("select day(o.ngayTao), sum(o.tongTien) " +
            "from HoaDon o " +
            "where month(o.ngayTao) = ?1 and year(o.ngayTao) = ?2 " +
            "group by day(o.ngayTao) " +
            "order by day(o.ngayTao)")
    List<Object[]> getOrderByDayInMonth(int month, int year);

    @Query("SELECT sp.maSP AS idSanPham, " +
            "sp.tenSP AS tenSanPham, " +
            "SUM(hdct.soLuong) AS tongSoLuong, " +
            "SUM(hdct.donGiaSauGiam) AS tongDoanhThu " +
            "FROM HoaDonChiTiet hdct " +
            "JOIN hdct.sanPhamChiTiet spct " +
            "JOIN spct.sanPham sp " +
            "GROUP BY sp.maSP, sp.tenSP " +
            "ORDER BY tongDoanhThu DESC")
    List<Object[]> findTop5BestSellingProducts();

    @Query("SELECT h.id FROM HoaDon h" +
            " WHERE h.maHD = :maHD")
    Integer getIdHDByMaHD(@Param("maHD") String maHD);

}
