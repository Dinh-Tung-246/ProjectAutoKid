create database DATN_AutoKid
go

use DATN_AutoKid
go

-- Bảng chuc_vu
CREATE TABLE chuc_vu (
    id_chuc_vu INT PRIMARY KEY IDENTITY,
    ma_chuc_vu NVARCHAR(50),
    ten_chuc_vu NVARCHAR(100)
);

-- Bảng nhan_vien
CREATE TABLE nhan_vien (
    id_nv INT PRIMARY KEY IDENTITY,
    ma_nv NVARCHAR(100),
    ten_nv NVARCHAR(100),
    gioi_tinh NVARCHAR(10),
    ngay_sinh DATE,
    ngay_lam_viec DATE,
    mat_khau NVARCHAR(255),
    sdt NVARCHAR(20),
    email NVARCHAR(100),
    trang_thai_nv INT,
    dia_chi TEXT,
    id_chuc_vu INT,
    FOREIGN KEY (id_chuc_vu) REFERENCES chuc_vu(id_chuc_vu)
);

-- Bảng thong_tin_van_chuyen
CREATE TABLE thong_tin_van_chuyen (
    id_ttvc INT PRIMARY KEY IDENTITY,
    ma_ttvc NVARCHAR(50) NOT NULL,
--    tinh NVARCHAR(50) NOT NULL,
--    huyen NVARCHAR(50),
--    xa NVARCHAR(50),
    dia_chi NVARCHAR(255) NOT NULL,
    ten_nguoi_nhan NVARCHAR(100),
    sdt NVARCHAR(20)
);

-- Bảng khach_hang
CREATE TABLE khach_hang (
    id_kh INT PRIMARY KEY IDENTITY,
 --   ma_kh NVARCHAR(100),
    ten_kh NVARCHAR(100),
    email NVARCHAR(100),
    sdt NVARCHAR(20),
    dia_chi NVARCHAR(MAX),
    mat_khau NVARCHAR(255),
    id_ttvc INT,
    FOREIGN KEY (id_ttvc) REFERENCES thong_tin_van_chuyen(id_ttvc)
);

-- Bảng phuong_thuc_thanh_toan
CREATE TABLE phuong_thuc_thanh_toan (
    id_pttt INT PRIMARY KEY IDENTITY,
    ma_pttt NVARCHAR(50),
    ten_pttt NVARCHAR(100),
    ma_code NVARCHAR(100),
    thong_tin_thanh_toan NVARCHAR(100),
    trang_thai INT
);

-- Bảng loai_sp
CREATE TABLE loai_sp (
    id_loai_sp INT PRIMARY KEY IDENTITY,
    ma_lsp NVARCHAR(50),
    ten_loai NVARCHAR(100),
    trang_thai NVARCHAR(50),
    mo_ta NVARCHAR(MAX)
);

-- Bảng kich_co
CREATE TABLE kich_co (
    id_kich_co INT PRIMARY KEY IDENTITY,
    ma_kc NVARCHAR(50),
    ten_kc NVARCHAR(50),
    trang_thai_kc NVARCHAR(50),
    mo_ta TEXT
);

-- Bảng mau_sac
CREATE TABLE mau_sac (
    id_mau_sac INT PRIMARY KEY IDENTITY,
    ma_ms NVARCHAR(50),
    ten_ms NVARCHAR(50),
    trang_thai_ms NVARCHAR(50)
);

-- Bảng chat_lieu
CREATE TABLE chat_lieu (
    id_chat_lieu INT PRIMARY KEY IDENTITY,
    ma_cl NVARCHAR(50),
    ten_cl NVARCHAR(50),
    trang_thai_cl VARCHAR(50)
);

-- Bảng thuong_hieu
CREATE TABLE thuong_hieu (
    id_thuong_hieu INT PRIMARY KEY IDENTITY,
    ma_th NVARCHAR(50),
    ten_th NVARCHAR(100),
    trang_thai_th NVARCHAR(50),
    dia_chi TEXT
);

