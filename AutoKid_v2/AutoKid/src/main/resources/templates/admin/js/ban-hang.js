

document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("searchCustomerInput");
    const customerResults = document.getElementById("customerResults");
    const customerList = document.getElementById("customerList");
    const customerName = document.getElementById("customerName");
    const customerPhone = document.getElementById("customerPhone");
    const addCustomerBtn = document.getElementById("addCustomerBtn");
    const newCustomerName = document.getElementById("newCustomerName");
    const newCustomerPhone = document.getElementById("newCustomerPhone");
    const saveCustomerBtn = document.getElementById("saveCustomerBtn");

    function removeDuplicates(data) {
        const uniqueData = [];
        const seen = new Set();

        data.forEach(item => {
            if (!seen.has(item.sdt)) {
                seen.add(item.sdt);
                uniqueData.push(item);
            }
        });

        return uniqueData;
    }

    searchInput.addEventListener("input", function () {
        const sdt = searchInput.value.trim();
        if (sdt.length > 0) {
            fetch(`/admin/ban-hang/khachhang/search?sdt=${sdt}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Error fetching customer data');
                    }
                    return response.text();
                })
                .then(text => {
                    try {
                        if (isValidJson(text)) {
                            let data = JSON.parse(text);
                            data = removeDuplicates(data);
                            customerList.innerHTML = "";
                            customerResults.style.display = "block";
                            if (data.length > 0) {
                                data.forEach(khachHang => {
                                    const listItem = document.createElement("li");
                                    listItem.className = "list-group-item d-flex justify-content-between align-items-center";
                                    listItem.textContent = `${khachHang.tenKH} - ${khachHang.sdt}`;
                                    listItem.addEventListener("click", function () {
                                        customerName.textContent = khachHang.tenKH;
                                        customerPhone.textContent = khachHang.sdt;
                                        customerResults.style.display = "none";
                                        searchInput.value = '';
                                        saveCustomerToSession(khachHang.sdt);
                                        event.stopPropagation();
                                    });

                                    customerList.appendChild(listItem);
                                });
                            } else {
                                const noResultItem = document.createElement("li");
                                noResultItem.className = "list-group-item";
                                noResultItem.textContent = "Không tìm thấy khách hàng.";
                                customerList.appendChild(noResultItem);
                                addCustomerBtn.style.display = "inline-block";
                            }
                        } else {
                            console.error("Dữ liệu trả về không hợp lệ:", text);
                        }
                    } catch (error) {
                        console.error("Lỗi khi chuyển đổi dữ liệu JSON:", error);
                    }
                })
                .catch(error => console.error("Error fetching customer data:", error));
        } else {
            customerResults.style.display = "none";
        }
    });
    function isValidJson(jsonString) {
        try {
            JSON.parse(jsonString);
            return true;
        } catch (e) {
            return false;
        }
    }

    if (addCustomerBtn) {
        addCustomerBtn.addEventListener("click", function () {
            newCustomerPhone.value = searchInput.value.trim();
            const myModal = new bootstrap.Modal(document.getElementById('addCustomerModal'));
            myModal.show(); // Mở modal
        });
    } else {

    }

    saveCustomerBtn.addEventListener("click", function () {
        const tenKH = newCustomerName.value.trim();
        const sdt = newCustomerPhone.value.trim();
        if (tenKH && sdt) {
            saveCustomerBtn.disabled = true;

            fetch('/admin/ban-hang/khach-hang/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    tenKH: tenKH,
                    sdt: sdt
                })
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Lỗi trong quá trình thêm khách hàng!');
                    }
                    return response.json();
                })
                .then(data => {
                    customerName.textContent = tenKH;
                    customerPhone.textContent = sdt;
                    const modalElement = document.querySelector('#addCustomerModal');
                    const modalInstance = bootstrap.Modal.getInstance(modalElement);
                    modalInstance.hide();

                    const toast = new bootstrap.Toast(document.getElementById('successToast'));
                    toast.show();

                    saveCustomerToSession(sdt);
                    searchInput.value = '';
                    customerResults.style.display = "none";
                })
                .catch(error => {
                    console.error('Error saving customer:', error);
                    alert('Đã xảy ra lỗi, vui lòng thử lại!');
                })
                .finally(() => {
                    // Bật lại nút sau khi xử lý xong
                    saveCustomerBtn.disabled = false;
                });
        }
    });

    // Lưu khách hàng vào session
    function saveCustomerToSession(sdt) {
        fetch('/admin/ban-hang/set-session', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ sdt: sdt })
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(errorMessage => {
                        throw new Error(errorMessage);
                    });
                }
                return response.text();
            })
            .then(responseText => {
                try {
                    const cleanedResponse = responseText.replace(/}}]}}]}}]/g, '}');
                    const customer = JSON.parse(cleanedResponse);
                    localStorage.setItem("customerData", JSON.stringify(customer));
                } catch (error) {
                    throw new Error("Invalid JSON response: " + error.message);
                }
            })
            .catch(error => {
                console.error("Error setting customer to session:", error.message);
                alert(error.message);
            });
    }

});


document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.getElementById("searchProductInput");
    const productResults = document.getElementById("productResults");
    const productList = document.getElementById("productList");
    const cart = JSON.parse(sessionStorage.getItem("cart")) || []; // Giỏ hàng lưu trong Session Storage
    const quantityModal = new bootstrap.Modal(document.getElementById('quantityModal'));
    let selectedProduct = null; // Lưu trữ sản phẩm đã chọn
    let selectedOrder = null; // Lưu trữ đơn hàng đã chọn

    // Lưu trữ danh sách hóa đơn trong sessionStorage
    let invoices = JSON.parse(sessionStorage.getItem("invoices")) || [];

    if (!sessionStorage.getItem("cart")) {
        sessionStorage.setItem("cart", JSON.stringify({})); // Khởi tạo cart nếu chưa có
    }
    // Hàm hiển thị danh sách hóa đơn
    function renderInvoices() {
        const invoiceContainer = document.getElementById("invoiceContainer");
        invoiceContainer.innerHTML = ""; // Xóa nội dung cũ

        invoices.forEach((invoice, index) => {
            const invoiceItem = document.createElement("div");
            invoiceItem.className = "invoice-item";

            // Tên hóa đơn
            const invoiceName = document.createElement("span");
            invoiceName.className = "invoice-name";
            invoiceName.textContent = `Đơn hàng ${index + 1}`;

            // Biểu tượng xóa
            const deleteButton = document.createElement("button");
            deleteButton.className = "delete-button";
            deleteButton.textContent = "×"; // Dấu "X"
            deleteButton.addEventListener("click", () => {
                deleteInvoice(index);
            });

            // Xử lý khi nhấn vào đơn hàng
            invoiceItem.addEventListener("click", () => {
                selectOrder(invoice);
                // Hiển thị giỏ hàng của hóa đơn khi click
                renderCart(invoice);
                // Thêm hoặc bớt class để thể hiện đơn hàng đang được chọn
                const allInvoiceItems = document.querySelectorAll('.invoice-item');
                allInvoiceItems.forEach((item, idx) => {
                    item.classList.toggle('selected', idx === index);
                });
            });

            invoiceItem.appendChild(invoiceName);
            invoiceItem.appendChild(deleteButton);
            invoiceContainer.appendChild(invoiceItem);
            console.log('Đơn hàng đã chọn:', selectedOrder);
        });
    }

    // Hàm xóa hóa đơn
    function deleteInvoice(index) {
        // Xóa hóa đơn khỏi danh sách
        invoices.splice(index, 1);

        // Xóa giỏ hàng của hóa đơn khỏi cart
        let storedCart = JSON.parse(sessionStorage.getItem("cart")) || [];

        // Nếu cart có đủ phần tử, xóa phần tử tại index
        if (Array.isArray(storedCart) && storedCart.length > index) {
            storedCart.splice(index, 1);
        }
        sessionStorage.setItem("cart", JSON.stringify(storedCart));
        sessionStorage.setItem("invoices", JSON.stringify(invoices));
        renderInvoices();
        showToast('deleteInvoiceToast');
    }

    document.getElementById("addInvoiceButton").addEventListener("click", () => {
        if (invoices.length >= 10) {
            showToast('maxInvoiceToast');
            return;
        }
        const newInvoice = `Hóa đơn ${invoices.length + 1}`;
        invoices.push(newInvoice);
        let storedCart = JSON.parse(sessionStorage.getItem("cart"));
        if (!Array.isArray(storedCart)) {
            storedCart = [];
        }

        // Tạo giỏ hàng trống cho hóa đơn mới
        storedCart.push([]); // Thêm giỏ hàng trống cho hóa đơn mới

        // Lưu danh sách hóa đơn và giỏ hàng vào Session Storage
        sessionStorage.setItem("cart", JSON.stringify(storedCart));
        sessionStorage.setItem("invoices", JSON.stringify(invoices));

        // Cập nhật giao diện
        renderInvoices();
        showToast('invoiceToast');
    });



    // Hàm hiển thị Toast thông báo
    function showToast(toastId) {
        const toastElement = document.getElementById(toastId);
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
    }
    // Khởi tạo giao diện khi tải trang
    renderInvoices();


    // Kiểm tra và lấy lại thông tin đơn hàng đã chọn từ sessionStorage
    const storedSelectedOrder = JSON.parse(sessionStorage.getItem("selectedOrder"));
    if (storedSelectedOrder) {
        selectedOrder = storedSelectedOrder;
        console.log("Đơn hàng đã chọn từ sessionStorage:", selectedOrder);
        // Có thể thêm mã để hiển thị giỏ hàng của đơn hàng đã chọn
        const selectedInvoiceIndex = invoices.findIndex(invoice => invoice === selectedOrder);
        if (selectedInvoiceIndex >= 0) {
            renderCart(selectedInvoiceIndex); // Hiển thị giỏ hàng của đơn hàng đã chọn
            const allInvoiceItems = document.querySelectorAll('.invoice-item');
            allInvoiceItems.forEach((item, idx) => {
                item.classList.toggle('selected', idx === selectedInvoiceIndex); // Đánh dấu đơn hàng đã chọn
            });
        }
    } else {
        console.log("Chưa có đơn hàng được chọn.");
    }

    searchInput.addEventListener("input", function () {
        const tenSP = searchInput.value.trim(); // Lấy tên sản phẩm người dùng nhập
        const maSPCT = searchInput.value.trim(); // Bạn có thể lấy mã SPCT từ một input khác nếu cần

        if (tenSP.length > 0 || maSPCT.length > 0) {
            // Gọi API để tìm sản phẩm theo tên sản phẩm hoặc mã sản phẩm chi tiết
            fetch(`/admin/ban-hang/san-pham-chi-tiet/search?tenSP=${tenSP}&maSPCT=${maSPCT}`)
                .then(response => response.json())
                .then(data => {
                    productList.innerHTML = ""; // Xóa danh sách cũ
                    productResults.style.display = "block"; // Hiển thị danh sách sản phẩm

                    if (data.length > 0) {
                        data.forEach(sanPham => {
                            // Tạo dòng trong bảng
                            const row = document.createElement("tr");
                            row.innerHTML = `
                            <td>${sanPham.maSPCT}</td>
                            <td>${sanPham.tenSP}</td>
                            <td>${sanPham.donGia}</td>
                            <td>${sanPham.soLuong}</td>
                            <td>${sanPham.mauSac}</td>
                            <td>${sanPham.thuongHieu}</td>
                            <td>${sanPham.chatLieu}</td>
                            <td>${sanPham.kichCo}</td>
                            <td class="text-center btn btn-primary"><i class="bi bi-bag-check"></i></td>
                        `;
                            row.addEventListener("click", function () {
                                selectedProduct = sanPham; // Lưu sản phẩm đã chọn
                                document.getElementById("productQuantity").value = 1;
                                quantityModal.show();
                                productResults.style.display = "none";
                                searchInput.value = "";
                                event.stopPropagation();
                            });
                            productList.appendChild(row);
                        });
                    } else {
                        const noResultItem = document.createElement("li");
                        noResultItem.className = "list-group-item";
                        noResultItem.textContent = "Không tìm thấy sản phẩm.";
                        productList.appendChild(noResultItem);
                    }
                })
                .catch(error => console.error("Error fetching product data:", error));
        } else {
            productResults.style.display = "none";
        }
    });

    document.addEventListener("click", function (event) {
        const isClickInsideSearch = searchInput.contains(event.target) || productResults.contains(event.target) || customerResults.contains(event.target);
        const isClickInsideProduct = productResults.contains(event.target);
        const isClickInsideCustomer = customerResults.contains(event.target);
        if (!isClickInsideSearch && !isClickInsideProduct && !isClickInsideCustomer) {
            customerResults.style.display = "none";
            productResults.style.display = "none";
        }
    });


    function addProductToCart(product, quantity) {
        if (!selectedOrder) {
            alert("Vui lòng chọn đơn hàng trước!");
            return;
        }

        console.log("Đơn hàng đã chọn:", selectedOrder);
        let storedCart = JSON.parse(sessionStorage.getItem("cart")) || [];
        const orderIndex = invoices.indexOf(selectedOrder); // Tìm vị trí của đơn hàng trong mảng invoices
        if (orderIndex === -1) {
            console.log("Không tìm thấy đơn hàng");
            return;
        }
        if (!storedCart[orderIndex]) {
            storedCart[orderIndex] = [];
            console.log(`Tạo giỏ hàng mới cho đơn hàng: ${selectedOrder}`);
        }

        const currentCart = storedCart[orderIndex];
        console.log("Giỏ hàng trước khi thay đổi:", currentCart);
        const existingItem = currentCart.find(item => item.maSPCT === product.maSPCT);
        if (existingItem) {
            existingItem.soLuong += quantity;
            existingItem.thanhTien = existingItem.soLuong * existingItem.donGia;
            console.log(`Cập nhật sản phẩm ${existingItem.maSPCT}: Số lượng mới = ${existingItem.soLuong}`);
        } else {
            currentCart.push({
                maSPCT: product.maSPCT,
                tenSP: product.tenSP,
                mauSac:product.mauSac,
                chatLieu:product.chatLieu,
                kichCo:product.kichCo,
                donGia: product.donGia,
                soLuong: quantity,
                thanhTien: product.donGia * quantity
            });
        }
        storedCart[orderIndex] = currentCart;
        console.log("Giỏ hàng sau khi thay đổi:", currentCart);
        sessionStorage.setItem("cart", JSON.stringify(storedCart));
        renderCart(selectedOrder);
    }
// Hàm render giỏ hàng
    function renderCart(selectedOrder) {
        console.log("Đang render giỏ hàng cho đơn hàng:", selectedOrder);

        // Lấy giỏ hàng từ sessionStorage
        const storedCart = JSON.parse(sessionStorage.getItem("cart")) || [];
        const orderIndex = invoices.indexOf(selectedOrder);
        let currentCart = storedCart[orderIndex] || []; // Giỏ hàng của đơn hàng đã chọn

        console.log("Giỏ hàng hiện tại: ", currentCart);

        const cartTableBody = document.getElementById("cartTableBody");
        cartTableBody.innerHTML = ""; // Xóa các sản phẩm cũ
        let totalAmount = 0;
        let totalQuantity = 0; // Tổng số lượng sản phẩm

        // Kiểm tra xem giỏ hàng có sản phẩm không
        if (currentCart.length > 0) {
            currentCart.forEach((product, index) => {
                const row = document.createElement("tr");

                row.innerHTML = `
                     <td class="productId">${product.maSPCT}</td>
                    <td class="productName">${product.tenSP}<br>
                    <small>Chất liệu: ${product.chatLieu || "Không có"}</small>, 
                    <small>Màu sắc: ${product.mauSac || "Không có"}</small>, 
                    <small>Kích cỡ: ${product.kichCo || "Không có"}</small>
                    </td>
                    <td class="productPrice">${product.donGia}</td>
                    <td class="productQuantity">
                        <input type="number" value="${product.soLuong}" min="1" class="quantity-input custom-width" data-index="${index}">
                    </td>
                    <td class="thanhTien">${product.thanhTien}</td>
            `;
                cartTableBody.appendChild(row);
                totalAmount += product.thanhTien;
                totalQuantity += product.soLuong; // Cộng dồn số lượng
            });
        } else {
            // Hiển thị thông báo giỏ hàng trống nếu không có sản phẩm
            const row = document.createElement("tr");
            row.innerHTML = `<td colspan="5" class="text-center">Giỏ hàng trống</td>`;
            cartTableBody.appendChild(row);
        }

        // Cập nhật thông tin tổng cộng và tổng số lượng
        updateSummaryTable(totalAmount, totalQuantity);

        // Lắng nghe sự kiện thay đổi số lượng
        document.querySelectorAll('.quantity-input').forEach(input => {
            input.addEventListener('change', (event) => {
                const index = event.target.getAttribute('data-index');
                const newQuantity = parseInt(event.target.value);

                // Cập nhật số lượng trong currentCart
                if (newQuantity > 0) {
                    currentCart[index].soLuong = newQuantity;
                    currentCart[index].thanhTien = currentCart[index].donGia * newQuantity; // Cập nhật lại thành tiền
                }

                // Cập nhật cột thành tiền của sản phẩm tương ứng
                const row = event.target.closest("tr");
                const thanhTienCell = row.querySelector(".thanhTien");
                if (thanhTienCell) {
                    thanhTienCell.textContent = currentCart[index].thanhTien; // Cập nhật cột Thành tiền
                }

                // Cập nhật lại giỏ hàng trong sessionStorage
                storedCart[orderIndex] = currentCart;
                sessionStorage.setItem("cart", JSON.stringify(storedCart));

                // Cập nhật lại tổng tiền và tổng số lượng
                totalAmount = currentCart.reduce((total, product) => total + product.thanhTien, 0);
                totalQuantity = currentCart.reduce((total, product) => total + product.soLuong, 0);

                // Cập nhật thông tin bảng tổng quát
                updateSummaryTable(totalAmount, totalQuantity);
            });
        });
    }

// Hàm cập nhật thông tin bảng tổng quát
    function updateSummaryTable(totalAmount, totalQuantity) {
        const totalQuantityField = document.querySelector("table#summaryTable tbody tr:nth-child(1) td");
        const totalField = document.querySelector("table#summaryTable tbody tr:nth-child(2) td");
        const voucherField = document.getElementById("voucherSelect");
        const finalAmountField = document.querySelector("table#summaryTable tbody tr:nth-child(4) td");
        const paymentField = document.querySelector("table#summaryTable tbody tr:nth-child(5) input");
        const changeField = document.querySelector("table#summaryTable tbody tr:nth-child(6) td");

        // Cập nhật tổng số lượng
        if (totalQuantityField) {
            totalQuantityField.textContent = totalQuantity;
        }

        // Cập nhật tổng cộng
        if (totalField) {
            totalField.textContent = `${totalAmount} VNĐ`;
        }

        // Tính thành tiền (áp dụng voucher nếu có)
        let finalAmount = totalAmount;
        if (voucherField) {
            const selectedVoucher = voucherField.value;
            if (selectedVoucher === "voucher1") {
                finalAmount = totalAmount * 0.9; // Giảm 10%
            } else if (selectedVoucher === "voucher2") {
                finalAmount = totalAmount - 50000; // Giảm 50k
            } else if (selectedVoucher === "voucher3") {
                finalAmount = totalAmount * 0.85; // Giảm 15%
            }
        }

        // Cập nhật thành tiền
        if (finalAmountField) {
            finalAmountField.textContent = `${finalAmount} VNĐ`;
        }

        // Tự động cập nhật tiền thừa
        if (paymentField && changeField) {
            const customerPay = parseInt(paymentField.value) || 0;
            const change = customerPay - finalAmount;

            // Cập nhật tiền thừa, cho phép là âm khi tiền khách trả ít hơn
            changeField.textContent = `${change} VNĐ`;
        }

        // Lắng nghe sự kiện thay đổi voucher
        if (voucherField) {
            voucherField.addEventListener("change", () => {
                updateSummaryTable(totalAmount, totalQuantity);
            });
        }

        // Lắng nghe sự kiện thay đổi tiền khách trả
        if (paymentField) {
            paymentField.addEventListener("input", () => {
                updateSummaryTable(totalAmount, totalQuantity);
            });
        }
    }

// Hàm để thay đổi đơn hàng
    function selectOrder(order) {
        selectedOrder = order;
        console.log("Đã chọn đơn hàng:", selectedOrder);
        renderCart(selectedOrder);
    }





    let isUpdating = false; // Trạng thái để kiểm tra liệu đã có yêu cầu đang xử lý hay chưa

    document.getElementById("addToCartButton").addEventListener("click", async function () {
        if (isUpdating) return; // Nếu đang cập nhật, không thực hiện thêm lần nữa

        isUpdating = true; // Đánh dấu trạng thái đang cập nhật

        const quantity = parseInt(document.getElementById("productQuantity").value);
        const productId = selectedProduct.maSPCT;

        console.log("Số lượng sản phẩm:", quantity);
        console.log("Sản phẩm đã chọn:", selectedProduct);
        console.log("Đơn hàng đã chọn:", selectedOrder);

        if (!selectedOrder) {
            alert("Vui lòng chọn đơn hàng trước!");
            isUpdating = false; // Khôi phục trạng thái nếu không chọn đơn hàng
            return;
        }

        // Kiểm tra số lượng từ cơ sở dữ liệu
        try {
            const response = await fetch(`/admin/ban-hang/san-pham-chi-tiet/${productId}/get-quantity`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });

            const result = await response.json();
            const availableQuantity = result.soLuong;

            if (quantity <= 0 || quantity > availableQuantity) {
                alert(`Số lượng sản phẩm không hợp lệ! Hiện tại chỉ còn ${availableQuantity} sản phẩm.`);
                isUpdating = false; // Khôi phục trạng thái nếu có lỗi
                return;
            }

            // Gửi yêu cầu cập nhật số lượng sản phẩm lên server
            try {
                const updateResponse = await fetch(`/admin/ban-hang/san-pham-chi-tiet/${productId}/update-quantity`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ soLuong: quantity }) // Truyền số lượng muốn trừ đi
                });

                const updateResult = await updateResponse.json();
                if (updateResult.success) {
                    addProductToCart(selectedProduct, quantity);
                    alert("Sản phẩm đã được cập nhật vào giỏ hàng và cơ sở dữ liệu!");
                } else {
                    alert("Có lỗi xảy ra khi cập nhật sản phẩm!");
                }
            } catch (error) {
                console.error("Error updating product quantity:", error);
                alert("Có lỗi xảy ra khi cập nhật sản phẩm!");
            }
        } catch (error) {
            console.error("Error fetching product quantity:", error);
            alert("Có lỗi xảy ra khi kiểm tra số lượng sản phẩm!");
        } finally {
            isUpdating = false; // Khôi phục trạng thái
        }
    });


// Hàm hiển thị Toast thông báo
    function showToast(toastId) {
        const toastElement = document.getElementById(toastId);
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
    }

});
// Khi tải lại trang
document.addEventListener("DOMContentLoaded", function () {
    const storedSelectedOrder = JSON.parse(sessionStorage.getItem("selectedOrder"));
    if (storedSelectedOrder) {
        selectedOrder = storedSelectedOrder;
        console.log("Đơn hàng đã chọn từ sessionStorage:", selectedOrder);
    } else {
        console.log("Chưa có đơn hàng được chọn.");
    }
});

document.getElementById("paymentButton").addEventListener("click", async function () {
    // Hiển thị thông báo xác nhận thanh toán
    const confirmPayment = confirm("Bạn có chắc chắn muốn thanh toán?");

    if (confirmPayment) {
        // Lấy thông tin giỏ hàng
        const cartItems = [];
        let totalAmount = 0;
        let totalQuantity = 0;

        const productRows = document.querySelectorAll("#cartTableBody tr");
        productRows.forEach(row => {
            const productId = row.querySelector(".productId").textContent.trim();
            const productName = row.querySelector(".productName").textContent.trim();
            const productPrice = parseFloat(row.querySelector(".productPrice").textContent);
            const productQuantity = parseInt(row.querySelector(".productQuantity input").value);
            const totalPrice = productPrice * productQuantity;

            cartItems.push({
                productId,
                productName,
                productPrice,
                productQuantity,
                totalPrice
            });

            totalAmount += totalPrice;
            totalQuantity += productQuantity;

        });

        console.log(cartItems)
        // // Kiểm tra giỏ hàng có sản phẩm hay không
        // if (cartItems.length === 0) {
        //     alert("Giỏ hàng không có sản phẩm!");
        //     return;
        // }

        // Lấy thông tin khách hàng từ localStorage
        const customerData = JSON.parse(localStorage.getItem("customerData"));
        console.log(customerData)
        if (!customerData || !customerData.idKH) {
            alert("Thông tin khách hàng không hợp lệ hoặc không có trong hệ thống!");
            return;
        }

        const customerId = customerData.idKH;

        // Lấy thông tin thanh toán từ giao diện
        const paymentType = document.getElementById("paymentTypeSelect").value;
        const voucher = document.getElementById("voucherSelect").value;
        const customerPaid = parseFloat(document.querySelector("#summaryTable input[type='text']").value);

        // Kiểm tra số tiền thanh toán
        if (isNaN(customerPaid) || customerPaid < totalAmount) {
            alert("Số tiền khách hàng thanh toán không hợp lệ hoặc chưa đủ!");
            return;
        }

        console.log(sessionStorage.getItem("infoNV"));

        // Lấy thông tin nhân viên từ localStorage
        const employeeData = JSON.parse(sessionStorage.getItem("infoNV"));
        console.log(employeeData)
        if (!employeeData || !employeeData.id) {
            alert("Thông tin nhân viên không hợp lệ hoặc không có trong hệ thống!");
            return;
        }

        const employeeId = employeeData.id;

        // Tạo đối tượng hóa đơn
        const invoice = {
            id_kh: customerId,
            khachHang: {
                id: customerId
            },
            id_nv: employeeId,
            nhanVien: {
                id: employeeId
            },
            totalAmount,
            totalQuantity,
            paymentType,
            voucher,
            cartItems
        };

        console.log("Sending invoice:", JSON.stringify(invoice));

        try {
            const response = await fetch('/admin/ban-hang/create-invoice', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(invoice)
            });

            if (response.ok) {
                const result = await response.json();
                if (result.success) {
                    // Hiển thị thông báo thành công
                    const toast = new bootstrap.Toast(document.getElementById("invoiceToast"));
                    toast.show();

                    // Reset giỏ hàng và cập nhật bảng tổng kết
                    resetCart();
                    updateSummary();
                } else {
                    alert("Đã có lỗi khi tạo hóa đơn: " + result.error);
                    console.error("Lỗi từ server:", result.error);
                }
            } else {
                const errorData = await response.text();
                alert("Đã có lỗi khi gửi yêu cầu! Mã lỗi: " + response.status);
                console.error("Chi tiết lỗi:", errorData);
            }
        } catch (error) {
            console.error("Lỗi khi tạo hóa đơn:", error);
            alert("Đã có lỗi khi tạo hóa đơn! Vui lòng thử lại.");
        }
    }
});

// Hàm reset giỏ hàng
function resetCart() {
    document.getElementById("cartTableBody").innerHTML = "";
}

// Cập nhật bảng tóm tắt thông tin hóa đơn
function updateSummary() {
    const productRows = document.querySelectorAll("#cartTableBody tr");
    let totalAmount = 0;
    let totalQuantity = 0;

    productRows.forEach(row => {
        const productPrice = parseFloat(row.querySelector(".productPrice").textContent);
        const productQuantity = parseInt(row.querySelector(".productQuantity").textContent);
        const totalPrice = productPrice * productQuantity;

        totalAmount += totalPrice;
        totalQuantity += productQuantity;
    });

    // Cập nhật thông tin vào bảng tổng kết
    document.querySelector("#summaryTable tbody tr:nth-child(1) td").textContent = totalQuantity;  // Cập nhật số lượng
    document.querySelector("#summaryTable tbody tr:nth-child(2) td").textContent = totalAmount.toFixed(2);  // Cập nhật tổng tiền
    document.querySelector("#summaryTable tbody tr:nth-child(4) td").textContent = totalAmount.toFixed(2);  // Cập nhật vào dòng 4
    document.querySelector("#summaryTable tbody tr:nth-child(6) td").textContent = totalAmount.toFixed(2);  // Cập nhật vào dòng 6

    // Tính số tiền trả lại
    const customerPaid = parseFloat(document.querySelector("#summaryTable input[type='text']").value);
    const changeAmount = customerPaid - totalAmount;

    // Cập nhật số tiền thay đổi (nếu có)
    document.querySelector("#summaryTable tbody tr:nth-child(7) td").textContent = changeAmount.toFixed(2);
}







