$(document).ready(function () {
  // Xóa các nút "-" và "+"
  $(".pro-qty .dec").remove();
  $(".pro-qty .inc").remove();
});

// Hàm thêm sản phẩm vào giỏ hàng
function addToCart(a) {
  // a.preventDefault();
  //Lấy idSPCT, tenSPCT, donGia
  let idSP = a.getAttribute("data-id");
  let tenSP = a.getAttribute("data-ten");
  // Loại bỏ dấu phân cách hàng nghìn và chuyển chuỗi giá về số
  let donGiaStr = a.getAttribute("data-gia").replace(/\./g, "");
  let donGia = parseFloat(donGiaStr); // Chuyển chuỗi thành số thực

  // Lấy giỏ hàng từ Local Storage hoặc tạo mới nếu chưa có
  let cart = JSON.parse(localStorage.getItem("cart")) || [];

  // Kiểm tra xem sản phẩm đã có trong giỏ chưa
  let sp = cart.find((item) => item.id === idSP);

  if (sp) {
    // Nếu có, tăng số lượng
    sp.quantity += 1;
  } else {
    cart.push({ id: idSP, name: tenSP, price: donGia, quantity: 1 });
  }

  // Lưu giỏ hàng vào LocalSttorage và cập nhật bảng
  localStorage.setItem("cart", JSON.stringify(cart));
  updateCartCount();
  updateCartTotal();
  // updateCartTable();
  Swal.fire({
    title: "Thêm sản phẩm vào giỏ hàng thành công!",
    icon: "success",
    confirmButtonText: null,
  });
  setTimeout(function () {
    window.location.href = "http://localhost:8080/autokid/shoping-cart";
  }, 1000);
}

// hàm tăng số lượng
function increaseQuantity() {
  const quantityInput = document.getElementById("quantity-input");
  let quantity = parseInt(quantityInput.value) || 1;
  quantityInput.value = quantity + 1;
}

// hàm giảm số lượng
function decreaseQuantity() {
  const quantityInput = document.getElementById("quantity-input");
  let quantity = parseInt(quantityInput.value) || 1;
  if (quantity > 1) {
    quantityInput.value = quantity - 1;
  }
}

// Hàm thêm sản phẩm vào giỏ từ button
$("#add-to-cart").on("click", function (event) {
  event.preventDefault();

  const a = document.getElementById("add-to-cart");
  let idSP = a.getAttribute("data-id");
  let tenSP = a.getAttribute("data-name");
  let giaStr = a.getAttribute("data-price").replace(/\./g, "");
  const giaSP = parseFloat(giaStr);
  let quantitySP = parseInt(document.getElementById("quantity-input").value);

  let cart = JSON.parse(localStorage.getItem("cart")) || [];

  // Kiểm tra xem sản phẩm đã có trong giỏ chưa
  let sp = cart.find((item) => item.id === idSP);

  if (sp) {
    sp.quantity = sp.quantity + quantitySP;
  } else {
    cart.push({
      id: idSP,
      name: tenSP,
      price: giaSP,
      quantity: quantitySP,
    });
  }

  localStorage.setItem("cart", JSON.stringify(cart));
  updateCartCount();
  updateCartTotal();
  Swal.fire({
    title: "Thêm sản phẩm vào giỏ hàng thành công!",
    icon: "success",
    confirmButtonText: null,
  });
  setTimeout(function () {
    window.location.href = "http://localhost:8080/autokid/shoping-cart";
  }, 1000);
});