-- Bảng khuyen_mai
CREATE TABLE khuyen_mai (
    id_khuyen_mai INT PRIMARY KEY IDENTITY,
	ma_km VARCHAR(20) not null,
    ten_km NVARCHAR(100) not null,
    gia_tri DECIMAL(10, 2) not null,
    ngay_bat_dau DATE not null,
    ngay_ket_thuc DATE not null,
    trang_thai VARCHAR(10)     

);

-- Bảng san_pham
CREATE TABLE san_pham (
    id_san_pham INT PRIMARY KEY IDENTITY,
    ma_sp NVARCHAR(50),
    ten_sp NVARCHAR(100),
    trang_thai_sp NVARCHAR(50),
	ngay_tao DATE,
	gia_nhap MONEY,	
	gia_ban MONEY,
    id_loai_sp INT,
	id_chat_lieu INT,
	id_kich_co INT,
    id_thuong_hieu INT,
	id_km INT,
	mo_ta NVARCHAR(MAX),
	anh_sp_mau NVARCHAR(100),
	FOREIGN KEY (id_km) REFERENCES khuyen_mai(id_khuyen_mai),
    FOREIGN KEY (id_loai_sp) REFERENCES loai_sp(id_loai_sp),
	FOREIGN KEY (id_chat_lieu) REFERENCES chat_lieu(id_chat_lieu),
    FOREIGN KEY (id_kich_co) REFERENCES kich_co(id_kich_co),
    FOREIGN KEY (id_thuong_hieu) REFERENCES thuong_hieu(id_thuong_hieu)
);

-- Bảng san_pham_chi_tiet
CREATE TABLE san_pham_chi_tiet (
    id_san_pham_chi_tiet INT PRIMARY KEY IDENTITY,
    ma_spct NVARCHAR(50),
    id_san_pham INT,
    id_mau_sac INT,
    so_luong INT,
    anh NVARCHAR(255),
    trang_thai_spct NVARCHAR(50),
    FOREIGN KEY (id_san_pham) REFERENCES san_pham(id_san_pham),
    FOREIGN KEY (id_mau_sac) REFERENCES mau_sac(id_mau_sac)
);

-- Bảng voucher
CREATE TABLE voucher (
    id_voucher INT PRIMARY KEY IDENTITY,
    ma_voucher VARCHAR(50) NOT NULL UNIQUE,      
    ten_voucher NVARCHAR(100) NOT NULL,        
    loai_voucher int NOT NULL, 
    dieu_kien FLOAT NULL,                   
    gia_tri FLOAT NOT NULL,                     
    gia_tri_toi_da FLOAT NULL,             
    ngay_bat_dau DATE NOT NULL,                  
    ngay_ket_thuc DATE NOT NULL,                
    trang_thai int NOT NULL,     
);

-- Bảng hoa_don
CREATE TABLE hoa_don (
    id_hd INT PRIMARY KEY IDENTITY,
    ma_hd NVARCHAR(100),
    id_kh INT,
    id_nv INT,
    id_pttt INT,
	id_voucher INT,
    ngay_tao DATETIME,
    phi_ship MONEY,
    hinh_thuc_thanh_toan NVARCHAR(50),
    tong_tien MONEY,
    trang_thai_hd NVARCHAR(50),
	ten_nguoi_nhan NVARCHAR(50),
	dia_chi_nguoi_nhan NVARCHAR(200),
	sdt_nguoi_nhan VARCHAR(10),
    FOREIGN KEY (id_kh) REFERENCES khach_hang(id_kh),
    FOREIGN KEY (id_nv) REFERENCES nhan_vien(id_nv),
	FOREIGN KEY (id_voucher) REFERENCES voucher(id_voucher),
    FOREIGN KEY (id_pttt) REFERENCES phuong_thuc_thanh_toan(id_pttt)
);

-- Bảng hoa_don_chi_tiet
CREATE TABLE hoa_don_chi_tiet (
    id_hoa_don_chi_tiet INT PRIMARY KEY IDENTITY,
    id_hd INT,
    id_san_pham_chi_tiet INT,
    so_luong INT,
    don_gia MONEY,
    don_gia_sau_giam MONEY,
    FOREIGN KEY (id_hd) REFERENCES hoa_don(id_hd),
    FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id_san_pham_chi_tiet)
);

