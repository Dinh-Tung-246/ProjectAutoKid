document.addEventListener("DOMContentLoaded", () => {
    const KH = JSON.parse(sessionStorage.getItem("KH")) || [];
    renderUserMenu();
    if (KH.length != 0) {
        console.log("Ban vua dang nhap");
        updateCartAfterLogin();
    }
});

function updateCartCount() {
    const cart = JSON.parse(sessionStorage.getItem('cart')) || [];
    const count = cart.reduce((total, item) => total + item.quantity, 0); // Tính tổng số lượng sản phẩm
    document.getElementById('cart-count').textContent = count; // cập nhật số lượng vào biểu tượng giỏ hàng
}

function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function updateCartTotal() {
    const cart = JSON.parse(sessionStorage.getItem('cart')) || [];

    const total = cart.reduce((sum, item) => {
        const price = parseFloat(item.price) || 0;
        const quantity = item.quantity || 0;
        return sum + (price * quantity);
    }, 0); // Tính tổng số tiền

    document.getElementById('cart-total').textContent = formatPrice(total); // cập nhật tổng giá
}

async function updateCartAfterLogin() {
    const KH = JSON.parse(sessionStorage.getItem("KH")) || [];
    let idKH = KH.idKH;
    try {
        const response = await fetch('/api/gen-cart', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({idKH}),
        })
        const result = await response.json();
        document.getElementById('cart-count').textContent = result.cartCount;
        document.getElementById('cart-total').textContent = result.totalPrice;
    } catch (error) {
        alert("error");
    }
}

window.onload = function () {
    const infoKH = JSON.parse(sessionStorage.getItem("KH")) || [];
    if (infoKH.length === 0) {
        updateCartCount();
        updateCartTotal();
    } else {
        console.log("ban da dang nhap");
    }
};

function renderUserMenu() {
    const userMenu = document.getElementById("user-menu");
    const khachHang = JSON.parse(sessionStorage.getItem("KH"));
    if (!khachHang || !khachHang.tenKH) {
        userMenu.innerHTML = `<a href="http://localhost:8080/autokid/login/">
                    <i class="fa fa-user login-btn"></i> Đăng nhập
                </a>`;
    } else {
        userMenu.innerHTML =
            `<div class="header__top__right__language">
                    <div onclick="toggleDropdown()">
                        <i class="fa fa-user"></i> ${khachHang.tenKH}
                    </div>
                    <span class="arrow_carrot-down"></span>
                      <ul class="dropdown-custom" style="width: 150px;">
                        <li><a href="http://localhost:8080/autokid/account">Cập nhật thông tin</a></li>
                        <li><a href="http://localhost:8080/autokid/account/order-tracking?idKH=${khachHang.idKH}">Theo dõi đơn hàng</a></li>
                        <li><a href="#" onclick="logout()">Đăng xuất</a></li>
                      </ul>
                </div>`;
    }
}

function logout() {
    Swal.fire({
        title: "Bạn có thật sự muốn đăng xuất tài khoản?",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Tôi muốn!"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: "Đăng xuất thành công!",
                icon: "success"
            }).then(() => {
                sessionStorage.removeItem("KH");
                renderUserMenu();
                setTimeout(() => {
                    window.location.href="http://localhost:8080/autokid/home";
                }, 500)
            })
        }
    });
}

