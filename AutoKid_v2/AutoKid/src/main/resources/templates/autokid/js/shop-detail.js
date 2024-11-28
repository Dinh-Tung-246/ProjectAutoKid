$(document).ready(function () {
    // Xóa các nút "-" và "+"
    $(".pro-qty .dec").remove();
    $(".pro-qty .inc").remove();
});

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
    let idSPCT = document.getElementById('selectedValue').value;
    let tenMS = document.getElementById('selectedValueMS').value;
    let soLuongSPCT = a.getAttribute("data-soluong");

    if (soLuongSPCT === null) {
        Swal.fire({
            title: "Sản phẩm này hiện đang hết hàng!",
            text: "Xin lỗi vì sự bất tiện này",
            icon: "warning",
            confirmButtonText: "OK",
        });
        return;
    }

    console.log("ten", tenMS);
    console.log("idspct", idSPCT);
    if (tenMS == undefined || tenMS == null || tenMS.trim() == '') {
        Swal.fire({
            title: "Bạn cần chọn màu!",
            icon: "error",
            confirmButtonText: "OK"
        });
    } else {
        let cart = JSON.parse(sessionStorage.getItem("cart")) || [];

        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        let sp = cart.find((item) => item.idSPCT === idSPCT && item.idSP === idSP);

        if (sp) {
            sp.quantity = sp.quantity + quantitySP;
        } else {
            cart.push({
                idSP: idSP,
                name: tenSP,
                price: giaSP,
                idSPCT: idSPCT,
                color: tenMS,
                quantity: quantitySP,
            });
        }

        sessionStorage.setItem("cart", JSON.stringify(cart));
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
    }
});

document.querySelectorAll('.custom-radio-div').forEach(item => {
    item.addEventListener('click', function () {
        // Bỏ class selected khoit tất cả các item
        document.querySelectorAll('.custom-radio-div').forEach(el => el.classList.remove('selected'));

        // thêm class "selected" vào item được chọn
        this.classList.add('selected');

        //Lưu lại giá trị được chọn vào input ẩn
        document.getElementById('selectedValue').value = this.getAttribute('data-idspct');
        document.getElementById('selectedValueMS').value = this.getAttribute('data-tenms');
        document.getElementById('so-luong').textContent = this.getAttribute('data-soluong');
        console.log('Selected value: ', this.getAttribute('data-idspct'));
        console.log('So luong: ', this.getAttribute('data-soluong'));
        console.log('Mau sac: ', this.getAttribute('data-tenms'));
    })
})
