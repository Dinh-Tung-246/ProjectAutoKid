function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function renderPrice() {
    const checkoutData = JSON.parse(sessionStorage.getItem("checkout")) || [];
    const tbody = document.getElementById("checkout-price");
    const totalBody = document.getElementById("total-price");
    tbody.innerHTML = "";

    let total = 50000;

    checkoutData.forEach((item) => {
        const row = document.createElement("ul");
        row.innerHTML = `
            <li>${item.name} <span>${item.totalPrice}</span></li>
            <div id="total-price"></div>
          `;
        tbody.appendChild(row);
        const itemPrice = parseInt(item.totalPrice.replace(/\./g, ""), 10);
        total += itemPrice;
    });

    totalBody.innerHTML = `
                    <div style="margin-bottom: 20px; color: firebrick;">
                        <strong>Phí ship <span style="margin-left: 310px;">50.000</span></strong>
                    </div>
                   <div class="checkout__order__total" >
                       Tổng cộng <span id="totalPrice">${formatPrice(total)}</span>
                       <input type="hidden" id="secret-price" value="${formatPrice(total)}">
                   </div>
        `;
}

function renderInfo() {
    const data = JSON.parse(sessionStorage.getItem("KH")) || [];
    console.log(data.length)
    if (data != null && data.length != 0) {
        document.getElementById("name-kh").value = data.tenKH;
        document.getElementById("sdt-kh").value = data.sdtKH;
        document.getElementById("diaChi-kh").value = data.diaChiKH;
        document.getElementById("email-kh").value = data.emailKH;
        console.log(data.tenKH, " - ", data.sdtKH, " - ", data.diaChiKH);
    }
}

document.addEventListener("DOMContentLoaded", async () => {
    renderPrice();
    renderInfo();
    let totalPrice = document.getElementById("totalPrice").textContent;
    console.log("totalPrice: ", totalPrice);
    const payload = {
        totalPrice: totalPrice.replaceAll('.', ''),
    }
    const content = document.getElementById("select-voucher");
    console.log("content", content)
    const response = await fetch('/api/get-voucher', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
    });
    const result = await response.json();
    console.log("result voucher", result);

    // Làm sạch container trước khi thêm mới
    content.innerHTML = '';

    // Tạo thẻ <select>
    const selectElement = document.createElement('select');
    selectElement.id = 'voucher-select'; // Gán id cho select
    selectElement.style.width = '100%'; // Đặt chiều rộng cho select
    selectElement.style.padding = '5px'; // Padding cho đẹp
    selectElement.style.borderRadius = '5px';

    // Thêm option mặc định
    const defaultOption = document.createElement('option');
    defaultOption.textContent = 'Chọn voucher';
    defaultOption.value = '';
    // defaultOption.disabled = true;
    defaultOption.hidden = true;
    selectElement.appendChild(defaultOption);

    result.forEach(voucher => {
        const option = document.createElement('option');
        option.value = voucher.id; // Giá trị của option
        option.textContent = voucher.ten; // Nội dung hiển thị
        option.setAttribute('data-loai', voucher.loaiVoucher);
        option.setAttribute('data-giatri', voucher.giaTri);
        option.setAttribute('data-giatrimax', voucher.giaTriToiDa);
        option.setAttribute('data-dieukien', voucher.dieuKien);
        selectElement.appendChild(option);
    });
    content.appendChild(selectElement);
});

//validate số điện thoại
function validatePhoneNumber(phoneNumber) {
    // Biểu thức chính quy cho số điện thoại hợp lệ ở Việt Nam
    const phoneRegex = /^(03|05|07|08|09)\d{8}$/;

    // Kiểm tra tính hợp lệ
    return phoneRegex.test(phoneNumber);
}

function validateFullName(name) {
    // Biểu thức chính quy cho họ và tên (chỉ chấp nhận ký tự chữ cái, dấu tiếng Việt, và khoảng trắng)
    const nameRegex = /^[\\p{L}\\s]+$/;

    // Kiểm tra độ dài hợp lệ
    if (name.length < 3 || name.length > 50) {
        return "Họ và tên không hợp lệ";
    }

    return "sc";
}

function validateEmail(email) {
    // Biểu thức chính quy kiểm tra email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function getFormattedDate() {
    const now = new Date();

    const day = String(now.getDate()).padStart(2, '0');
    const month = String(now.getMonth() + 1).padStart(2, '0'); // Tháng trong JavaScript bắt đầu từ 0
    const year = now.getFullYear();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');

    return `${day}${month}${year}${hours}${minutes}${seconds}`;
}