-- Bảng trung gian KhuyenMai va SanPham
--CREATE TABLE khuyen_mai_san_pham (
--    id INT PRIMARY KEY IDENTITY,
--    khuyen_mai_id INT,
--    san_pham_id INT,
--    CONSTRAINT fk_khuyen_mai FOREIGN KEY (khuyen_mai_id) REFERENCES khuyen_mai (id_khuyen_mai) ON DELETE CASCADE,
--    CONSTRAINT fk_san_pham FOREIGN KEY (san_pham_id) REFERENCES san_pham (id_san_pham) ON DELETE CASCADE
--);

-- Bảng gio_hang
CREATE TABLE gio_hang (
    id_gio_hang INT PRIMARY KEY IDENTITY,
    id_kh INT,
    trang_thai NVARCHAR(50),
    FOREIGN KEY (id_kh) REFERENCES khach_hang(id_kh)
);

-- Bảng gio_hang_chi_tiet
CREATE TABLE gio_hang_chi_tiet (
    id_gio_hang_chi_tiet INT PRIMARY KEY IDENTITY,
    id_gio_hang INT NOT NULL,
    id_san_pham_chi_tiet INT,
    so_luong INT,
    don_gia MONEY,
--	ngay_tao DATETIME GETDATE(),    nghiệp vụ cao cấp: khi này giỏ hàng sẽ sắp xếp các sản phẩm theo ngày thêm vào giỏ hàng
    FOREIGN KEY (id_gio_hang) REFERENCES gio_hang(id_gio_hang),
    FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id_san_pham_chi_tiet)
);

-- Bảng hoa_don_history
-- CREATE TABLE hoa_don_history (
--    id_lich_su_hoa_don INT PRIMARY KEY IDENTITY,
--    id_hd INT,
--    ngay_thay_doi DATETIME,
--    ngay_tao DATETIME,
--    trang_thai NVARCHAR(50),
--    FOREIGN KEY (id_hd) REFERENCES hoa_don(id_hd)
--);

-- Bảng danh_gia
CREATE TABLE danh_gia (
    id_danh_gia INT PRIMARY KEY IDENTITY,
    id_san_pham_chi_tiet INT,
    noi_dung_danh_gia TEXT,
    ngay_tao DATETIME,
    so_sao INT,
    FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id_san_pham_chi_tiet)
);

ALTER TABLE hoa_don
ADD CONSTRAINT DF_hoa_don_ngay_tao DEFAULT GETDATE() FOR ngay_tao;

INSERT INTO loai_sp (ma_lsp, ten_loai, trang_thai, mo_ta) 
VALUES 
('LSP001', N'Xe đạp trẻ em 3 bánh', N'Hoạt động', N'Xe đạp 3 bánh dành cho trẻ từ 1-3 tuổi'),
('LSP002', N'Xe đạp trẻ em có bánh phụ', N'Hoạt động', N'Xe đạp có bánh phụ dành cho trẻ từ 3-5 tuổi'),
('LSP003', N'Xe đạp trẻ em thể thao', N'Hoạt động', N'Xe đạp thể thao dành cho trẻ từ 5-7 tuổi'),
('LSP004', N'Xe đạp thăng bằng', N'Hoạt động', N'Xe đạp thăng bằng dành cho trẻ em từ 2-5 tuổi'),
('LSP005', N'Xe đạp điện trẻ em', N'Hoạt động', N'Xe đạp điện cho trẻ em từ 6-10 tuổi');

INSERT INTO khuyen_mai (ma_km, ten_km, gia_tri, ngay_bat_dau, ngay_ket_thuc,trang_thai) 
VALUES 
('KM001', 'Black Friday', 50, '2024-10-31', '2024-11-20', '1'); 

