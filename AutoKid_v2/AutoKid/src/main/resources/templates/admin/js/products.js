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
    event.preventDefault();
    const maTH = form.querySelector('#maTH').value.trim();
    const tenTH = form.querySelector('#tenTH').value.trim();
    const diaChi = form.querySelector('#diaChi').value.trim();
    if (!maTH) {
        Swal.fire({
            title: "Lỗi!",
            text: "Mã thương hiệu không được để trống.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!tenTH) {
        Swal.fire({
            title: "Lỗi!",
            text: "Tên thương hiệu không được để trống.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!diaChi) {
        Swal.fire({
            title: "Lỗi!",
            text: "Địa chỉ không được để trống.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    const formData = new FormData(form);
    fetch('/admin/add/thuong-hieu', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                title: "Thêm thương hiệu thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                const modalElement = document.querySelector('#addThuongHieuModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
                form.reset();
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
                const modalElement = document.querySelector('#productDetailModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
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
            const thuongHieuTableAnBody = document.querySelector('#thuongHieuTableAn tbody');
            const sanPhamTableBody = document.querySelector('#sanPhamTable tbody');

            // Cập nhật danh sách thương hiệu "Hoạt động"
            if (thuongHieuTableBody) {
                thuongHieuTableBody.innerHTML = '';
                data.forEach(thuongHieu => {
                    if (thuongHieu.trangThaiTH === 'Hoạt động') {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                        <td>${thuongHieu.maTH}</td>
                        <td>${thuongHieu.tenTH}</td>                   
                        <td>${thuongHieu.diaChi}</td>
                        <td>${thuongHieu.trangThaiTH}</td>
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
                    }
                });
                attachUpdateButtonEvents();
            }

            // Cập nhật danh sách thương hiệu "Không hoạt động"
            if (thuongHieuTableAnBody) {
                thuongHieuTableAnBody.innerHTML = ''; // Xóa dữ liệu cũ
                data.forEach(thuongHieu => {
                    if (thuongHieu.trangThaiTH === 'Không hoạt động') {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                        <td>${thuongHieu.maTH}</td>
                        <td>${thuongHieu.tenTH}</td>                      
                        <td>${thuongHieu.diaChi}</td>
                        <td>${thuongHieu.trangThaiTH}</td>
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
                        thuongHieuTableAnBody.appendChild(row);
                    }
                });
                attachUpdateButtonEvents();
            }

            // Cập nhật danh sách thương hiệu trong dropdown
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
    const maSP = form.querySelector('#maSP').value.trim();
    const tenSP = form.querySelector('#tenSP').value.trim();
    const giaNhap = form.querySelector('#giaNhap').value.trim();
    const donGia = form.querySelector('#donGia').value.trim();
    const ngayTao = form.querySelector('#ngayTao').value.trim();
    const loaiSanPham = form.querySelector('#loaiSanPham').value;
    const thuongHieu = form.querySelector('#thuongHieu').value;
    const kichCo = form.querySelector('#kichCo').value;
    const chatLieu = form.querySelector('#chatLieu').value;
    const anhSPMau = form.querySelector('#anhSPMau').value;

    if (!maSP) {
        Swal.fire({
            title: "Lỗi!",
            text: "Mã sản phẩm không được để trống.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!tenSP) {
        Swal.fire({
            title: "Lỗi!",
            text: "Tên sản phẩm không được để trống.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!giaNhap || giaNhap <= 0) {
        Swal.fire({
            title: "Lỗi!",
            text: "Giá nhập không được để trống và phải lớn hơn 0.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!donGia || donGia <= 0) {
        Swal.fire({
            title: "Lỗi!",
            text: "Giá bán  không được để trống và phải lớn hơn 0.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!ngayTao) {
        Swal.fire({
            title: "Lỗi!",
            text: "Ngày tạo không được để trống.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!loaiSanPham) {
        Swal.fire({
            title: "Lỗi!",
            text: "Vui lòng chọn loại sản phẩm.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!thuongHieu) {
        Swal.fire({
            title: "Lỗi!",
            text: "Vui lòng chọn thương hiệu.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!kichCo) {
        Swal.fire({
            title: "Lỗi!",
            text: "Vui lòng chọn kích cỡ.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!chatLieu) {
        Swal.fire({
            title: "Lỗi!",
            text: "Vui lòng chọn chất liệu.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!anhSPMau) {
        Swal.fire({
            title: "Lỗi!",
            text: "Vui lòng chọn ảnh sản phẩm.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }

    const formData = new FormData(form);
    fetch('/admin/add/san-pham', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                // Thêm sản phẩm thành công
                Swal.fire({
                    title: "Thêm sản phẩm thành công!",
                    icon: "success",
                    confirmButtonText: "OK"
                }).then(() => {
                    location.reload();
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
    event.preventDefault();
    const changeImageCheckbox = document.getElementById("changeImageCheckbox");
    const formData = new FormData(form);
    if (!changeImageCheckbox.checked) {
        const currentImageName = document.getElementById("currentImageName").value;
        formData.append("anh", currentImageName);
    }
    fetch('/admin/update/san-pham', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                title: "Cập nhật sản phẩm thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                location.reload();
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


function handleAddCL(event, form) {
    event.preventDefault();
    const maCl = form.querySelector('#maCl').value.trim();
    const tenCl = form.querySelector('#tenCl').value.trim();
    const trangThaiCl = form.querySelector('#trangThaiCl').value.trim();
    if (!maCl) {
        Swal.fire({
            title: "Lỗi!",
            text: "Mã chất liệu không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!tenCl) {
        Swal.fire({
            title: "Lỗi!",
            text: "Tên chất liệu không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!trangThaiCl) {
        Swal.fire({
            title: "Lỗi!",
            text: "Trạng thái chất liệu không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    const formData = new FormData(form);
    fetch('/admin/add/chat-lieu', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
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
    event.preventDefault();
    const tenCL = form.querySelector('#tenCl').value.trim();
    const trangThaiCL = form.querySelector('#trangThaiCl').value.trim();
    if (!tenCL) {
        Swal.fire({
            title: "Lỗi!",
            text: "Tên chất liệu không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!trangThaiCL) {
        Swal.fire({
            title: "Lỗi!",
            text: "Trạng thái chất liệu không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    const formData = new FormData(form);
    fetch('/admin/update/chat-lieu', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                title: "Cập nhật chất liệu thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                const modalElement = document.querySelector('#productDetailModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
                form.reset();
                form.reset();
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
    fetch('/admin/chat-lieu/list')
        .then(response => response.json())
        .then(data => {
            const chatLieuTableBody = document.querySelector('#chatLieuTable tbody');
            const chatLieuTableAnBody = document.querySelector('#chatLieuTableAn tbody');
            const chatLieuSelect = document.querySelector('#chatLieu');
            if (chatLieuTableBody) {
                chatLieuTableBody.innerHTML = '';
                data.forEach(chatLieu => {
                    if (chatLieu.trangThaiCl === 'Hoạt động') {
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
                    }
                });
                attachUpdateButtonEvents();
            }
            if (chatLieuTableAnBody) {
                chatLieuTableAnBody.innerHTML = '';
                data.forEach(chatLieu => {
                    if (chatLieu.trangThaiCl === 'Không hoạt động') {
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
                        chatLieuTableAnBody.appendChild(row);
                    }
                });
                attachUpdateButtonEvents();
            }
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
    event.preventDefault();
    const maSPCT = form.querySelector("#maSPCT").value;
    if (!maSPCT) {
        Swal.fire({
            title: "Lỗi!",
            text: "Mã sản phẩm chi tiết không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    const sanPham = form.querySelector("#sanPham").value;
    if (!sanPham) {
        Swal.fire({
            title: "Lỗi!",
            text: "Bạn cần chọn sản phẩm!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    const mauSac = form.querySelector("#mauSac").value;
    if (!mauSac) {
        Swal.fire({
            title: "Lỗi!",
            text: "Bạn cần chọn màu sắc!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    const soLuong = form.querySelector("#soLuong").value;
    if (isNaN(soLuong) || soLuong <= 0) {
        Swal.fire({
            title: "Lỗi!",
            text: "Số lượng phải là một số dương!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    const anh = form.querySelector("#anh").files[0];
    if (anh) {
        const validImageTypes = ["image/jpeg", "image/png", "image/gif"];
        if (!validImageTypes.includes(anh.type)) {
            Swal.fire({
                title: "Lỗi!",
                text: "Ảnh phải có định dạng .jpg, .png, hoặc .gif!",
                icon: "error",
                confirmButtonText: "OK"
            });
            return ;
        }
    }
    const formData = new FormData(form);
    fetch('/admin/add/products', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    title: "Thêm sản phẩm chi tiết thành công!",
                    icon: "success",
                    confirmButtonText: "OK"
                }).then(() => {
                    location.reload();
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
}

function handleUpdateSPCT(event, form) {
    event.preventDefault();
    const changeImageCheckbox = document.getElementById("changeImageCheckbox");
    const formData = new FormData(form);

    // Nếu không chọn ảnh mới, giữ lại ảnh cũ
    if (!changeImageCheckbox.checked) {
        const currentImageName = document.getElementById("anh").value;
        if (currentImageName) {
            formData.append("anh", currentImageName);
        } else {
            console.warn("Current image name is empty, please check.");
        }
    }

    fetch('/admin/update/products', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                title: "Cập nhật sản phẩm chi tiết thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                location.reload();
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
}




function handleAddMS(event, form) {
    event.preventDefault();
    const maMS = form.querySelector('#maMS').value.trim();
    const tenMS = form.querySelector('#tenMS').value.trim();
    const trangThaiMS = form.querySelector('#trangThaiMS').value.trim();
    if (!maMS) {
        Swal.fire({
            title: "Lỗi!",
            text: "Mã màu sắc không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!tenMS) {
        Swal.fire({
            title: "Lỗi!",
            text: "Tên màu sắc không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!trangThaiMS) {
        Swal.fire({
            title: "Lỗi!",
            text: "Trạng thái màu sắc không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/add/mau-sac', {  // URL thêm màu sắc mới
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
                // Cập nhật lại danh sách màu sắc
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

    const tenMS = form.querySelector('#tenMS').value.trim(); // Lấy giá trị tên màu sắc
    const trangThaiMS = form.querySelector('#trangThaiMS').value.trim(); // Lấy giá trị trạng thái

    // Validate: Kiểm tra tên màu sắc và trạng thái
    if (!tenMS) {
        Swal.fire({
            title: "Lỗi!",
            text: "Tên màu sắc không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }

    if (!trangThaiMS) {
        Swal.fire({
            title: "Lỗi!",
            text: "Trạng thái màu sắc không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/update/mau-sac', {  // URL cập nhật màu sắc
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            // Cập nhật màu sắc thành công
            Swal.fire({
                title: "Cập nhật màu sắc thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                const modalElement = document.querySelector('#productDetailModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
                form.reset();
                // Cập nhật lại danh sách màu sắc
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
    fetch('/admin/mau-sac/list')  // URL để lấy danh sách màu sắc
        .then(response => response.json())
        .then(data => {
            const mauSacTableBody = document.querySelector('#mauSacTable tbody');
            const mauSacTableAnBody = document.querySelector('#mauSacTableAn tbody');
            const mauSacSelect = document.querySelector('#mauSac');

            // Cập nhật danh sách màu sắc "Hoạt động"
            if (mauSacTableBody) {
                mauSacTableBody.innerHTML = '';
                data.forEach(mauSac => {
                    if (mauSac.trangThaiMS === 'Hoạt động') {
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
                    }
                });
                attachUpdateButtonEvents();
            }

            // Cập nhật danh sách màu sắc "Không hoạt động"
            if (mauSacTableAnBody) {
                mauSacTableAnBody.innerHTML = ''; // Xóa dữ liệu cũ
                data.forEach(mauSac => {
                    if (mauSac.trangThaiMS === 'Không hoạt động') {
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
                        mauSacTableAnBody.appendChild(row);
                    }
                });
                attachUpdateButtonEvents();
            }

            // Cập nhật danh sách màu sắc trong dropdown
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
    event.preventDefault();
    const maKC = form.querySelector('#maKC').value.trim();
    const tenKC = form.querySelector('#tenKC').value.trim();
    const trangThaiKC = form.querySelector('#trangThaiKC').value.trim();
    if (!maKC) {
        Swal.fire({
            title: "Lỗi!",
            text: "Mã kích cỡ không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!tenKC) {
        Swal.fire({
            title: "Lỗi!",
            text: "Tên kích cỡ không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!trangThaiKC) {
        Swal.fire({
            title: "Lỗi!",
            text: "Trạng thái kích cỡ không được chọn!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/add/kich-co', {  // URL thêm kích cỡ mới
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
                // Cập nhật lại danh sách kích cỡ
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
    event.preventDefault();
    const tenKC = form.querySelector('#tenKC').value.trim();
    const trangThaiKC = form.querySelector('#trangThaiKC').value.trim();
    if (!tenKC) {
        Swal.fire({
            title: "Lỗi!",
            text: "Tên kích cỡ không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    if (!trangThaiKC) {
        Swal.fire({
            title: "Lỗi!",
            text: "Trạng thái kích cỡ không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }
    const formData = new FormData(form);
    fetch('/admin/update/kich-co', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                title: "Cập nhật kích cỡ thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                const modalElement = document.querySelector('#productDetailModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
                form.reset();
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
    fetch('/admin/kich-co/list')  // URL lấy danh sách kích cỡ
        .then(response => response.json())
        .then(data => {
            const kichCoTableBody = document.querySelector('#kichCoTable tbody');
            const kichCoTableAnBody = document.querySelector('#kichCoTableAn tbody');
            const kichCoSelect = document.querySelector('#kichCo');

            // Kiểm tra nếu đang ở bảng "Kích cỡ Hoạt động"
            if (kichCoTableBody) {
                kichCoTableBody.innerHTML = '';
                data.forEach(kichCo => {
                    if (kichCo.trangThaiKC === 'Hoạt động') {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${kichCo.maKC}</td>
                            <td>${kichCo.tenKC}</td>
                            <td>${kichCo.moTa}</td>
                            <td>${kichCo.trangThaiKC}</td>                        
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
                    }
                });
            }

            // Kiểm tra nếu đang ở bảng "Kích cỡ Không hoạt động"
            if (kichCoTableAnBody) {
                kichCoTableAnBody.innerHTML = '';
                data.forEach(kichCo => {
                    if (kichCo.trangThaiKC === 'Không hoạt động') {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${kichCo.maKC}</td>
                            <td>${kichCo.tenKC}</td>                       
                            <td>${kichCo.moTa}</td>
                            <td>${kichCo.trangThaiKC}</td>
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
                        kichCoTableAnBody.appendChild(row);
                    }
                });
            }

            // Cập nhật danh sách chọn kích cỡ (nếu cần thiết)
            if (kichCoSelect) {
                kichCoSelect.innerHTML = '<option value="" disabled selected>Chọn kích cỡ</option>';
                data.forEach(kichCo => {
                    const option = document.createElement('option');
                    option.value = kichCo.id;
                    option.textContent = kichCo.tenKC;
                    kichCoSelect.appendChild(option);
                });
            }

            // Gọi lại hàm để gán sự kiện cho các nút Cập nhật
            attachUpdateButtonEvents();
        })
        .catch(error => {
            console.error('Lỗi khi cập nhật danh sách kích cỡ:', error);
        });
}
function handleAddLSP(event, form) {
    event.preventDefault();
    const maLSP = form.querySelector('#maLSP').value.trim();
    const tenLoai = form.querySelector('#tenLoai').value.trim();
    const moTa = form.querySelector('#moTa').value.trim();
    if (!maLSP) {
        Swal.fire({
            title: "Lỗi!",
            text: "Mã loại sản phẩm không được để trống.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return false;
    }
    if (!tenLoai) {
        Swal.fire({
            title: "Lỗi!",
            text: "Tên loại sản phẩm không được để trống.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return false;
    }
    if (!moTa) {
        Swal.fire({
            title: "Lỗi!",
            text: "Mô tả không được để trống.",
            icon: "error",
            confirmButtonText: "OK"
        });
        return false;
    }
    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/add/loai-san-pham', {  // URL thêm loại sản phẩm mới
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

    const tenLoai = form.querySelector('#tenLoai').value.trim(); // Lấy giá trị tên loại sản phẩm
    const trangThai = form.querySelector('#trangThai').value.trim(); // Lấy giá trị trạng thái

    // Validate: Kiểm tra tên loại sản phẩm và trạng thái
    if (!tenLoai) {
        Swal.fire({
            title: "Lỗi!",
            text: "Tên loại sản phẩm không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }

    if (!trangThai) {
        Swal.fire({
            title: "Lỗi!",
            text: "Trạng thái loại sản phẩm không được để trống!",
            icon: "error",
            confirmButtonText: "OK"
        });
        return;
    }

    const formData = new FormData(form);

    // Gửi dữ liệu form qua fetch API
    fetch('/admin/update/loai-san-pham', {  // URL cập nhật loại sản phẩm
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                title: "Cập nhật loại sản phẩm thành công!",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                const modalElement = document.querySelector('#productDetailModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
                form.reset();
                updateLoaiSanPhamList();  // Cập nhật lại danh sách sau khi thay đổi
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
            const loaiSanPhamTableHoatDong = document.querySelector('#loaiSanPhamTableHoatDong tbody');  // Bảng Hoạt động
            const loaiSanPhamTableKhongHoatDong = document.querySelector('#loaiSanPhamTableKhongHoatDong tbody');  // Bảng Không hoạt động

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
            if (loaiSanPhamTableHoatDong) {
                loaiSanPhamTableHoatDong.innerHTML = '';
                data.forEach(loaiSanPham => {
                    if (loaiSanPham.trangThai === 'Đang bán') {
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
                                        data-id="${loaiSanPham.idLoaiSP}"
                                        data-ma-lsp="${loaiSanPham.maLSP}"
                                        data-ten-loai="${loaiSanPham.tenLoai}"
                                        data-trang-thai="${loaiSanPham.trangThai}"
                                        data-mo-ta="${loaiSanPham.moTa}">
                                    Cập nhật
                                </button>
                            </td>
                        `;
                        loaiSanPhamTableHoatDong.appendChild(row);
                    }
                });
            }
            if (loaiSanPhamTableKhongHoatDong) {
                loaiSanPhamTableKhongHoatDong.innerHTML = '';
                data.forEach(loaiSanPham => {
                    if (loaiSanPham.trangThai === 'Ngừng bán') {
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
                                        data-id="${loaiSanPham.idLoaiSP}"
                                        data-ma-lsp="${loaiSanPham.maLSP}"
                                        data-ten-loai="${loaiSanPham.tenLoai}"
                                        data-trang-thai="${loaiSanPham.trangThai}"
                                        data-mo-ta="${loaiSanPham.moTa}">
                                    Cập nhật
                                </button>
                            </td>
                        `;
                        loaiSanPhamTableKhongHoatDong.appendChild(row);
                    }
                });
            }
            attachUpdateButtonEvents();
        })
        .catch(error => {
            console.error('Lỗi khi cập nhật danh sách loại sản phẩm:', error);
        });
}