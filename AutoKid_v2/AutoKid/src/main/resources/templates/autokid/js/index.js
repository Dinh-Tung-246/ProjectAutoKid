// Hàm thêm sản phẩm vào giỏ hàng
function addToCart(a) {
  //Lấy idSPCT, tenSPCT, donGia
  let idSP = a.getAttribute("data-id");
  let tenSP = a.getAttribute("data-ten");
  // Loại bỏ dấu phân cách hàng nghìn và chuyển chuỗi giá về số
  let donGiaStr = a.getAttribute("data-gia").replace(/\./g, "");
  let donGia = parseFloat(donGiaStr); // Chuyển chuỗi thành số thực
  let idSPCT = a.getAttribute("data-idspct");
  let mauSac = a.getAttribute("data-mausac");
  let soLuongSPCT = a.getAttribute("data-soluong");
  let anhSP = a.getAttribute('data-anh');

  if (soLuongSPCT === null) {
    Swal.fire({
      title: "Sản phẩm này hiện đang hết hàng!",
      text: "Xin lỗi vì sự bất tiện này",
      icon: "warning",
      confirmButtonText: "OK",
    });
    return;
  }

  let cart = JSON.parse(sessionStorage.getItem("cart")) || [];

  // Kiểm tra xem sản phẩm đã có trong giỏ chưa
  let sp = cart.find((item) => item.idSPCT === idSPCT && item.idSP === idSP);

  if (sp) {
    // Nếu có, tăng số lượng
    sp.quantity += 1;
  } else {
    cart.push({ idSP: idSP, name: tenSP, price: donGia, idSPCT: idSPCT, color: mauSac, anhSP: anhSP, quantity: 1 });
  }

  sessionStorage.setItem("cart", JSON.stringify(cart));
  updateCartCount();
  updateCartTotal();
  // updateCartTable();
  Swal.fire({
    title: "Thêm sản phẩm giỏ hàng thành công!",
    icon: "success",
  });
}