INSERT INTO chat_lieu (ma_cl, ten_cl, trang_thai_cl) 
VALUES 
('CL001', N'Thép', N'Hoạt động'),
('CL002', N'Hợp kim nhôm', N'Hoạt động'),
('CL003', N'Nhựa cao cấp', N'Hoạt động'),
('CL004', N'Nhôm', N'Hoạt động'),
('CL005', N'Carbon', N'Hoạt động');

INSERT INTO mau_sac (ma_ms, ten_ms, trang_thai_ms) 
VALUES 
('MS001', N'Đỏ', N'Hoạt động'),
('MS002', N'Xanh lá', N'Hoạt động'),
('MS003', N'Vàng', N'Hoạt động'),
('MS004', N'Đen', N'Hoạt động'),
('MS005', N'Hồng', N'Hoạt động'),
('MS006', N'Xanh lam', N'Hoạt động'),
('MS007', N'Tím', N'Hoạt động'),
('MS008', N'Cam', N'Hoạt động'),
('MS009', N'Đen pha cam', N'Hoạt động'),
('MS010', N'Đen pha vàng', N'Hoạt động'),
('MS011', N'Trắng pha xanh lam', N'Hoạt động'),
('MS012', N'Trắng pha hồng', N'Hoạt động'),
('MS013', N'Đen pha xanh lam', N'Hoạt động'),
('MS014', N'Xám', N'Hoạt động'),
('MS015', N'Lam pha lục', N'Hoạt động'),
('MS016', N'Cam pha lam', N'Hoạt động'),
('MS017', N'Xanh lam nhạt', N'Hoạt động'),
('MS018', N'Xanh lục nhạt', N'Hoạt động')

INSERT INTO kich_co (ma_kc, ten_kc, trang_thai_kc, mo_ta) 
VALUES 
('KC001', N'Nhỏ', N'Hoạt động', N'Kích thước nhỏ dành cho trẻ từ 1-3 tuổi'),
('KC002', N'Trung bình', N'Hoạt động', N'Kích thước trung bình dành cho trẻ từ 3-5 tuổi'),
('KC003', N'Lớn', N'Hoạt động', N'Kích thước lớn dành cho trẻ từ 5-7 tuổi'),
('KC004', N'Rất lớn', N'Hoạt động', N'Kích thước rất lớn dành cho trẻ trên 7 tuổi'),
('KC005', N'Siêu nhỏ', N'Hoạt động', N'Kích thước dành cho trẻ sơ sinh');

INSERT INTO thuong_hieu (ma_th, ten_th, trang_thai_th, dia_chi) 
VALUES 
('TH001', N'ABC', N'Hoạt động', N'Địa chỉ ABC - Việt Nam'),
('TH002', N'XYZ', N'Hoạt động', N'Địa chỉ XYZ - Nhật Bản'),
('TH003', N'KidsSport', N'Hoạt động', N'Địa chỉ KidsSport - Hàn Quốc'),
('TH004', N'JoyBalance', N'Hoạt động', N'Địa chỉ JoyBalance - Mỹ'),
('TH005', N'Mini E-Bike', N'Hoạt động', N'Địa chỉ Mini E-Bike - Trung Quốc');

