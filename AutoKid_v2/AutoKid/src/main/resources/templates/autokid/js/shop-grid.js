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
                    style="background-image: url('/img/product/${product.anhSPMau}')"
                    style="width: 262.5px; height: 270px; background-size: cover;"
                  >
                    <ul class="product__item__pic__hover">
                      <li>
                        <a href="#"><i class="fa fa-heart"></i></a>
                      </li>
                      <li>
                        <a href="http://localhost:8080/autokid/shop-detail?idSP=${product.idSP}"><i class="fa fa-retweet"></i></a>
                      </li>
                      <li>
                        <a href="javascript:void(0)"
                        data-id="${product.idSP}"
                        data-ten="${product.tenSP}"
                        data-gia="${product.giaSauGiam}"
                        data-idspct="${product.idSPCT}"
                        data-mausac="${product.mauSacSPCT}"
                        data-soluong="${product.soLuongSPCT}"
                        data-anh="${product.anhSPMau}"
                        onclick="addToCart(this)"
                        >
                            <i class="fa fa-shopping-cart"></i>
                        </a>
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

