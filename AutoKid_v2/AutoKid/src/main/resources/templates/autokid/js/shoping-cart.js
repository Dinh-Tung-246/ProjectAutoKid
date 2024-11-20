function formatPrice(price) {
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function renderCartForm() {
  const cartData = JSON.parse(localStorage.getItem("cart")) || [];
  const tbody = document.getElementById("cart-body");
  tbody.innerHTML = ""; // Xóa nội dung cũ

  cartData.forEach((item) => {
    const row = document.createElement("tr");
    row.innerHTML = `
            <td class="shoping__cart__item">
            <input type="checkbox" class="check" style="margin: 30px">
                      <img src="img/imgs/demo1.jpg" alt="" />
                      <h5 class="shoping__cart__name">${item.name}</h5>
                    </td>
                    <td class="shoping__cart__price">${formatPrice(
                      item.price
                    )}<span> </span></td>
                    <td class="shoping__cart__quantity">
                      <div class="quantity">
                        <div class="pro-qty">
                          <span class="dec qtybtn">-</span>
                          <input type="text" class="shoping__cart__quantityProduct" value="${
                            item.quantity
                          }" data-id="${item.id}" />
                          <span class="inc qtybtn">+</span>
                        </div>
                      </div>
                    </td>
                    <td class="shoping__cart__total"><span>${formatPrice(
                      item.price * item.quantity
                    )} </span></td>
                    <td class="shoping__cart__item__close">
                      <span class="icon_close" data-id="${
                        item.id
                      }" onclick="removeFromCart(event)" style="margin: 30px"></span>
                    </td>
          `;
    tbody.appendChild(row);
  });

  handleQuantityChange();
}

// Hàm xử lý sự kiện cho nút cộng và trừ
function handleQuantityChange() {
  $("#cart-body").on("click", ".qtybtn", function () {
    var $button = $(this);
    var oldValue = $button.parent().find("input").val();
    if ($button.hasClass("inc")) {
      var newVal = parseFloat(oldValue) + 1;
    } else {
      if (oldValue > 1) {
        var newVal = parseFloat(oldValue) - 1;
      } else {
        newVal = 1;
      }
    }
    $button.parent().find("input").val(newVal);
  });
}

// Gọi hàm render sau khi trang load nếu chưa đăng nhập
document.addEventListener("DOMContentLoaded", () => {
  const isLoggedIn = /*[[${isLoggedIn}]]*/ false;
  if (!isLoggedIn) {
    renderCartForm();
  }
});

//sự kiện cho nút cập nhật giỏ hàng
$("#update-cart-btn").on("click", function (event) {
  event.preventDefault(); // Ngăn chặn hành động mặc định của thẻ <a>
  const cartData = JSON.parse(localStorage.getItem("cart")) || [];
  cartData.forEach((item) => {
    // Cập nhật lại số lượng trong localStorage
    const quantity = $(`#cart-body input[data-id="${item.id}"]`).val();
    item.quantity = parseInt(quantity) || 0; // đảm bảo không bị NaN
  });
  localStorage.setItem("cart", JSON.stringify(cartData));
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
          const itemId = event.target.getAttribute("data-id");

          let cart = JSON.parse(localStorage.getItem("cart"));

          cart = cart.filter((item) => item.id != itemId);

          localStorage.setItem("cart", JSON.stringify(cart));

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

$("#checkout-btn").on("click", function (event) {
  event.preventDefault();

  const checkoutItems = [];

  // duyệt qua từng dòng sản phẩm đã check
  $("#cart-body input.check:checked").each(function () {
    const row = $(this).closest("tr"); //Tìm dòng sản phẩm tương ứng
    const name = row.find(".shoping__cart__name").text().trim(); // lấy tên sản phẩm
    const totalPrice = row.find(".shoping__cart__total").text().trim(); // lấy tổng giá sản phẩm

    checkoutItems.push({ name, totalPrice });
  });

  localStorage.setItem("checkout", JSON.stringify(checkoutItems));

  window.location.href = "http://localhost:8080/autokid/checkout";
});