INSERT INTO san_pham (ma_sp, ten_sp, gia_nhap,gia_ban, trang_thai_sp,mo_ta, id_loai_sp, id_kich_co, id_chat_lieu, id_thuong_hieu, id_km, anh_sp_mau) 
VALUES 
('SP001', N'FORNIX Panda', 400000, 500000, N'Đang bán', N'Xe đạp 3 bánh ABC màu đỏ, khung thép, kích cỡ nhỏ', 1, 1, 1, 1, 1, 'PandaPurpleBlue.jpg'),
('SP002', N'ROYALBABY Little Swan', 500000, 700000, N'Đang bán', N'Xe đạp có bánh phụ XYZ màu xanh, khung hợp kim, kích cỡ trung bình', 2, 2, 2, 2, 1, 'RoyPink.jpg'),
('SP003', N'Youth LIV Adore FW', 600000, 800000, N'Đang bán', N'Xe đạp thăng bằng JoyBalance màu hồng, khung nhôm, kích cỡ nhỏ', 3, 3, 3, 3, null, 'AdoreGreen.jpg'),
('SP004', N'Youth MAX BIKE Bambi', 700000, 1000000, N'Đang bán', N'Xe đạp điện mini E-Bike màu đen, khung thép, kích cỡ lớn', 4, 4, 4, 4, null, 'BambiBluOrange.jpg'),
('SP005', N'YOUTH MAX BIKE Barbie', 400000, 550000, N'Đang bán', N'Xe đạp 3 bánh ABC màu xanh, khung thép, kích cỡ nhỏ', 5, 5, 5, 5, null, 'BabyGreen.jpg'),
('SP006', N'Youth MAX BIKE Bella', 600000, 750000, N'Đang bán', N'Xe đạp 3 bánh ABC màu xanh, khung thép, kích cỡ nhỏ', 5, 5, 5, 5, null, 'Pink.jpg'),
('SP007', N'Youth MAX BIKE Hola', 800000, 1100000, N'Đang bán', N'Xe đạp 3 bánh ABC màu xanh, khung thép, kích cỡ nhỏ', 5, 5, 5, 5, null, 'Black.jpg'),
('SP008', N'Youth MAX BIKE Mini', 400000, 600000, N'Đang bán', N'Xe đạp 3 bánh ABC màu xanh, khung thép, kích cỡ nhỏ', 5, 5, 5, 5, null, 'Blue.jpg'),
('SP009', N'Youth MAX BIKE Subasa', 650000, 850000, N'Đang bán', N'Xe đạp 3 bánh ABC màu xanh, khung thép, kích cỡ nhỏ', 5, 5, 5, 5, null, 'SubasaBlue.jpg'),
('SP010', N'Youth MAX BIKE Zira', 600000, 850000, N'Đang bán', N'Xe đạp 3 bánh ABC màu xanh, khung thép, kích cỡ nhỏ', 5, 5, 5, 5, null, 'ZiraBlue.jpg'),
('SP011', N'Youth MISAKI Luka', 550000, 7000000, N'Đang bán', N'Xe đạp 3 bánh ABC màu xanh, khung thép, kích cỡ nhỏ', 5, 5, 5, 5, null, 'LukaLightBlueGreen.jpg'),
('SP012', N'Youth VINBIKE Mochi', 500000, 650000, N'Đang bán', N'Xe đạp 3 bánh ABC màu xanh, khung thép, kích cỡ nhỏ', 5, 5, 5, 5, null, 'MochiBlack.jpg');

