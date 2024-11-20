// Hàm cập nhật sản phẩm sau khi dữ liệu từ API
function updateProductList(products) {
    const productContainer = document.getElementById("product-container");
    productContainer.innerHTML = '';// xóa danh sách cũ

    products.forEach(product => {
        console.log("Product: ", product);
        const productHTML = `
        <div class="col-lg-4 col-md-6 col-sm-6">
                <div class="product__item">
                  <div
                    class="product__item__pic set-bg"
                    style="background-image: url('/img/categories/${product.anhSPMau}')"
                  >
                    <ul class="product__item__pic__hover">
                      <li>
                        <a href="#"><i class="fa fa-heart"></i></a>
                      </li>
                      <li>
                        <a href="#"><i class="fa fa-retweet"></i></a>
                      </li>
                      <li>
                        <a href="#"><i class="fa fa-shopping-cart"></i></a>
                      </li>
                    </ul>
                  </div>
                  <div class="product__item__text">
                    <h6><a href="#">${product.tenSP}</a></h6>
                    ${product.donGia === product.giaSauGiam ? `
                        <h5>${product.donGia} VND</h5>
                    ` : `
                        <h5>
                            <del style="text-decoration-thickness: 2px; opacity: 0.5;">
                                ${product.donGia} VNĐ
                            </del><br/>
                            ${product.giaSauGiam} VNĐ
                        </h5>
                    `}
                </div>
                </div>
              </div>
            `;
        productContainer.innerHTML += productHTML;
    });

}

// Lọc theo khoảng giá
function filterByPrice(value) {
    fetch(`/autokid/shop/price?value=${value}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            updateProductList(data);
        })
        .catch((error) => {
            console.log("ERROR", error);
        });
}

// Lọc theo sort by
function filterByPrice2(value) {
    fetch(`/autokid/shop/filter?filter=${value}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => response.json())
        .then((data) => {
            updateProductList(data);
        })
        .catch((error) => {
            console.log("ERROR", error);
        });
}

//Lọc theo thương hiệu
function filterByBrands() {
    const selectedBrands = Array.from(document.querySelectorAll(".checkbox-brand:checked")).map((checkbox) => checkbox.value);

    fetch(`/autokid/shop/brand?list=${selectedBrands.join(",")}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            updateProductList(data);
        })
        .catch((error) => {
            console.log("ERROR", error);
        });
}

document.addEventListener("DOMContentLoaded", function () {
    const checkboxes = document.querySelectorAll(".checkbox-brand");
    checkboxes.forEach((checkbox) => {
        console.log("Checkbox clicked:", checkbox.value); // Kiểm tra sự kiện
        checkbox.addEventListener("change", filterByBrands);
    });
});

// Hàm thêm sản phẩm vào giỏ hàng
function addToCart(a) {
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
        title: "Thêm sản phẩm giỏ hàng thành công!",
        icon: "success",
    });
}

