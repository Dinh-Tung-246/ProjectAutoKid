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

    // Sự kiện tìm kiếm khách hàng
    searchInput.addEventListener("input", function () {
        const sdt = searchInput.value.trim(); // Lấy số điện thoại người dùng nhập

        if (sdt.length > 0) {
            // Gọi API để tìm khách hàng dựa trên số điện thoại
            fetch(`/admin/ban-hang/khachhang/search?sdt=${sdt}`)
                .then(response => response.json())
                .then(data => {
                    customerList.innerHTML = "";
                    customerResults.style.display = "block"; // Hiển thị danh sách khách hàng

                    if (data.length > 0) {
                        data.forEach(khachHang => {
                            const listItem = document.createElement("li");
                            listItem.className = "list-group-item d-flex justify-content-between align-items-center";
                            listItem.textContent = `${khachHang.tenKH} - ${khachHang.sdt}`;

                            // Thêm sự kiện click vào dòng khách hàng
                            listItem.addEventListener("click", function () {
                                customerName.textContent = khachHang.tenKH;
                                customerPhone.textContent = khachHang.sdt;
                                customerResults.style.display = "none"; // Ẩn kết quả tìm kiếm khi chọn khách hàng
                            });

                            customerList.appendChild(listItem);
                        });
                    } else {
                        const noResultItem = document.createElement("li");
                        noResultItem.className = "list-group-item";
                        noResultItem.textContent = "Không tìm thấy khách hàng.";
                        customerList.appendChild(noResultItem);

                        // Hiển thị nút thêm khách hàng
                        addCustomerBtn.style.display = "inline-block";
                    }
                })
                .catch(error => console.error("Error fetching customer data:", error));
        } else {
            customerResults.style.display = "none"; // Ẩn kết quả khi không có nhập
        }
    });

    // Mở modal thêm khách hàng khi ấn nút "+"
    if (addCustomerBtn) {
        addCustomerBtn.addEventListener("click", function () {
            // Điền số điện thoại vào form thêm khách hàng trong modal
            newCustomerPhone.value = searchInput.value.trim();
            const myModal = new bootstrap.Modal(document.getElementById('addCustomerModal'));
            myModal.show(); // Mở modal
        });
    } else {
        console.error("Nút thêm khách hàng không tồn tại.");
    }

    // Lưu khách hàng mới
    saveCustomerBtn.addEventListener("click", function () {
        const tenKH = newCustomerName.value.trim();
        const sdt = newCustomerPhone.value.trim();

        if (tenKH && sdt) {
            // Gọi API để thêm khách hàng mới
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
                .then(response => response.json())
                .then(data => {
                    // Cập nhật tên và số điện thoại khi thêm khách hàng thành công
                    customerName.textContent = tenKH;
                    customerPhone.textContent = sdt;
                    // Đóng modal và ẩn nút thêm khách hàng
                    const modalElement = document.querySelector('#addCustomerModal');
                    const modalInstance = bootstrap.Modal.getInstance(modalElement);
                    modalInstance.hide();

                    // Hiển thị thông báo thành công
                    const toast = new bootstrap.Toast(document.getElementById('successToast'));
                    toast.show();


                    searchInput.value = ''; // Xóa ô nhập tìm kiếm
                    customerResults.style.display = "none"; // Ẩn kết quả tìm kiếm
                })
                .catch(error => console.error('Error saving customer:', error));
        }
    });

    // Đóng bảng khách hàng khi người dùng click ra ngoài ô input hoặc bảng kết quả
    document.addEventListener("click", function (event) {
        if (!searchInput.contains(event.target) && !customerResults.contains(event.target)) {
            customerResults.style.display = "none"; // Ẩn bảng kết quả
        }
    });
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

        // Cập nhật Session Storage
        sessionStorage.setItem("cart", JSON.stringify(storedCart));
        sessionStorage.setItem("invoices", JSON.stringify(invoices));

        // Cập nhật giao diện
        renderInvoices();
        showToast('deleteInvoiceToast');
    }

    document.getElementById("addInvoiceButton").addEventListener("click", () => {
        // Kiểm tra số lượng hóa đơn đã có, nếu vượt quá 10 thì không cho tạo thêm
        if (invoices.length >= 10) {
            showToast('maxInvoiceToast');
            return; // Dừng lại không tạo hóa đơn mới
        }

        // Tạo một hóa đơn mới
        const newInvoice = `Hóa đơn ${invoices.length + 1}`;
        invoices.push(newInvoice); // Thêm vào danh sách hóa đơn

        // Lấy giỏ hàng hiện tại từ sessionStorage
        let storedCart = JSON.parse(sessionStorage.getItem("cart"));

        // Nếu storedCart không phải là mảng (null hoặc không hợp lệ), tạo một mảng mới
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

    // Sự kiện tìm kiếm sản phẩm
    searchInput.addEventListener("input", function () {
        const tenSP = searchInput.value.trim(); // Lấy tên sản phẩm người dùng nhập

        if (tenSP.length > 0) {
            // Gọi API để tìm sản phẩm
            fetch(`/admin/ban-hang/san-pham-chi-tiet/search?tenSP=${tenSP}`)
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
                                document.getElementById("productQuantity").value = 1; // Đặt số lượng mặc định là 1
                                quantityModal.show(); // Hiển thị modal nhập số lượng
                                productResults.style.display = "none"; // Ẩn kết quả tìm kiếm khi chọn sản phẩm
                            });

                            // Thêm dòng vào bảng
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
            productResults.style.display = "none"; // Ẩn kết quả khi không có nhập
        }
    });

    // Đóng bảng sản phẩm khi người dùng click ra ngoài ô input hoặc bảng kết quả
    document.addEventListener("click", function (event) {
        if (!searchInput.contains(event.target) && !productResults.contains(event.target)) {
            productResults.style.display = "none"; // Ẩn bảng kết quả
        }
    });
    function addProductToCart(product, quantity) {
        if (!selectedOrder) {
            alert("Vui lòng chọn đơn hàng trước!");
            return;
        }

        console.log("Đơn hàng đã chọn:", selectedOrder);

        // Lấy giỏ hàng từ sessionStorage, nếu không có thì tạo mảng mới
        let storedCart = JSON.parse(sessionStorage.getItem("cart")) || [];

        // Tạo giỏ hàng cho đơn hàng nếu chưa có
        const orderIndex = invoices.indexOf(selectedOrder); // Tìm vị trí của đơn hàng trong mảng invoices
        if (orderIndex === -1) {
            console.log("Không tìm thấy đơn hàng");
            return;
        }

        // Nếu giỏ hàng của đơn hàng chưa có, tạo một mảng trống
        if (!storedCart[orderIndex]) {
            storedCart[orderIndex] = [];
            console.log(`Tạo giỏ hàng mới cho đơn hàng: ${selectedOrder}`);
        }

        const currentCart = storedCart[orderIndex]; // Giỏ hàng của đơn hàng đã chọn
        console.log("Giỏ hàng trước khi thay đổi:", currentCart);

        // Kiểm tra nếu sản phẩm đã có trong giỏ hàng
        const existingItem = currentCart.find(item => item.maSPCT === product.maSPCT);
        if (existingItem) {
            // Cập nhật số lượng và thành tiền
            existingItem.soLuong += quantity;
            existingItem.thanhTien = existingItem.soLuong * existingItem.donGia;
            console.log(`Cập nhật sản phẩm ${existingItem.maSPCT}: Số lượng mới = ${existingItem.soLuong}`);
        } else {
            // Thêm sản phẩm mới vào giỏ hàng
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
            console.log(`Thêm sản phẩm mới ${product.maSPCT}: Số lượng = ${quantity}`);
        }

        // Lưu lại giỏ hàng vào sessionStorage
        storedCart[orderIndex] = currentCart;
        console.log("Giỏ hàng sau khi thay đổi:", currentCart);

        // Lưu lại toàn bộ cart vào sessionStorage
        sessionStorage.setItem("cart", JSON.stringify(storedCart));

        // Render lại giỏ hàng
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
                <td>${product.maSPCT}</td>
                <td>
                    ${product.tenSP}<br>
                    <small>Chất liệu: ${product.chatLieu || "Không có"}</small>, 
                    <small>Màu sắc: ${product.mauSac || "Không có"}</small>, 
                    <small>Kích cỡ: ${product.kichCo || "Không có"}</small>
                </td>
                <td>${product.donGia}</td>
                <td>
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





    document.getElementById("addToCartButton").addEventListener("click", async function () {
        const quantity = parseInt(document.getElementById("productQuantity").value);
        const maxQuantity = selectedProduct.soLuong; // Số lượng có sẵn trong cơ sở dữ liệu

        console.log("Số lượng sản phẩm:", quantity);
        console.log("Sản phẩm đã chọn:", selectedProduct);
        console.log("Đơn hàng đã chọn:", selectedOrder);

        if (!selectedOrder) {
            alert("Vui lòng chọn đơn hàng trước!");
            return;
        }

        // Kiểm tra nếu số lượng vượt quá số lượng có sẵn trong cơ sở dữ liệu
        if (quantity <= 0 || quantity > maxQuantity) {
            // Hiển thị thông báo lỗi nếu số lượng không hợp lệ
            const modalElement = document.querySelector('#quantityModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement);
            modalInstance.hide();
            showToast('quantityErrorToast');
            return;
        }

        // Nếu số lượng hợp lệ, tiếp tục cập nhật giỏ hàng
        if (quantity > 0 && selectedProduct) {
            // Cập nhật giỏ hàng trong sessionStorage
            addProductToCart(selectedProduct, quantity);

            try {
                // Gửi yêu cầu cập nhật số lượng sản phẩm lên server
                const response = await fetch(`/admin/ban-hang/san-pham-chi-tiet/${selectedProduct.maSPCT}/update-quantity`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ soLuong: quantity })
                });

                const result = await response.json();
                if (result.success) {
                    alert("Sản phẩm đã được cập nhật vào giỏ hàng và cơ sở dữ liệu!");
                } else {
                    alert("Có lỗi xảy ra khi cập nhật sản phẩm!");
                }
            } catch (error) {
                console.error("Error updating product quantity:", error);
            }

            quantityModal.hide(); // Đóng modal
        } else {
            alert("Số lượng không hợp lệ!");
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