document.querySelector('#checkout-form').addEventListener('submit', async function (event) {
    event.preventDefault();

    const hoTen = document.querySelector('input[class="name-kh"]').value.trim();
    const sdt = document.querySelector('input[class="phone-number"]').value.trim();
    const diaChi = document.querySelector('input[class="address-kh"]').value.trim();
    const emailKH = document.querySelector('input[class="email-kh"]').value.trim();
    const voucher = document.getElementById('id-voucher').value;
    const paymentTypeElement = document.querySelector('input[name="payment_type"]:checked');
    // Lấy dữ liệu từ sessionStorage
    const checkoutData = JSON.parse(sessionStorage.getItem('checkout')) || [];
    const cartData = JSON.parse(sessionStorage.getItem('cart')) || [];
    // có thể null KHData vì trường hợp khách vãng lai
    const KHData = JSON.parse(sessionStorage.getItem('KH')) || [];

    if (!hoTen || !sdt || !diaChi || !paymentTypeElement) {
        Swal.fire({
            title: "Bạn không được để trống dữ các trường",
            icon: "warning",
        });
        return;
    }
    if (validatePhoneNumber(sdt) == false) {
        Swal.fire({
            title: "Bạn cần nhập đúng số điện thoại",
            icon: "warning",
        });
        return;
    }
    if (validateFullName(hoTen) != 'sc') {
        Swal.fire({
            title: validateFullName(hoTen),
            icon: "warning",
        });
        return;
    }
    if (!validateEmail(emailKH)) {
        Swal.fire({
            title: "Email bạn nhập không hợp lệ",
            icon: "warning",
        });
        return;
    }

    let idKH = null;
    if (KHData != null) {
        idKH = KHData.idKH;
    }

    let TongTien = document.getElementById('totalPrice').textContent;

    let vnpTxnRef = getFormattedDate();
    let orderInfo = "Thanh toan don hang " + vnpTxnRef;
    const paymentData = {
        amount: TongTien.replaceAll('.',''),
        orderInfo: orderInfo,
        vnpTxnRef: vnpTxnRef,
    }

    sessionStorage.setItem("payment", JSON.stringify(paymentData));
    const paymentType = paymentTypeElement.value;

    //Xây dựng payload hóa đơn
    const hoaDon = {
        maHD: "HD" + vnpTxnRef,
        tenNguoiNhan: hoTen,
        sdtNguoiNhan: sdt,
        diaChiNguoiNhan: diaChi,
        emailNguoiNhan: emailKH,
        ngayTao: getCurrentTimestamp(),
        idPttt: parseInt(paymentType),
        tongTien: TongTien.replaceAll('.', ''),
        idKH: idKH,
        voucher: voucher,
        phiShip: 0,
        hinhThucThanhToan: "",
        trangThaiHD: "Chưa thanh toán, chờ giao hàng",
    };

    const hdct = checkoutData.map(item => ({
        idSP: item.idSP,
        idSPCT: item.idSPCT,
        soLuong: item.quantity,
        donGiaSauGiam: parseFloat(item.totalPrice.replace(/\./g, '')),
    }));

    const response = await fetch('/api/check-checkout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(hdct),
    });

    Swal.fire({
        title: "Bạn có chắc chắn muốn đặt hàng không?",
        icon: "question",
        confirmButtonText: "Có",
        cancelButtonColor: "#d33",
        confirmButtonColor: "#3085d6",
        cancelButtonText: "Không",
        showCancelButton: true,
    }).then(async (confirm) => {
        if (confirm.isConfirmed) {
            if (!response.ok) {
                Swal.fire({
                    title: "Sản phẩm bạn mua đã bị hết hàng hoặc trong kho không còn đủ số lượng!",
                    text: "Xin lỗi vì sự bất tiện này",
                    icon: "error",
                    confirmButtonText: "OK",
                })
            } else {
                if (paymentTypeElement.id === 'PTTT001') {
                    Swal.fire({
                        title: "Đang xử lý...",
                        text: "Vui lòng chờ trong giây lát",
                        allowOutsideClick: false, // Không cho phép đóng khi click ra ngoài
                        allowEscapeKey: false, // Không cho phép đóng bằng phím ESC
                        didOpen: () => {
                            Swal.showLoading(); // Hiển thị spinner
                        },
                    });
                    await handleCashPayment(hoaDon, hdct);
                } else if (paymentTypeElement.id === 'PTTT002') {
                    let cartData = JSON.parse(sessionStorage.getItem("cart")) || [];

                    let hdctIdSPCTs = hdct.map(item => item.idSPCT); // lấy ra danh sách idSPCT

                    cartData = cartData.filter(product => !hdctIdSPCTs.includes(product.idSPCT));
                    sessionStorage.setItem("cart", JSON.stringify(cartData));
                    const idKhachHang = idKH;
                    const tenNN = document.getElementById("name-kh").value.trim();
                    const diaChiNN = document.getElementById("diaChi-kh").value.trim();
                    const sdtNN = document.getElementById("sdt-kh").value.trim();
                    const InfoNN = {
                        idKH: idKhachHang,
                        tenNN: tenNN,
                        diaChiNN: diaChiNN,
                        sdt: sdtNN,
                        emailNN: emailKH,
                        voucher: voucher,
                    }

                    sessionStorage.setItem("info", JSON.stringify(InfoNN));
                    window.location.href = "http://localhost:8080/payment/";
                }
            }
        }
    })
});

