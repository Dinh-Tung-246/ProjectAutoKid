
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

    function renderInvoices() {
        const invoiceContainer = document.getElementById("invoiceContainer");
        invoiceContainer.innerHTML = ""; // Xóa nội dung cũ

        invoices.forEach((invoice, index) => {
            const invoiceItem = document.createElement("div");
            invoiceItem.className = "invoice-item";


            // Tên hóa đơn hiển thị từ danh sách `invoices`
            const invoiceName = document.createElement("span");
            invoiceName.className = "invoice-name";
            invoiceName.textContent = invoice; // Hiển thị đúng tên hóa đơn

            // Biểu tượng xóa
            const deleteButton = document.createElement("button");
            deleteButton.className = "delete-button";
            deleteButton.textContent = "×"; // Dấu "X"
            deleteButton.addEventListener("click", (event) => {
                event.stopPropagation(); // Ngăn không cho sự kiện click trên hóa đơn được gọi khi nhấn nút xóa
                deleteInvoice(index); // Xóa đơn hàng tại vị trí index
            });

            // Xử lý khi nhấn vào đơn hàng
            invoiceItem.addEventListener("click", () => {
                selectOrder(invoice); // Cập nhật đơn hàng đã chọn
                const allInvoiceItems = document.querySelectorAll('.invoice-item');
                allInvoiceItems.forEach((item, idx) => {
                    item.classList.toggle('selected', idx === index);
                });

            });

            invoiceItem.appendChild(invoiceName);
            invoiceItem.appendChild(deleteButton);
            invoiceContainer.appendChild(invoiceItem);

        });
    }



    function deleteInvoice(index) {
        // Lưu thông tin hóa đơn bị xóa
        const deletedOrder = invoices[index];

        // Xóa hóa đơn khỏi danh sách
        invoices.splice(index, 1);

        // Cập nhật danh sách trong sessionStorage
        sessionStorage.setItem("invoices", JSON.stringify(invoices));

        // Xóa giỏ hàng tương ứng trong sessionStorage
        let storedCart = JSON.parse(sessionStorage.getItem("cart")) || [];
        if (Array.isArray(storedCart) && storedCart.length > index) {
            storedCart.splice(index, 1);
        }
        sessionStorage.setItem("cart", JSON.stringify(storedCart));

        // Chỉ cập nhật selectedOrder nếu hóa đơn đang chọn bị xóa
        if (selectedOrder === deletedOrder) {
            selectedOrder = invoices[index] || invoices[invoices.length - 1] || null; // Chọn hóa đơn tiếp theo hoặc đặt null
            sessionStorage.setItem("selectedOrder", JSON.stringify(selectedOrder));

            const newIndex = invoices.indexOf(selectedOrder);
            renderCart(storedCart[newIndex] || []); // Hiển thị giỏ hàng của hóa đơn được chọn mới
        }

        renderInvoices(); // Cập nhật lại giao diện danh sách hóa đơn
        showToast('deleteInvoiceToast'); // Hiển thị thông báo xóa thành công
    }



    function selectOrder(invoice) {
        selectedOrder = invoice;  // Cập nhật thông tin đơn hàng đã chọn
        sessionStorage.setItem("selectedOrder", JSON.stringify(invoice)); // Lưu thông tin vào sessionStorage
        console.log('Đơn hàng đã chọn:', selectedOrder);
        renderCart(selectedOrder);
    }


    document.getElementById("addInvoiceButton").addEventListener("click", () => {
        // Kiểm tra giới hạn tối đa số hóa đơn
        if (invoices.length >= 10) {
            showToast('maxInvoiceToast');
            return;
        }
        let maxIndex = 0;
        invoices.forEach((invoice) => {
            const match = invoice.match(/Hóa đơn (\d+)/);
            if (match) {
                const currentIndex = parseInt(match[1]);
                if (currentIndex > maxIndex) {
                    maxIndex = currentIndex;
                }
            }
        });
        const newInvoiceName = `Hóa đơn ${maxIndex + 1}`;
        invoices.push(newInvoiceName);
        let storedCart = JSON.parse(sessionStorage.getItem("cart")) || [];
        if (!Array.isArray(storedCart)) {
            storedCart = [];
        }
        storedCart.push([]);
        sessionStorage.setItem("cart", JSON.stringify(storedCart));
        sessionStorage.setItem("invoices", JSON.stringify(invoices));
        selectedOrder = null;
        sessionStorage.setItem("selectedOrder", JSON.stringify(null));
        renderInvoices();
        renderCart(null);
        showToast('invoiceToast');
    });

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
    renderInvoices();

    searchInput.addEventListener("input", function () {
        const tenSP = searchInput.value.trim();
        const maSPCT = searchInput.value.trim();
        if (tenSP.length > 0 || maSPCT.length > 0) {
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
            showNotification("Vui lòng chọn đơn hàng trước!");
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

    function renderCart(selectedOrder) {
        const storedCart = JSON.parse(sessionStorage.getItem("cart")) || [];
        const orderIndex = invoices.indexOf(selectedOrder);
        let currentCart = storedCart[orderIndex] || []; // Giỏ hàng của đơn hàng đã chọn
        const cartTableBody = document.getElementById("cartTableBody");
        cartTableBody.innerHTML = ""; // Xóa các sản phẩm cũ
        const voucherField = document.getElementById("voucherSelect");


        const resetFields = () => {
            if (voucherField) voucherField.value = "";  // Reset voucher
            updateSummaryTable(0, 0);
        };

        resetFields();

        // Nếu giỏ hàng trống
        if (currentCart.length === 0) {
            resetFields();
            const row = document.createElement("tr");
            row.innerHTML = `<td colspan="5" class="text-center">Giỏ hàng trống</td>`;
            cartTableBody.appendChild(row);
            return;
        }

        // Hiển thị các sản phẩm trong giỏ hàng
        let totalAmount = 0;
        let totalQuantity = 0;

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
            <td class="productQuantity" style="position: relative;">
                <button style="position: absolute;top: 19px;left: 8px;" class="decrease-btn" data-index="${index}">-</button>
                <input style="position: absolute;width: 80px;right: 30px;top: 8px;text-align: center" type="number" value="${product.soLuong}" min="1" class="quantity-input" data-index="${index}">
                <button style="position:absolute;right: 13px;top: 18px;" class="increase-btn" data-index="${index}">+</button>
            </td>
            <td class="thanhTien">${product.thanhTien}</td>
        `;
            cartTableBody.appendChild(row);

            totalAmount += product.thanhTien;
            totalQuantity += product.soLuong;
        });

        // Cập nhật thông tin tổng cộng và tổng số lượng
        updateSummaryTable(totalAmount, totalQuantity);


        document.querySelectorAll('.increase-btn').forEach(button => {
            button.removeEventListener('click', handleIncrease); // Xóa sự kiện lắng nghe cũ
            button.addEventListener('click', handleIncrease); // Thêm sự kiện mới
        });

        function handleIncrease(event) {
            const index = event.target.getAttribute('data-index');
            const currentQuantity = currentCart[index].soLuong;
            const maSPCT = currentCart[index].maSPCT; // Mã sản phẩm chi tiết

            // Gửi yêu cầu kiểm tra số lượng tồn kho từ API
            fetch(`/admin/ban-hang/san-pham-chi-tiet/${maSPCT}/get-quantity`)
                .then(response => response.json())
                .then(data => {
                    // Kiểm tra xem API trả về thông tin số lượng tồn kho
                    const stockQuantity = data.soLuong;

                    // Nếu sản phẩm hết hàng
                    if (stockQuantity <= 0) {
                        alert("Sản phẩm đã hết hàng! Không thể thêm vào giỏ.");
                        return; // Dừng lại nếu sản phẩm hết hàng
                    }

                    // Nếu sản phẩm còn hàng, tiếp tục tăng số lượng
                    const newQuantity = currentQuantity + 1;
                    currentCart[index].soLuong = newQuantity;
                    currentCart[index].thanhTien = currentCart[index].donGia * newQuantity;

                    // Cập nhật giao diện
                    const row = event.target.closest("tr");
                    const thanhTienCell = row.querySelector(".thanhTien");
                    const input = row.querySelector(".quantity-input");

                    if (thanhTienCell) {
                        thanhTienCell.textContent = currentCart[index].thanhTien;
                    }

                    if (input) {
                        input.value = newQuantity; // Cập nhật giá trị trong ô input
                    }

                    // Cập nhật giỏ hàng trong sessionStorage
                    storedCart[orderIndex] = currentCart;
                    sessionStorage.setItem("cart", JSON.stringify(storedCart));

                    // Gửi yêu cầu backend để cập nhật số lượng
                    updateProductQuantity(currentCart[index].maSPCT, 1);

                    // Tính tổng số lượng và tổng tiền
                    const { totalQuantity, totalAmount } = calculateTotals();
                    updateSummaryTable(totalAmount, totalQuantity);
                })
                .catch(error => {
                    console.error("Có lỗi xảy ra khi kiểm tra số lượng:", error);
                    alert("Có lỗi khi kiểm tra số lượng sản phẩm.");
                });
        }

        document.querySelectorAll('.decrease-btn').forEach(button => {
            button.removeEventListener('click', handleDecrease); // Xóa sự kiện lắng nghe cũ
            button.addEventListener('click', handleDecrease); // Thêm sự kiện mới
        });

        function handleDecrease(event) {
            const index = event.target.getAttribute('data-index');
            const currentQuantity = currentCart[index].soLuong;

            // Kiểm tra nếu số lượng lớn hơn 1 thì mới giảm
            if (currentQuantity > 1) {
                const newQuantity = currentQuantity - 1;
                currentCart[index].soLuong = newQuantity;
                currentCart[index].thanhTien = currentCart[index].donGia * newQuantity;

                // Cập nhật giao diện
                const row = event.target.closest("tr");
                const thanhTienCell = row.querySelector(".thanhTien");
                const input = row.querySelector(".quantity-input");

                if (thanhTienCell) {
                    thanhTienCell.textContent = currentCart[index].thanhTien;
                }

                if (input) {
                    input.value = newQuantity; // Cập nhật giá trị trong ô input
                }

                // Cập nhật giỏ hàng trong sessionStorage
                storedCart[orderIndex] = currentCart;
                sessionStorage.setItem("cart", JSON.stringify(storedCart));

                // Gửi yêu cầu backend để cập nhật số lượng
                updateProductQuantity(currentCart[index].maSPCT, -1);

                // Tính tổng số lượng và tổng tiền
                const { totalQuantity, totalAmount } = calculateTotals();
                updateSummaryTable(totalAmount, totalQuantity);
            }
        }


        document.querySelectorAll('.quantity-input').forEach(input => {
            input.addEventListener('change', (event) => {
                const index = event.target.getAttribute('data-index');
                const newQuantity = parseInt(event.target.value);

                if (isNaN(newQuantity) || newQuantity < 1) {
                    event.target.value = currentCart[index].soLuong;
                    return;
                }

                const quantityChange = newQuantity - currentCart[index].soLuong;
                currentCart[index].soLuong = newQuantity;
                currentCart[index].thanhTien = currentCart[index].donGia * newQuantity;

                const row = event.target.closest("tr");
                const thanhTienCell = row.querySelector(".thanhTien");
                if (thanhTienCell) {
                    thanhTienCell.textContent = currentCart[index].thanhTien;
                }

                storedCart[orderIndex] = currentCart;
                sessionStorage.setItem("cart", JSON.stringify(storedCart));
                updateProductQuantity(currentCart[index].maSPCT, quantityChange);

                const { totalQuantity, totalAmount } = calculateTotals();
                updateSummaryTable(totalAmount, totalQuantity);
            });

        });

        function calculateTotals() {
            let totalQuantity = 0;
            let totalAmount = 0;

            currentCart.forEach(item => {
                totalQuantity += item.soLuong;
                totalAmount += item.soLuong * item.donGia;
            });

            return { totalQuantity, totalAmount };
        }

    }

    async function updateSummaryTable(totalAmount, totalQuantity) {
        const totalQuantityField = document.querySelector("table#summaryTable tbody tr:nth-child(1) td");
        const totalField = document.querySelector("table#summaryTable tbody tr:nth-child(2) td");
        const voucherField = document.getElementById("voucherSelect");
        const finalAmountField = document.querySelector("table#summaryTable tbody tr:nth-child(4) td");
        // Cập nhật tổng số lượng và tổng cộng
        if (totalQuantityField) totalQuantityField.textContent = totalQuantity;
        if (totalField) totalField.textContent = `${totalAmount.toLocaleString()} VNĐ`;

        // Nếu không có sản phẩm trong giỏ hàng, reset các trường
        if (totalQuantity === 0) {
            if (finalAmountField) finalAmountField.textContent = "0 VNĐ";
            return;
        }

        let finalAmount = totalAmount;

        // Xử lý voucher khi chọn
        if (voucherField) {
            const selectedVoucherId = voucherField.value;
            if (selectedVoucherId) {
                try {
                    const response = await fetch(`/admin/ban-hang/voucher/${selectedVoucherId}`);
                    if (response.ok) {
                        const voucher = await response.json();
                        // Kiểm tra điều kiện áp dụng voucher
                        if (totalAmount >= voucher.dieuKien) {
                            if (voucher.loaiVoucher === 1) {
                                // Giảm theo tỷ lệ phần trăm
                                const discount = totalAmount * (voucher.giaTri / 100);
                                finalAmount = totalAmount - discount;
                            } else if (voucher.loaiVoucher === 2) {
                                // Giảm theo giá trị cố định
                                finalAmount = totalAmount - voucher.giaTri;
                            }
                        } else {
                            showNotification(`Đơn hàng phải đạt tối thiểu ${voucher.dieuKien.toLocaleString()} VNĐ để áp dụng voucher này.`);
                            voucherField.value = "";
                        }
                    } else {
                        alert("Không thể áp dụng voucher. Vui lòng thử lại!");
                    }
                } catch (error) {
                    console.error("Lỗi khi lấy thông tin voucher:", error);
                    alert("Không thể áp dụng voucher. Vui lòng thử lại!");
                }
            }
        }

        // Đảm bảo giá trị cuối cùng không âm
        finalAmount = Math.max(finalAmount, 0);

        // Cập nhật lại thành tiền cuối cùng
        if (finalAmountField) finalAmountField.textContent = `${finalAmount.toLocaleString()} VNĐ`;

    }

// Sự kiện khi chọn voucher
    document.getElementById("voucherSelect").addEventListener("change", async function() {
        const totalAmount = parseInt(document.querySelector("table#summaryTable tbody tr:nth-child(2) td").textContent.replace(/\D/g, "")) || 0;
        const totalQuantity = parseInt(document.querySelector("table#summaryTable tbody tr:nth-child(1) td").textContent) || 0;
        await updateSummaryTable(totalAmount, totalQuantity);
    });



    function updateProductQuantity(maSPCT, quantityChange) {
        const request = {
            maSPCT: maSPCT,
            soLuong: quantityChange
        };
        fetch(`/admin/ban-hang/san-pham-chi-tiet/${maSPCT}/update-quantity`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(request)
        })
            .then(response => {
                if (!response.ok) {
                    console.error('Lỗi:', response.statusText);
                    return Promise.reject('Cập nhật không thành công');
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    console.log("Cập nhật số lượng thành công");
                } else {
                    console.log("Cập nhật số lượng thất bại");
                }
            })
            .catch(error => {
                console.error("Có lỗi xảy ra khi cập nhật số lượng:", error);
            });
    }

    let isUpdating = false;
    document.getElementById("addToCartButton").addEventListener("click", async function () {
        if (isUpdating) return;
        isUpdating = true;
        const quantity = parseInt(document.getElementById("productQuantity").value);
        const productId = selectedProduct.maSPCT;

        if (!selectedOrder) {
            showNotification("Vui lòng chọn đơn hàng trước!");
            isUpdating = false;
            return;
        }
        try {
            const response = await fetch(`/admin/ban-hang/san-pham-chi-tiet/${productId}/get-quantity`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });

            const result = await response.json();
            const availableQuantity = result.soLuong;

            if (quantity <= 0 || quantity > availableQuantity) {
                showNotification(`Số lượng sản phẩm không hợp lệ! Hiện tại chỉ còn ${availableQuantity} sản phẩm.`);
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
                    showNotification("Sản phẩm đã được cập nhật vào giỏ hàng!");
                    quantityModal.hide();
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

document.getElementById("paymentButton").addEventListener("click", function () {
    // Hiển thị modal xác nhận
    const confirmModal = new bootstrap.Modal(document.getElementById("confirmPaymentModal"));
    confirmModal.show();

    // Lắng nghe sự kiện "Xác nhận" từ modal
    document.getElementById("confirmPaymentButton").addEventListener("click", async function () {
        confirmModal.hide(); // Đóng modal

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
            const finalAmountField = document.querySelector("table#summaryTable tbody tr:nth-child(4) td");
            const discountPriceText = finalAmountField ? finalAmountField.textContent.trim() : '0';
            const discountPrice = discountPriceText ? parseFloat(discountPriceText.replace(/\D/g, "")) : 0;
            const totalPrice = productPrice * productQuantity;

            cartItems.push({
                productId,
                productName,
                productPrice,
                productQuantity,
                totalPrice,
                discountPrice
            });

            totalAmount += totalPrice;
            totalQuantity += productQuantity;
        });

        const customerData = JSON.parse(localStorage.getItem("customerData"));
        if (!customerData || !customerData.idKH) {
            showNotification("Thông tin khách hàng không hợp lệ hoặc không có trong hệ thống!");
            return;
        }
        const customerId = customerData.idKH;
        const paymentTypeSelect = document.getElementById("paymentTypeSelect");
        const paymentTypeId = paymentTypeSelect.value;
        if (!paymentTypeId) {
            showNotification("Vui lòng chọn loại thanh toán!");
            return;
        }
        const voucher = document.getElementById("voucherSelect").value;
        const finalAmountField = document.querySelector("table#summaryTable tbody tr:nth-child(4) td");
        const discountPriceText = finalAmountField ? finalAmountField.textContent.trim() : '0';
        const discountPrice = discountPriceText ? parseFloat(discountPriceText.replace(/\D/g, "")) : 0;
        const employeeData = JSON.parse(sessionStorage.getItem("infoNV"));
        if (!employeeData || !employeeData.id) {
            showNotification("Thông tin nhân viên không hợp lệ hoặc không có trong hệ thống!");
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
            discountPrice,
            paymentTypeId,
            voucher,
            cartItems
        };

        console.log(invoice)
        console.log(JSON.stringify(invoice));  // In dữ liệu trước khi gửi lên API
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
                    const toast = new bootstrap.Toast(document.getElementById("invoiceToast"));
                    toast.show();
                    resetCart();
                    updateSummary();
                    document.getElementById("voucherSelect").value = '';
                    document.getElementById("paymentTypeSelect").value = '';
                    generateInvoicePDF(invoice);
                } else {
                    showNotification("Đã có lỗi khi tạo hóa đơn: " + result.error);
                }
            } else {
                const errorResponse = await response.json();  // In ra lỗi từ server
                console.error("Lỗi khi gửi yêu cầu: ", errorResponse); // Kiểm tra lỗi chi tiết
                showNotification("Đã có lỗi khi gửi yêu cầu! Mã lỗi: " + response.status);
            }
        } catch (error) {
            showNotification("Đã có lỗi khi tạo hóa đơn! Vui lòng thử lại.");
        }
    }, { once: true }); // Đảm bảo sự kiện chỉ được lắng nghe một lần
});

// Hàm hiển thị modal thông báo
function showNotification(message) {
    const modalMessage = document.getElementById("modalMessage");
    modalMessage.textContent = message; // Cập nhật thông điệp trong modal
    const notificationModal = new bootstrap.Modal(document.getElementById("notificationModal"));
    notificationModal.show(); // Hiển thị modal
}

function generateInvoicePDF(invoice) {
    const { jsPDF } = window.jspdf;  // Lấy jsPDF từ window.jspdf khi dùng CDN

    const doc = new jsPDF();
    // Kiểm tra nếu font Arial không tồn tại
    try {
        doc.setFont('Arial', 'normal');
    } catch (error) {
        console.error("Lỗi font Arial:", error);
        doc.setFont('Helvetica', 'normal');  // Sử dụng font Helvetica thay thế nếu Arial không có
    }

    // Tiêu đề hóa đơn
    doc.setFontSize(16);
    doc.text("Hóa Đơn Mua Hàng", 105, 20, null, null, 'center');
    doc.setFontSize(12);

    // Thêm ngày tạo hóa đơn
    const currentDate = new Date();
    const formattedDate = `${currentDate.getDate()}/${currentDate.getMonth() + 1}/${currentDate.getFullYear()} ${currentDate.getHours()}:${currentDate.getMinutes()}:${currentDate.getSeconds()}`;
    doc.text(`Ngày tạo: ${formattedDate}`, 10, 40);  // In ngày tạo hóa đơn
  
    const customer = invoice.khachHang || {};  // Lấy thông tin khách hàng từ đối tượng invoice
    const employee = invoice.nhanVien || {};  // Lấy thông tin nhân viên từ đối tượng invoice
    doc.text(`Khách hàng: ${customer.tenKH || 'Chưa có thông tin'}`, 10, 50);  // Thông tin khách hàng
    doc.text(`Nhân viên: ${employee.tenNV || 'Chưa có thông tin'}`, 10, 60);  // Thông tin nhân viên
  
    // Bắt đầu vẽ bảng
    const startY = 70;  // Vị trí bắt đầu vẽ bảng
    let yPosition = startY;

    // Cài đặt tiêu đề cho bảng
    doc.setFontSize(10);
    doc.text("STT", 10, yPosition);
    doc.text("Tên Sản Phẩm", 30, yPosition);
    doc.text("Đơn Giá", 100, yPosition);
    doc.text("Số Lượng", 130, yPosition);
    doc.text("Thành Tiền", 160, yPosition);

    yPosition += 10; // Dịch xuống một chút để vẽ các sản phẩm

    // Vẽ các sản phẩm trong giỏ hàng

    if (invoice.cartItems && invoice.cartItems.length > 0) {
        invoice.cartItems.forEach((item, index) => {
            doc.text((index + 1).toString(), 10, yPosition);  // Số thứ tự
            doc.text(item.productName, 30, yPosition);  // Tên sản phẩm
            doc.text(item.productPrice.toString(), 100, yPosition);  // Đơn giá
            doc.text(item.productQuantity.toString(), 130, yPosition);  // Số lượng
            doc.text(item.totalPrice.toString(), 160, yPosition);  // Thành tiền

            yPosition += 10; // Dịch xuống một chút cho dòng tiếp theo
        });
    } else {
        doc.text("Không có sản phẩm trong giỏ hàng", 10, yPosition);
        yPosition += 10;
    }

    // Tổng tiền
    yPosition += 10; // Dịch xuống sau bảng
    doc.text(`Tổng Tiền: ${invoice.totalAmount} VND`, 10, yPosition);

    // Phương thức thanh toán
    yPosition += 10;

    doc.text(`Phương thức thanh toán: ${invoice.paymentType || 'Chưa chọn phương thức thanh toán'}`, 10, yPosition);

    // Voucher (nếu có)
    if (invoice.voucher) {
        yPosition += 10;
        doc.text(`Voucher: ${invoice.voucher}`, 10, yPosition);

    } else {
        yPosition += 10;
        doc.text("Không có voucher", 10, yPosition);
    }

    // Lưu và in hóa đơn PDF
    doc.save('invoice.pdf');
    window.open(doc.output('bloburl'));  // Mở PDF để in
}

function resetCart() {
    document.getElementById("cartTableBody").innerHTML = "";
}

const customerName = document.getElementById("customerName");
const customerPhone = document.getElementById("customerPhone");

function updateSummary() {
    let totalAmount = 0;
    let totalQuantity = 0;

    // Cập nhật thông tin vào bảng tổng kết
    document.querySelector("#summaryTable tbody tr:nth-child(1) td").textContent = totalQuantity;  // Số lượng = 0
    document.querySelector("#summaryTable tbody tr:nth-child(2) td").textContent = totalAmount.toFixed(2);  // Tổng tiền = 0
    document.querySelector("#summaryTable tbody tr:nth-child(4) td").textContent = totalAmount.toFixed(2);  // Cập nhật vào dòng 4
    customerName.textContent = '';
    customerPhone.textContent = '';
}