INSERT INTO san_pham_chi_tiet (ma_spct, id_san_pham, id_mau_sac, so_luong, anh, trang_thai_spct) 
VALUES 
('SPCT001', 1, 6, 50, 'PandaPurpleBlue.jpg', N'Còn hàng'),
('SPCT002', 1, 5, 30, 'PandaWhitePink.jpg', N'Còn hàng'),
('SPCT003', 2, 5, 20, 'RoyPink.jpg', N'Còn hàng'),
('SPCT004', 3, 2, 40, 'AdoreGreen.jpg', N'Còn hàng'),
('SPCT005', 3, 7, 15, 'AdorePurple.jpg', N'Còn hàng'),
('SPCT006', 4, 8, 45, 'BambiBluOrange.jpg', N'Còn hàng'),
('SPCT007', 4, 2,35, 'BambiGreyGreen.jpg', N'Còn hàng'),
('SPCT008', 4, 6, 18, 'BambiRedBlue.jpg', N'Còn hàng'),
('SPCT009', 5, 2, 37, 'BabyGreen.jpg', N'Còn hàng'),
('SPCT010', 5, 5, 20, 'BabyPink.jpg', N'Còn hàng'),
('SPCT011', 5, 7, 25, 'BabyPurple.jpg', N'Còn hàng'),
('SPCT012', 6, 5, 30, 'Pink.jpg', N'Còn hàng'),
('SPCT013', 6, 7, 35, 'Purple.jpg', N'Còn hàng'),
('SPCT014', 7, 4, 40, 'Black.jpg', N'Còn hàng'),
('SPCT015', 7, 9, 50, 'BlackOrange.jpg', N'Còn hàng'),
('SPCT016', 7, 10, 30, 'BlackYellow.jpg', N'Còn hàng'),
('SPCT017', 7, 11, 20, 'WhiteBlue.jpg', N'Còn hàng'),
('SPCT018', 8, 6, 40, 'Blue.jpg', N'Còn hàng'),
('SPCT019', 8, 12, 25, 'WhitePink.jpg', N'Còn hàng'),
('SPCT020', 9, 6, 35, 'SubasaBlue.jpg', N'Còn hàng'),
('SPCT021', 9, 13, 25, 'SubasaDarkBlue.jpg', N'Còn hàng'),
('SPCT022', 9, 2, 30, 'SubasaGreen.jpg', N'Còn hàng'),
('SPCT023', 9, 14, 15, 'SubasaGrey.jpg', N'Còn hàng'),
('SPCT024', 10, 6, 25, 'ZiraBlue.jpg', N'Còn hàng'),
('SPCT025', 10, 8, 10, 'ZiraOrange.jpg', N'Còn hàng'),
('SPCT026', 11, 15, 30, 'LukaLightBlueGreen.jpg', N'Còn hàng'),
('SPCT027', 11, 16, 40, 'LukaOrangeBlue.jpg', N'Còn hàng'),
('SPCT028', 12, 4, 25, 'MochiBlack.jpg', N'Còn hàng'),
('SPCT029', 12, 17, 20, 'MochiLightBlue.jpg', N'Còn hàng'),
('SPCT030', 12, 18, 15, 'MochiLightGreen.jpg', N'Còn hàng'),
('SPCT031', 12, 5, 20, 'MochiPink.jpg', N'Còn hàng'),
('SPCT032', 12, 1, 25, 'MochiRed.jpg', N'Còn hàng')

INSERT INTO chuc_vu (ma_chuc_vu, ten_chuc_vu) 
VALUES 
('CV001', N'Quản lý'),
('CV002', N'Nhân viên'),
('CV003', N'ADMIN');

INSERT INTO nhan_vien (ma_nv, ten_nv, gioi_tinh, ngay_sinh, ngay_lam_viec, mat_khau, sdt, email, trang_thai_nv, dia_chi, id_chuc_vu) 
VALUES 
('NV001', N'Trần Văn A', N'Nam', '1990-05-15', '2023-01-01', 'password123', '0901234567', 'trana@gmail.com', 1, N'Số 1, đường A', 1),
('NV002', N'Nguyễn Thị B', N'Nữ', '1995-08-20', '2023-02-01', 'password123', '0907654321', 'nguyenb@gmail.com', 0, N'Số 2, đường B', 2),
('NV003', N'Nguyễn Văn C', N'Nam', '1992-11-10', '2023-03-15', 'password123', '0902223334', 'nguyenc@gmail.com',1, N'Số 3, đường C', 3),
('NV005', N'Nguyễn Văn T', N'Nữ', '1993-01-15', '2023-03-15', '$2a$10$0ed0A4BzYIdhUS0N75nsDOaaAXQC7PuJZ3uoh3.YIh9alKRq3zBCm', '0902223334', 'nguyenngoc@gmail.com',1, N'Số 4, đường C', 3);

INSERT INTO thong_tin_van_chuyen (ma_ttvc, dia_chi, ten_nguoi_nhan, sdt) 
VALUES 
('TTVC001', N'Số 4, phố B', N'Trần Văn A', '0901234567'),
('TTVC002',  N'Số 10, đường C', N'Nguyễn Thị B', '0907654321'),
('TTVC003', N' Đường C', N'Nguyễn Văn F', '0123456784');

