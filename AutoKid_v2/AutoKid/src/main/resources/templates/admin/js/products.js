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

// Hàm chung để xử lý gửi form
// Hàm chung để xử lý gửi form
function handleFormSubmit(event, form, successMessage, redirectUrl) {
    // Ngăn chặn hành động mặc định của form
    event.preventDefault();

    // Gửi dữ liệu form qua fetch API
    const formData = new FormData(form);
    fetch(form.action, {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            // Hiển thị thông báo thành công
            Swal.fire({
                title: successMessage,
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                // Sau khi người dùng nhấn OK, tải lại trang
                window.location.href = redirectUrl;
            });
        } else {
            // Đọc nội dung lỗi từ server nếu có
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

    return false; // Ngăn chặn hành động gửi form mặc định
}

// Các hàm cụ thể sử dụng hàm chung
function handleAddTH(event, form) {
    return handleFormSubmit(event, form, "Thêm thương hiệu thành công!", "/admin/thuong-hieu");
}

function handleUpdateTH(event, form) {
    return handleFormSubmit(event, form, "Cập nhật thương hiệu thành công!", "/admin/thuong-hieu");
}

function handleAddCL(event, form) {
    return handleFormSubmit(event, form, "Thêm chất liệu thành công!", "/admin/chat-lieu");
}

function handleUpdateCL(event, form) {
    return handleFormSubmit(event, form, "Cập nhật chất liệu thành công!", "/admin/chat-lieu");
}

function handleAddSPCT(event, form) {
    return handleFormSubmit(event, form, "Thêm chi tiết sản phẩm thành công!", "/admin/products");
}

function handleUpdateSPCT(event, form) {
    return handleFormSubmit(event, form, "Cập nhật sản phẩm chi tiết thành công!", "/admin/products");
}

function handleAddMS(event, form) {
    return handleFormSubmit(event, form, "Thêm màu sắc thành công!", "/admin/mau-sac");
}

function handleUpdateMS(event, form) {
    return handleFormSubmit(event, form, "Cập nhật màu sắc thành công!", "/admin/mau-sac");
}

function handleAddKC(event, form) {
    return handleFormSubmit(event, form, "Thêm kích cỡ thành công!", "/admin/kich-co");
}

function handleUpdateKC(event, form) {
    return handleFormSubmit(event, form, "Cập nhật kích cỡ thành công!", "/admin/kich-co");
}

function handleAddLSP(event, form) {
    return handleFormSubmit(event, form, "Thêm loại sản phẩm thành công!", "/admin/loai-san-pham");
}

function handleUpdateLSP(event, form) {
    return handleFormSubmit(event, form, "Cập nhật loại sản phẩm thành công!", "/admin/loai-san-pham");
}