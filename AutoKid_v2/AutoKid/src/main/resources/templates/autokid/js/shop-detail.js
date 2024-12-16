$(document).ready(function () {
    // Xóa các nút "-" và "+"
    $(".pro-qty .dec").remove();
    $(".pro-qty .inc").remove();
});

// hàm tăng số lượng
function increaseQuantity() {
    const quantityInput = document.getElementById("quantity-input");
    let quantity = parseInt(quantityInput.value) || 50;
    if (quantity < 50) {
        quantityInput.value = quantity + 1;
    }
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
$("#add-to-cart").on("click", async function (event) {
    event.preventDefault();

    const cartData = JSON.parse(sessionStorage.getItem("cart")) || [];
    const a = document.getElementById("add-to-cart");
    let idSP = a.getAttribute("data-id");
    let tenSP = a.getAttribute("data-name");
    let giaStr = a.getAttribute("data-price").replace(/\./g, "");
    const giaSP = parseFloat(giaStr);
    let quantitySP = parseInt(document.getElementById("quantity-input").value); // so luong muon mua
    let idSPCT = document.getElementById('selectedValue').value;
    let tenMS = document.getElementById('selectedValueMS').value;
    let soLuongSPCT = document.getElementById('so-luong').textContent; // số lượng trong kho
    const anhSP = document.getElementById('image__product_detail').src;
    const anhStr = anhSP.split('/').pop();
    console.log("id spct", idSPCT);

    //Lay san pham chi tiet trong sessionStorage
    let item = cartData.find(product => product.idSPCT === idSPCT);

    console.log("item", item);
    if (item !== undefined) {
        let tongSoLuong = item.quantity + quantitySP;
        if (tongSoLuong > soLuongSPCT - 1) {
            Swal.fire({
                title: "Số lượng sản phẩm quá lớn, hiện tại shop không thể kịp cung cấp!",
                text: "Xin lỗi vì sự bất tiện này",
                icon: "warning",
                confirmButtonText: "OK",
            });
            return;
        }
    }

    console.log(soLuongSPCT, "=======")
    if (soLuongSPCT === null || soLuongSPCT == 0) {
        Swal.fire({
            title: "Sản phẩm này hiện đang hết hàng!",
            text: "Xin lỗi vì sự bất tiện này",
            icon: "warning",
            confirmButtonText: "OK",
        });
        return;
    }
    if (quantitySP > soLuongSPCT - 1) {
        Swal.fire({
            title: "Số lượng sản phẩm quá lớn, hiện tại shop không thể kịp cung cấp!",
            text: "Xin lỗi vì sự bất tiện này",
            icon: "warning",
            confirmButtonText: "OK",
        });
        return;
    }

    console.log("ten", tenMS);
    console.log("idspct", idSPCT);
    if (tenMS === undefined || tenMS === null || tenMS.trim() === '') {
        Swal.fire({
            title: "Bạn cần chọn màu!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    } else {
        const KH = JSON.parse(sessionStorage.getItem("KH")) || [];
        if (KH.length === 0) {
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
                    anhSP: anhStr,
                    quantity: quantitySP,
                });
            }

            sessionStorage.setItem("cart", JSON.stringify(cart));
            updateCartCount();
            updateCartTotal();
            Swal.fire({
                title: "Thêm sản phẩm vào giỏ hàng thành công!",
                icon: "success",
                confirmButtonText: "OK",
            }).then(() => {
                window.location.reload();
            });
        } else {
            const idKH = KH.idKH;
            const payload = {
                idKH: idKH,
                idSPCT: idSPCT,
                soLuong: quantitySP,
            }

            const result = await fetch('/api/add-to-cart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            });
            if (result.ok) {
                Swal.fire({
                    title: "Thêm sản phẩm vào giỏ hàng thành công!",
                    icon: "success",
                    confirmButtonText: "OK",
                }).then(() => {
                    window.location.reload();
                });
            }
        }
    }
});

document.querySelectorAll('.custom-radio-div').forEach(item => {
    item.addEventListener('click', function () {
        // Bỏ class selected khoit tất cả các item
        document.querySelectorAll('.custom-radio-div').forEach(el => el.classList.remove('selected'));

        // thêm class "selected" vào item được chọn
        this.classList.add('selected');

        const anhSPCT = this.getAttribute('data-anhspct');
        const soLuong = this.getAttribute('data-soluong');
        console.log(soLuong);
        if (soLuong === 0) {
            document.getElementById('so-luong').textContent = 'Hết hàng';
        } else {
            document.getElementById('so-luong').textContent = soLuong;
        }

        //Lưu lại giá trị được chọn vào input ẩn
        document.getElementById('selectedValue').value = this.getAttribute('data-idspct');
        document.getElementById('selectedValueMS').value = this.getAttribute('data-tenms');

        const imgE = document.getElementById('image__product_detail');
        imgE.src = '/img/product/' + anhSPCT;

        console.log('Selected value: ', this.getAttribute('data-idspct'));
        console.log('So luong: ', this.getAttribute('data-soluong'));
        console.log('Mau sac: ', this.getAttribute('data-tenms'));
    })
})