INSERT INTO khach_hang (ten_kh, email, sdt, dia_chi, mat_khau, id_ttvc) 
VALUES 
(N'Nguyễn Văn G', 'kh1@example.com', '0123456785', N'Số 4, Đường D', 'password1', 1),
(N'Trần Thị H', 'kh2@example.com', '0123456786', N'Số 5, Đường E', 'password2', 2),
(N'Nguyễn Văn I', 'kh3@example.com', '0123456787', N'Số 6, Đường F', 'password3', 3),
(N'Đinh Văn Tùng', 'tung@example.com', '0123456787', N'Số 6, Đường F', '$2a$10$N8DdTVZJkMd6e8KAgJeU9eySm.EO9xobhngj06hkAqFQkIwH2dY2W', 3); --Tt456903

INSERT INTO phuong_thuc_thanh_toan (ma_pttt, ten_pttt, ma_code, thong_tin_thanh_toan, trang_thai) 
VALUES 
('PTTT001', N'Thanh toán khi nhận hàng', 'TP004', N'Thanh toán bằng tiền mặt khi nhận hàng', 1),
('PTTT002', N'Chuyển khoản nhanh', 'TP005', N'Chuyển khoản qua VNpay', 1),
('PTTT003', N'Thẻ ghi nợ', 'TP006', N'Sử dụng thẻ ghi nợ để thanh toán', 0),
('PTTT004', N'Thẻ tín dụng', 'TP007', N'Sử dụng thẻ tín dụng để thanh toán', 0);

INSERT INTO hoa_don (ma_hd, id_kh, id_nv, id_pttt, ngay_tao, phi_ship, hinh_thuc_thanh_toan, tong_tien, trang_thai_hd, ten_nguoi_nhan, dia_chi_nguoi_nhan, sdt_nguoi_nhan) 
VALUES 
('HD001', 1, 1, 1, GETDATE(), 50000, N'Transfer', 1500000, N'Hoạt động', N'Trung Khá Bảnh', N'Aeon Mall Hà Đông', '0123456789'),
('HD002', 2, 2, 2, GETDATE(), 60000, N'Pay on delivery', 3000000, N'Hoạt động', N'Ngọc Bướng Bỉnh', N'Cầu Diễn, Hà Nội', '0123456789');

INSERT INTO hoa_don_chi_tiet (id_hd, id_san_pham_chi_tiet, so_luong, don_gia, don_gia_sau_giam) 
VALUES 
(2, 3, 2, 500000, 1000000),
(1, 2, 1, 800000, 800000),
(2, 3, 3, 1500000, 4500000);

INSERT INTO gio_hang (id_kh, trang_thai) 
VALUES 
(1, N'Đang hoạt động'),
(2, N'Đang hoạt động');

INSERT INTO gio_hang_chi_tiet (id_gio_hang, id_san_pham_chi_tiet, so_luong, don_gia) 
VALUES 
(1, 1, 1, 500000),
(1, 2, 2, 800000),
(2, 3, 1, 1500000);

INSERT INTO danh_gia (id_san_pham_chi_tiet, noi_dung_danh_gia, ngay_tao, so_sao) 
VALUES 
(1, N'Sản phẩm rất tốt, trẻ em thích.', GETDATE(), 5),
(2, N'Chất lượng tốt nhưng giá hơi cao.', GETDATE(), 4);

INSERT INTO voucher (ma_voucher, ten_voucher, loai_voucher, dieu_kien, gia_tri, gia_tri_toi_da, ngay_bat_dau, ngay_ket_thuc, trang_thai)
VALUES
('VC001', 'Giảm 20% cho đơn hàng trên 500k', 1, 20000, 20, 5000, '2024-12-01', '2024-12-31', 1),
('VC002', 'Giảm 10K cho đơn hàng trên 300k', 2, 20000, 10000, NULL, '2024-12-01', '2024-12-15', 1);



select * from voucher

--INSERT INTO hoa_don_history (id_hd, ngay_thay_doi, ngay_tao, trang_thai) 
--VALUES 
--(1, GETDATE(), GETDATE(), N'Hoạt động'),
--(2, GETDATE(), GETDATE(), N'Hoạt động');

--INSERT INTO khuyen_mai_san_pham(khuyen_mai_id, san_pham_id) VALUES
--(1, 1);

