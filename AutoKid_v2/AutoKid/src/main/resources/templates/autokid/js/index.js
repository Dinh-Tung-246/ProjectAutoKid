// Hàm thêm sản phẩm vào giỏ hàng
function addToCart(a) {
  const KH = JSON.parse(sessionStorage.getItem("KH")) || [];
  if (KH.length === 0 ) {
    addToCartNoLogin(a);
  } else {
    addToCartLogined(a);
  }
}

function addToCartNoLogin(a) {
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

  if (soLuongSPCT === null || soLuongSPCT === 0) {
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

async function addToCartLogined(a) {
  // Lấy thông tin sản phẩm từ các thuộc tính HTML
  let idSPCT = a.getAttribute("data-idspct");
  let soLuongSPCT = a.getAttribute("data-soluong");
  const KH = JSON.parse(sessionStorage.getItem("KH")) || [];
  const idKH = KH.idKH;

  console.log("id spct:", idSPCT);

  // Kiểm tra nếu sản phẩm hết hàng
  if (!soLuongSPCT || soLuongSPCT === "0") {
    Swal.fire({
      title: "Sản phẩm này hiện đang hết hàng!",
      text: "Xin lỗi vì sự bất tiện này",
      icon: "warning",
      confirmButtonText: "OK",
    });
    return;
  }

  // Tạo payload gửi đến API
  const payload = {
    idKH: idKH,
    idSPCT: idSPCT,
    soLuong: 1, // Thêm số lượng mặc định là 1
  }

  console.log("payload: ", payload);

  try {
    // Gọi API bằng fetch
    const response = await fetch('/api/add-to-cart', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });

    console.log("Phan hoi tu server: ", response);

    // Kiểm tra kết quả phản hồi
    if (response.ok) {
      const result = await response.json();
      console.log("Thêm vào giỏ hàng thành công:", result);

      Swal.fire({
        title: "Thành công!",
        text: "Sản phẩm đã được thêm vào giỏ hàng.",
        icon: "success",
        confirmButtonText: "OK",
      });
    } else {
      const errorData = await response.json();
      console.error("Lỗi khi thêm vào giỏ hàng:", errorData);

      Swal.fire({
        title: "Thất bại!",
        text: "Không thể thêm sản phẩm vào giỏ hàng.",
        icon: "error",
        confirmButtonText: "OK",
      });
    }
  } catch (error) {
    // Xử lý lỗi kết nối hoặc lỗi khác
    console.error("Lỗi kết nối hoặc hệ thống:", error);

    Swal.fire({
      title: "Lỗi hệ thống!",
      text: "Có lỗi xảy ra khi kết nối đến server.",
      icon: "error",
      confirmButtonText: "OK",
    });
  }
}
