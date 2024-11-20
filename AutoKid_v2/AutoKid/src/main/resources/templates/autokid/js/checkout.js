function formatPrice(price){
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function renderPrice() {
    const checkoutData = JSON.parse(localStorage.getItem("checkout")) || [];
    const tbody = document.getElementById("checkout-price");
    const totalBody = document.getElementById("total-price");
    tbody.innerHTML = "";

    let total = 0;

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
                   <div class="checkout__order__total" >
                       Tổng cộng <span>${formatPrice(total)}</span>
                   </div>
        `;
}

document.addEventListener("DOMContentLoaded", () => {
    renderPrice();
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

document.querySelector('#checkout-form').addEventListener('submit', async function (event) {
    event.preventDefault();

    const hoTen = document.querySelector('input[class="name-kh"]').value.trim();
    const sdt = document.querySelector('input[class="phone-number"]').value.trim();
    const diaChi = document.querySelector('input[class="address-kh"]').value.trim();
    const paymentTypeElement = document.querySelector('input[name="payment_type"]:checked');

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
    if(validateFullName(hoTen) != 'sc'){
        Swal.fire({
            title: validateFullName(hoTen),
            icon: "warning",
        });
        return;
    }

        const paymentType = paymentTypeElement.value;

        // Lấy dữ liệu từ localstorage
        const checkoutData = JSON.parse(localStorage.getItem('checkout')) || [];
        const cartData = JSON.parse(localStorage.getItem('cart')) || []
        // có thể null KHData vì trường hợp khách vãng lai
        const KHData = JSON.parse(localStorage.getItem('KH')) || []

        if (checkoutData[0] == null) {
            Swal.fire({
                title: "Bạn cần chọn sản phẩm để mua trước!",
                icon: "warning",
                confirmButtonText: "OK",
            }).then(() => {
                window.location.href = "http://localhost:8080/autokid/shoping-cart";
            });
        }

        let idKH = null;
        if (KHData != null) {
            idKH = KHData.idKH;
        }

        let TongTien = checkoutData.reduce((sum, item) => {
            const price = parseFloat(item.totalPrice.replace(/\./g, ''));
            return sum + price;
        }, 0);

        //Xây dựng payload hóa đơn
        const hoaDon = {
            maHD: "HD" + new Date().getTime(),
            tenNguoiNhan: hoTen,
            sdtNguoiNhan: sdt,
            diaChiNguoiNhan: diaChi,
            ngayTao: getCurrentTimestamp(),
            idPttt: parseInt(paymentType),
            tongTien: TongTien,
            idKH: idKH,
            phiShip: 0,
            hinhThucThanhToan: "",
            trangThaiHD: "Chờ xác nhận",
        };

        const hdct = cartData.map(item => ({
            idSPCT: item.id,
            soLuong: item.quantity,
            donGiaSauGiam: item.price,
        }));

        console.log(hoaDon);
        console.log(hdct);

        if (checkoutData[0] != null) {
            try {
                const response = await fetch('/autokid/checkout/create', {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({hoaDon, hdct}),
                });

                if (response.ok) {
                    localStorage.removeItem("checkout");
                    Swal.fire({
                        title: "Đặt hàng thành công!",
                        text: "Shop đang xác nhận đơn hàng",
                        icon: "success",
                        confirmButtonText: "OK",
                    }).then(() => {
                        window.location.href = "http://localhost:8080/autokid/home";
                    });
                } else {
                    alert('ERROR');
                }
            } catch (error) {
                console.error("ERROR", error);
            }
        }
});

function getCurrentTimestamp() {
    const now = new Date();
    const pad = (num) => (num < 10 ? '0' : '') + num;
    return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ` +
        `${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}.${now.getMilliseconds()}`;
}