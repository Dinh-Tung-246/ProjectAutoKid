function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const count = cart.reduce((total, item) => total + item.quantity, 0); // Tính tổng số lượng sản phẩm
    document.getElementById('cart-count').textContent = count; // cập nhật số lượng vào biểu tượng giỏ hàng
}

function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function updateCartTotal() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];

    const total = cart.reduce((sum, item) => {
        const price = parseFloat(item.price) || 0;
        const quantity = item.quantity || 0;
        return sum + (price * quantity);
    }, 0); // Tính tổng số tiền

    document.getElementById('cart-total').textContent = formatPrice(total); // cập nhật tổng giá
}

// Gọi hàm sau khi DOM đã tải xong
document.addEventListener('DOMContentLoaded', () => {
    updateCartCount();
    updateCartTotal();
});

window.onload = function () {
    updateCartCount(); // Cập nhật số lượng giỏ hàng khi tải trang
    updateCartTotal(); // cập nhật tổng tiền giỏ hàng
};

function renderUserMenu() {
    const userMenu = document.getElementById("user-menu");
    const khachHang = JSON.parse(localStorage.getItem("KH"));

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
                localStorage.removeItem("KH");
                renderUserMenu();
            })
        }
    });
}

document.addEventListener("DOMContentLoaded", renderUserMenu);
