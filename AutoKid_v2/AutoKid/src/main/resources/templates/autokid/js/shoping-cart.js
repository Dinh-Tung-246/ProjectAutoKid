// Gọi hàm render sau khi trang load nếu chưa đăng nhập
document.addEventListener("DOMContentLoaded", () => {
    const KH = JSON.parse(sessionStorage.getItem("KH")) || [];
    if (KH.length === 0) {
        renderCartForm();
        console.log("Ban chua dang nhap");
    } else {
        console.log("Bạn đã đang nhập");
    }
});


function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function renderCartForm() {
    const cartData = JSON.parse(sessionStorage.getItem("cart")) || [];
    const tbody = document.getElementById("cart-body");
    tbody.innerHTML = ""; // Xóa nội dung cũ

    cartData.forEach((item) => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td class="shoping__cart__item">
            <input type="checkbox" class="check" style="margin: 30px">
                      <img src="img/product/${item.anhSP}" alt="" style="width: 120px; height: 120px; background-size: cover;"/>
                      <h5 class="shoping__cart__name">${item.name} (${item.color === null ? 'Màu mặc định' : item.color})</h5>
                    </td>
                    <td class="shoping__cart__price">${formatPrice(
            item.price
        )}<span> </span></td>
                    <td class="shoping__cart__quantity">
                      <div class="quantity">
                        <div class="pro-qty">
                          <span class="dec qtybtns">-</span>
                          <input type="hidden" class="shopping_cart_idProduct" value="${item.idSP}"/>
                          <input type="hidden" class="shopping_cart_idProductDetail" value="${item.idSPCT}"/>
                          <input type="text" class="shoping__cart__quantityProduct" value="${
            item.quantity
        }" data-sp="${item.idSP + item.idSPCT}" />
                          <span class="inc qtybtns">+</span>
                        </div>
                      </div>
                    </td>
                    <td class="shoping__cart__total"><span>${formatPrice(
            item.price * item.quantity
        )} </span></td>
                    <td class="shoping__cart__item__close">
                      <span class="icon_close" data-idsp="${item.idSP}" data-idspct="${item.idSPCT}"
                       onclick="removeFromCart(event)" style="margin: 30px"></span>
                    </td>
          `;
        tbody.appendChild(row);
    });

    handleQuantityChange();
}

// Hàm xử lý sự kiện cho nút cộng và trừ
function handleQuantityChange() {
    $("#cart-body").on("click", ".qtybtns", function () {
        var $button = $(this);
        var oldValue = $button.parent().find(".shoping__cart__quantityProduct").val();
        if ($button.hasClass("inc")) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            if (oldValue > 1) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 1;
            }
        }
        $button.parent().find(".shoping__cart__quantityProduct").val(newVal);
    });
}

//sự kiện cho nút cập nhật giỏ hàng
$("#update-cart-btn").on("click", function (event) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của thẻ <a>
    const cartData = JSON.parse(sessionStorage.getItem("cart")) || [];
    cartData.forEach((item) => {
        // Cập nhật lại số lượng trong sessionStorage
        const quantity = $(`#cart-body input[data-sp="${item.idSP + item.idSPCT}"]`).val();
        item.quantity = parseInt(quantity) || 0; // đảm bảo không bị NaN
    });
    sessionStorage.setItem("cart", JSON.stringify(cartData));
    renderCartForm();
    Swal.fire({
        title: "Cập nhật giỏ hàng thành công!",
        icon: "success",
        confirmButtonText: null,
    });
    setTimeout(function () {
        window.location.href = "http://localhost:8080/autokid/shoping-cart";
    }, 1000);
});

function removeFromCart(event) {
    Swal.fire({
        title: "Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng không?",
        text: "Thao tác trên sẽ xóa sản phẩm ra khỏi giỏ hàng!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Có",
        cancelButtonText: "Không",
    }).then((result) => {
        if (result.isConfirmed) {
            // Nếu người dùng chọn "Có, thực hiện!"
            Swal.fire(
                "Đã xóa thành công!",
                "Hành động của bạn đã được xác nhận.",
                "success"
            )
                // Thực hiện hành động cần thiết ở đây
                .then(() => {
                    const itemIdSP = event.target.getAttribute("data-idsp");
                    const itemIdSPCT = event.target.getAttribute("data-idspct");
                    console.log(itemIdSPCT);
                    console.log(itemIdSP);
                    let cart = JSON.parse(sessionStorage.getItem("cart"));

                    cart = cart.filter((item) => item.idSP != itemIdSP || (item.idSP == itemIdSP && item.idSPCT != itemIdSPCT));

                    sessionStorage.setItem("cart", JSON.stringify(cart));

                    renderCartForm();
                    updateCartCount(); // Cập nhật số lượng giỏ hàng khi tải trang
                    updateCartTotal(); // cập nhật tổng tiền giỏ hàng
                });
        } else {
            // Nếu người dùng chọn "Không, hủy!"
            Swal.fire("Đã hủy!", "Hành động của bạn đã bị hủy.", "error");
        }
    });
}

window.onload = function () {
    updateCartCount(); // Cập nhật số lượng giỏ hàng khi tải trang
    updateCartTotal(); // cập nhật tổng tiền giỏ hàng
};

$("#cart-body").on("change", ".check", function () {
    const $row = $(this).closest("tr");
    if ($(this).is(":checked")) {
        $row.addClass("hovered"); // thêm hiệu ứng hover
    } else {
        $row.removeClass("hovered"); // bỏ hiệu ứng hover
    }
});

//Xử lý khi check all
$(".check-all").on("change", function () {
    const isChecked = $(this).is(":checked");
    $(".check").prop("checked", isChecked).trigger("change"); // Check/unCheck tất cả
});

// Xử lý khi ấn mua hàng
$("#checkout-btn").on("click", function (event) {
    event.preventDefault();

    const checkoutItems = [];

    // duyệt qua từng dòng sản phẩm đã check
    $("#cart-body input.check:checked").each(function () {
        const row = $(this).closest("tr"); //Tìm dòng sản phẩm tương ứng
        const name = row.find(".shoping__cart__name").text().trim(); // lấy tên sản phẩm
        const totalPrice = row.find(".shoping__cart__total").text().trim(); // lấy tổng giá sản phẩm
        const quantity = row.find(".shoping__cart__quantityProduct").val().trim(); // Lấy số lượng
        const idSP = row.find(".shopping_cart_idProduct").val().trim(); // Lấy idsp
        const idSPCT = row.find(".shopping_cart_idProductDetail").val().trim(); // Lấy idspct

        checkoutItems.push({idSP, idSPCT, name, quantity, totalPrice});
    });

    if (checkoutItems[0] == null) {
        Swal.fire({
            title: "Bạn không có bất kỳ sản phẩm nào để mua",
            icon: "warning",
            confirmButtonText: "OK"
        });
    } else {
        sessionStorage.setItem("checkout", JSON.stringify(checkoutItems));
        window.location.href = "http://localhost:8080/autokid/checkout";
    }
});
