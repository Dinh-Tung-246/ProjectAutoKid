document.addEventListener("DOMContentLoaded", () => {
    const cookies = document.cookie.split("; ");
    const userCookie = cookies.find(cookie => cookie.startsWith("infoNV="));
    if (userCookie) {
        const jsonNhanVien = decodeURIComponent(userCookie.split("=")[1]);
        sessionStorage.setItem("infoNV", jsonNhanVien);
        const  nv = JSON.parse(jsonNhanVien);
        console.log(nv);
    }
})

// ket noi den websocket
const socket = new WebSocket('ws://localhost:8080/ws');

// khi ket noi thanh cong
socket.onopen = function () {
    console.log("Da ket noi voi WebSocket");
}

//Khi nhan duoc thong bao tu server
socket.onmessage = function (event) {
    console.log("Du lieu nhan tu server: ", event.data);
    // updateUI()
    window.location.reload();
}

// Khi có lỗi
socket.onerror = function(error) {
    console.error("Lỗi WebSocket:", error);
};

// Khi kết nối bị đóng
socket.onclose = function() {
    console.log("Kết nối WebSocket đã đóng.");
};

function updateUI() {
    fetch('/admin/customer-management/order-fragment')
        .then(response => response.text())
        .then(html => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;

            const updateNotification = tempDiv.querySelector('.notification-wrapper').innerHTML;

            document.querySelector('.notification-wrapper').innerHTML = updateNotification;
        })
}

function showDetail(button) {
    document.getElementById("modalTenKH").value =
        button.getAttribute("data-ten");
    document.getElementById("modalEmail").value =
        button.getAttribute("data-email");
    document.getElementById("modalSdt").value =
        button.getAttribute("data-sdt");
    document.getElementById("modalDiaChi").value =
        button.getAttribute("data-diaChi");
    document.getElementById("modalTenNN").value =
        button.getAttribute("data-tenNN");
    document.getElementById("modalSdtNN").value =
        button.getAttribute("data-sdtNN");
    document.getElementById("modalDCNN").value =
        button.getAttribute("data-dcNN");
    document.getElementById("detailModal").style.display = "block";
    document.getElementById("overlay").style.display = "block";
}

function closeDetail() {
    // document.getElementById("detailModal").style.display = "none";
    // document.getElementById("overlay").style.display = "none";
    window.location.reload();
}

function showCreateForm() {
    document.getElementById("createModal").style.display = "block";
    document.getElementById("overlay").style.display = "block";
}

function closeCreate(event) {
    event.preventDefault();
    window.location.reload();
}

document.getElementById('submitBtn').addEventListener('click', function (event) {
    event.preventDefault();
    event.preventDefault();
    const formElement = document.getElementById('customerForm');
    const formData = new FormData(formElement);

    // Chuyển FormData sang JSON
    const jsonData = {};
    formData.forEach((value, key) => {
        jsonData[key] = value;
    });
    console.log(jsonData);
    fetch('/admin/customer-management/create-customer', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(jsonData),
    }) . then(response => response.text())
        .then(result => {
            if(result == 'sc') {
                Swal.fire({
                    title: "Thêm mới thành công",
                    icon: "success",
                    confirmButtonText: "OK",
                }) .then(() => {
                    window.location.href="http://localhost:8080/admin/customer-management/";
                })
            } else {
                Swal.fire({
                    title: result,
                    icon: "warning",
                    confirmButtonText: "OK",
                });
            }
        })
        .catch(error => {
            alert('ERROR: ' + error);
        });
});