async function handleCashPayment(hoaDon, hdct) {
    let cartData = JSON.parse(sessionStorage.getItem("cart")) || [];

    let hdctIdSPCTs = hdct.map(item => item.idSPCT); // lấy ra danh sách idSPCT

    cartData = cartData.filter(product => !hdctIdSPCTs.includes(product.idSPCT));

    try {
        const response = await fetch('/autokid/checkout/create', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({hoaDon, hdct}),
        });

        if (response.ok) {
            Swal.close();
            sessionStorage.removeItem("checkout");
            sessionStorage.setItem("cart", JSON.stringify(cartData));
            Swal.fire({
                title: "Đặt hàng thành công!",
                text: "Hãy kiểm tra email để nhận thông tin về đơn hàng",
                icon: "success",
                confirmButtonText: "OK",
            }).then(() => {
                window.location.href = "http://localhost:8080/autokid/home";
            });
        } else {
            alert("error");
        }
    } catch (error) {
        console.error("ERROR", error);
    }
}

function getCurrentTimestamp() {
    const now = new Date();
    const pad = (num) => (num < 10 ? '0' : '') + num;
    return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ` +
        `${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}.${now.getMilliseconds()}`;
}

async function addVoucher(button) {
    const selectedElement = document.getElementById('voucher-select');
    const idVoucher = document.getElementById('id-voucher').value || [];
    const selectedOption = selectedElement.selectedOptions[0];

    console.log('selected option', selectedOption);
    if (selectedOption.value === '') {
        Swal.fire({
            title: 'Vui lòng chọn voucher',
            icon: 'warning',
            confirmButtonText: 'OK',
        })
        return;
    }
    const idVC = selectedOption.value;
    if (idVoucher === idVC) {
        Swal.fire({
            title: 'Bạn đã áp mã này rồi!',
            icon: 'warning',
            confirmButtonText: 'OK',
        })
        return;
    }
    let loai = selectedOption.getAttribute('data-loai');
    let dieuKien = selectedOption.getAttribute('data-dieukien');
    let giaTri = selectedOption.getAttribute('data-giatri');
    let secretPrice = document.getElementById('secret-price').value;
    let giaTriToiDa = selectedOption.getAttribute('data-giatrimax');
    secretPrice = secretPrice.replaceAll('.','');
    console.log('loai', loai);
    console.log('tong tien', secretPrice);
    console.log('gia tri', giaTri);
    console.log('dieu kien', dieuKien);
    console.log('gia tri max', giaTriToiDa);
    let giaCuoi;
    if (loai.trim() === '1' ) {
        let tienGiam = secretPrice * giaTri / 100;
        if (tienGiam < giaTriToiDa) {
            giaCuoi = secretPrice - tienGiam;
        } else {
            giaCuoi = secretPrice - giaTriToiDa;
        }
        console.log('tien giam', tienGiam);
        console.log('gia cuoi', giaCuoi);
    } else {
        giaCuoi = secretPrice - giaTri;
    }
    document.getElementById('totalPrice').textContent = formatPrice(giaCuoi);
    document.getElementById('id-voucher').value = idVC;
}
