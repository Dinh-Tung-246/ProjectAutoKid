// ket noi den websocket
const socket = new WebSocket('ws://localhost:8080/ws');

// khi ket noi thanh cong
socket.onopen = function () {
    console.log("Da ket noi voi WebSocket");
}

// Khi có lỗi
socket.onerror = function(error) {
    console.error("Lỗi WebSocket:", error);
};

// Khi kết nối bị đóng
socket.onclose = function() {
    console.log("Kết nối WebSocket đã đóng.");
};


function handleAddTH(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/add/thuong-hieu', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            // Thêm thương hiệu thành công, đóng modal và cập nhật danh sách thương hiệu
            Swal.fire({
                title: "Thêm thương hiệu thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                // Đóng modal
                const modalElement = document.querySelector('#addThuongHieuModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
                form.reset();
                // Cập nhật lại danh sách thương hiệu
                updateThuongHieuList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}
// Hàm cập nhật danh sách thương hiệu mà không cần tải lại trang
function handleUpdateTH(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/update/thuong-hieu', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            // Cập nhật thương hiệu thành công
            Swal.fire({
                title: "Cập nhật thương hiệu thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                form.reset();
                // Cập nhật danh sách thương hiệu (bạn cần định nghĩa hàm này)
                updateThuongHieuList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}

function updateThuongHieuList() {
    fetch('/admin/thuong-hieu/list')
        .then(response => response.json())
        .then(data => {
            const thuongHieuSelect = document.querySelector('#thuongHieu');
            const thuongHieuTableBody = document.querySelector('#thuongHieuTable tbody');
            const sanPhamTableBody = document.querySelector('#sanPhamTable tbody');



            // Cập nhật danh sách thương hiệu
            if (thuongHieuTableBody) {
                thuongHieuTableBody.innerHTML = '';
                data.forEach(thuongHieu => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${thuongHieu.maTH}</td>
                        <td>${thuongHieu.tenTH}</td>
                        <td>${thuongHieu.trangThaiTH}</td>
                        <td>${thuongHieu.diaChi}</td>
                        <td>
                            <button class="view-detail btn btn-info"
                                    data-bs-toggle="modal"
                                    data-bs-target="#productDetailModal"
                                    data-id="${thuongHieu.id}"
                                    data-math="${thuongHieu.maTH}"
                                    data-tenth="${thuongHieu.tenTH}"
                                    data-trangthai="${thuongHieu.trangThaiTH}"
                                    data-diachi="${thuongHieu.diaChi}">
                                Cập nhật
                            </button>
                        </td>
                    `;
                    thuongHieuTableBody.appendChild(row);
                });
                attachUpdateButtonEvents();
            }
            if (sanPhamTableBody) {
                thuongHieuSelect.innerHTML = '<option value="" disabled selected>Chọn thương hiệu</option>';
                data.forEach(thuongHieu => {
                    const option = document.createElement('option');
                    option.value = thuongHieu.id;
                    option.textContent = thuongHieu.tenTH;
                    thuongHieuSelect.appendChild(option);
                });
            }
        })
        .catch(error => {
            console.error('Lỗi khi cập nhật danh sách thương hiệu:', error);
        });
}

function handleAddSP(event, form) {
    event.preventDefault();

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/add/san-pham', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                // Thêm sản phẩm thành công, đóng modal và cập nhật danh sách sản phẩm
                Swal.fire({
                    title: "Thêm sản phẩm thành công!",
                    icon: "success",
                    confirmButtonText: "OK"
                }).then(() => {
                    // Đóng modal
                    const modalElement = document.querySelector('#exampleModal');
                    const modalInstance = bootstrap.Modal.getInstance(modalElement);
                    modalInstance.hide();
                    form.reset();
                    // Cập nhật danh sách sản phẩm
                    updateSPList();
                });
            } else {
                return response.text().then(text => {
                    throw new Error(text || 'Có lỗi xảy ra');
                });
            }
        })
        .catch(error => {
            Swal.fire({
                title: "Lỗi!",
                text: error.message,
                icon: "error",
                confirmButtonText: "OK"
            });
        });

    return false;
}


function handleUpdateSP(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/update/san-pham', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            // Cập nhật sản phẩm thành công
            Swal.fire({
                title: "Cập nhật sản phẩm thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                // Cập nhật danh sách sản phẩm
                updateSPList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}

function updateSPList() {
    fetch('/admin/san-pham/list') // URL để lấy danh sách sản phẩm mới
        .then(response => {
            if (!response.ok) {
                throw new Error('Không thể lấy danh sách sản phẩm');
            }
            return response.json();
        })
        .then(data => {
            // Cập nhật bảng sản phẩm
            const sanPhamTableBody = document.querySelector('#sanPhamTable tbody');
            const sanPhamSelect = document.querySelector('#sanPhamList');
            if (sanPhamTableBody) {
                sanPhamTableBody.innerHTML = ''; // Xóa dữ liệu cũ

                // Duyệt qua danh sách sản phẩm và tạo các hàng trong bảng
                data.forEach(sanPham => {
                    if (sanPham.trangThaiSP === 'Đang bán') { // Kiểm tra trạng thái sản phẩm
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${sanPham.maSP}</td>
                            <td>${sanPham.tenSP}</td>
                            <td><img src="/img/categories/${sanPham.anhSPMau}" alt="Ảnh sản phẩm"></td>
                            <td>${sanPham.donGia}</td>
                            <td>${sanPham.moTa}</td>
                            <td>${sanPham.trangThaiSP}</td>
                            <td>
                                <button
                                    class="view-detail btn btn-info"
                                    data-bs-toggle="modal"
                                    data-bs-target="#productDetailModal"
                                    data-id="${sanPham.id}"
                                    data-masp="${sanPham.maSP}"
                                    data-ten="${sanPham.tenSP}"
                                    data-anh="${sanPham.anhSPMau}"
                                    data-gianhap="${sanPham.giaNhap}"
                                    data-dongia="${sanPham.donGia}"
                                    data-ngaytao="${sanPham.ngayTao}"
                                    data-mota="${sanPham.moTa}"
                                    data-loai="${sanPham.loaiSanPham.idLoaiSP}"
                                    data-thuonghieu="${sanPham.thuongHieu.id}"
                                    data-kichco="${sanPham.kichCo.id}"
                                    data-chatlieu="${sanPham.chatLieu.id}"
                                    data-trangthai="${sanPham.trangThaiSP}"
                                >
                                    Chi tiết
                                </button>
                            </td>
                        `;
                        sanPhamTableBody.appendChild(row);
                    }
                });

                // Đính kèm sự kiện vào các nút chi tiết sản phẩm
                attachUpdateButtonEvents();
            }

            // Cập nhật danh sách chọn sản phẩm (nếu có)

            if (sanPhamSelect) {
                sanPhamSelect.innerHTML = '<option value="" disabled selected>Chọn sản phẩm</option>';
                data.forEach(sanPham => {
                    const option = document.createElement('option');
                    option.value = sanPham.id;
                    option.textContent = sanPham.tenSP;
                    sanPhamSelect.appendChild(option);
                });
            }
        })
        .catch(error => {
            console.error('Lỗi khi cập nhật danh sách sản phẩm:', error);
        });
}



function handleAddCL(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/add/chat-lieu', {  // URL thêm chất liệu mới
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            // Thêm chất liệu thành công, đóng modal và cập nhật danh sách chất liệu
            Swal.fire({
                title: "Thêm chất liệu thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                // Đóng modal
                const modalElement = document.querySelector('#addChatLieuModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
                form.reset();
                // Cập nhật lại danh sách chất liệu
                updateChatLieuList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}

function handleUpdateCL(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/update/chat-lieu', {  // URL cập nhật chất liệu
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            // Cập nhật chất liệu thành công
            Swal.fire({
                title: "Cập nhật chất liệu thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                form.reset();
                // Cập nhật lại danh sách chất liệu
                updateChatLieuList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}



function updateChatLieuList() {
    fetch('/admin/chat-lieu/list')  // URL lấy danh sách chất liệu mới
        .then(response => response.json())
        .then(data => {
            const chatLieuSelect = document.querySelector('#chatLieu');
            const chatLieuTableBody = document.querySelector('#chatLieuTable tbody');

            // Kiểm tra nếu đang ở trang quản lý chất liệu
            if (chatLieuTableBody) {
                chatLieuTableBody.innerHTML = '';
                data.forEach(chatLieu => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${chatLieu.maCl}</td>
                        <td>${chatLieu.tenCl}</td>
                        <td>${chatLieu.trangThaiCl}</td>
                        <td>
                            <button class="view-detail btn btn-info"
                                    data-bs-toggle="modal"
                                    data-bs-target="#productDetailModal"
                                    data-id="${chatLieu.id}"
                                    data-macl="${chatLieu.maCl}"
                                    data-tencl="${chatLieu.tenCl}"
                                    data-trangthai="${chatLieu.trangThaiCl}">
                                Cập nhật
                            </button>
                        </td>
                    `;
                    chatLieuTableBody.appendChild(row);
                });
                // Đính kèm sự kiện vào các nút cập nhật
                attachUpdateButtonEvents();
            }

            // Cập nhật danh sách chọn chất liệu (nếu cần thiết)
            if (chatLieuSelect) {
                chatLieuSelect.innerHTML = '<option value="" disabled selected>Chọn chất liệu</option>';
                data.forEach(chatLieu => {
                    const option = document.createElement('option');
                    option.value = chatLieu.id;
                    option.textContent = chatLieu.tenCl;
                    chatLieuSelect.appendChild(option);
                });
            }
        })
        .catch(error => {
            console.error('Lỗi khi cập nhật danh sách chất liệu:', error);
        });
}
function handleAddSPCT(event, form) {
    return handleFormSubmit(event, form, "Thêm chi tiết sản phẩm thành công!", "/admin/products");
}

function handleUpdateSPCT(event, form) {
    return handleFormSubmit(event, form, "Cập nhật sản phẩm chi tiết thành công!", "/admin/products");
}

function handleAddMS(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/add/mau-sac', {  // URL thêm chất liệu mới
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                title: "Thêm màu sắc thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                // Đóng modal
                const modalElement = document.querySelector('#addMauSacModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
                form.reset();
                // Cập nhật lại danh sách chất liệu
                updateMauSacList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}

function handleUpdateMS(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/update/mau-sac', {  // URL cập nhật chất liệu
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            // Cập nhật chất liệu thành công
            Swal.fire({
                title: "Cập nhật màu sắc thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                form.reset();
                // Cập nhật lại danh sách chất liệu
                updateMauSacList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}

function updateMauSacList() {
    fetch('/admin/mau-sac/list')  // URL để lấy danh sách màu sắc mới
        .then(response => response.json())
        .then(data => {
            const mauSacTableBody = document.querySelector('.posts-table tbody');
            const mauSacSelect = document.querySelector('#mauSac');

            // Kiểm tra nếu đang ở trang quản lý màu sắc
            if (mauSacTableBody) {
                mauSacTableBody.innerHTML = '';
                data.forEach(mauSac => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${mauSac.maMS}</td>
                        <td>${mauSac.tenMS}</td>
                        <td>${mauSac.trangThaiMS}</td>
                        <td>
                            <button class="view-detail btn btn-info"
                                    data-bs-toggle="modal"
                                    data-bs-target="#productDetailModal"
                                    data-id="${mauSac.id}"
                                    data-mams="${mauSac.maMS}"
                                    data-tenms="${mauSac.tenMS}"
                                    data-trangthai="${mauSac.trangThaiMS}">
                                Cập nhật
                            </button>
                        </td>
                    `;
                    mauSacTableBody.appendChild(row);
                });
                // Đính kèm sự kiện vào các nút cập nhật
                attachUpdateButtonEvents();
            }

            // Cập nhật danh sách chọn màu sắc (nếu cần thiết)
            if (mauSacSelect) {
                mauSacSelect.innerHTML = '<option value="" disabled selected>Chọn màu sắc</option>';
                data.forEach(mauSac => {
                    const option = document.createElement('option');
                    option.value = mauSac.id;
                    option.textContent = mauSac.tenMS;
                    mauSacSelect.appendChild(option);
                });
            }
        })
        .catch(error => {
            console.error('Lỗi khi cập nhật danh sách màu sắc:', error);
        });
}



function handleAddKC(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/add/kich-co', {  // URL thêm chất liệu mới
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                title: "Thêm kích cỡ thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                // Đóng modal
                const modalElement = document.querySelector('#addKichCoModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
                form.reset();
                // Cập nhật lại danh sách chất liệu
                updateKichCoList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}

function handleUpdateKC(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/update/kich-co', {  // URL cập nhật chất liệu
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            // Cập nhật chất liệu thành công
            Swal.fire({
                title: "Cập nhật kích cỡ thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                form.reset();
                // Cập nhật lại danh sách chất liệu
                updateKichCoList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}



function updateKichCoList() {
    fetch('/admin/kich-co/list')  // URL lấy danh sách chất liệu mới
        .then(response => response.json())
        .then(data => {
            const kichCoSelect = document.querySelector('#kichCo');
            const kichCoTableBody = document.querySelector('#kichCoTable tbody');
            if (kichCoTableBody) {
                kichCoTableBody.innerHTML = '';
                data.forEach(kichCo => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${kichCo.maKC}</td>
                        <td>${kichCo.tenKC}</td>
                        <td>${kichCo.trangThaiKC}</td>
                        <td>${kichCo.moTa}</td>
                        <td>
                            <button class="view-detail btn btn-info"
                                    data-bs-toggle="modal"
                                    data-bs-target="#productDetailModal"
                                    data-id="${kichCo.id}"
                                    data-makc="${kichCo.maKC}"
                                    data-tenkc="${kichCo.tenKC}"
                                    data-mota="${kichCo.moTa}"
                                    data-trangthai="${kichCo.trangThaiKC}">
                                Cập nhật
                            </button>
                        </td>
                    `;
                    kichCoTableBody.appendChild(row);
                });
                attachUpdateButtonEvents();
            }
            if (kichCoSelect) {
                kichCoSelect.innerHTML = '<option value="" disabled selected>Chọn kích cỡ</option>';
                data.forEach(kichCo => {
                    const option = document.createElement('option');
                    option.value = kichCo.id;
                    option.textContent = kichCo.tenKC;
                    kichCoSelect.appendChild(option);
                });
            }
        })
        .catch(error => {
            console.error('Lỗi khi cập nhật danh sách kích cỡ:', error);
        });
}
function handleAddLSP(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/add/loai-san-pham', {  // URL thêm chất liệu mới
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                title: "Thêm loại sản phẩm thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                // Đóng modal
                const modalElement = document.querySelector('#addLoaiSanPhamModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
                form.reset();
                // Cập nhật lại danh sách chất liệu
                updateLoaiSanPhamList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}

function handleUpdateLSP(event, form) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/update/loai-san-pham', {  // URL cập nhật chất liệu
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                title: "Cập nhật loại sản phẩm thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                form.reset();
                updateLoaiSanPhamList();
            });
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra');
            });
        }
    }).catch(error => {
        Swal.fire({
            title: "Lỗi!",
            text: error.message,
            icon: "error",
            confirmButtonText: "OK"
        });
    });

    return false;
}



function updateLoaiSanPhamList() {
    fetch('/admin/loai-san-pham/list')  // Lấy danh sách loại sản phẩm từ server
        .then(response => response.json())
        .then(data => {
            const loaiSanPhamSelect = document.querySelector('#loaiSanPham');  // Chọn phần tử <select> cho loại sản phẩm
            const loaiSanPhamTableBody = document.querySelector('#loaiSanPhamTable tbody');  // Chọn <tbody> của bảng hiển thị loại sản phẩm

            // Cập nhật danh sách loại sản phẩm cho phần tử <select>
            if (loaiSanPhamSelect) {
                loaiSanPhamSelect.innerHTML = '<option value="" disabled selected>Chọn loại sản phẩm</option>';
                data.forEach(loaiSanPham => {
                    const option = document.createElement('option');
                    option.value = loaiSanPham.id;
                    option.textContent = loaiSanPham.tenLoai;
                    loaiSanPhamSelect.appendChild(option);
                });
            }

            // Cập nhật bảng loại sản phẩm
            if (loaiSanPhamTableBody) {
                loaiSanPhamTableBody.innerHTML = '';  // Xóa các dòng cũ trong bảng
                data.forEach(loaiSanPham => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${loaiSanPham.maLSP}</td>
                        <td>${loaiSanPham.tenLoai}</td>
                        <td>${loaiSanPham.moTa}</td>
                        <td>${loaiSanPham.trangThai}</td>
                        <td>
                            <button class="view-detail btn btn-info"
                                    data-bs-toggle="modal"
                                    data-bs-target="#productDetailModal"
                                    data-id="${loaiSanPham.id}"
                                    data-ma-lsp="${loaiSanPham.maLSP}"
                                    data-ten-loai="${loaiSanPham.tenLoai}"
                                    data-trang-thai="${loaiSanPham.trangThai}"
                                    data-mo-ta="${loaiSanPham.moTa}">
                                Cập nhật
                            </button>
                        </td>
                    `;
                    loaiSanPhamTableBody.appendChild(row);
                });
                attachUpdateButtonEvents();  // Gọi hàm để đính kèm sự kiện cho các nút Cập nhật
            }
        })
        .catch(error => {
            console.error('Lỗi khi cập nhật danh sách loại sản phẩm:', error);
        });
}


