document.addEventListener("DOMContentLoaded", function (){
    const nv = JSON.parse(sessionStorage.getItem("infoNV")) || [];
    console.log("ten before:", nv.tenNV);
    let tenNV = nv.tenNV;
    tenNV = tenNV.replaceAll("+", " ");
    console.log("name after: ", tenNV);
    document.getElementById("name-user").innerText = tenNV;
});

function confirmOrder(event, liElement) {
    event.preventDefault();

    const valueMess = document.getElementById('value-mess').value;
    let trangThai;
    if (valueMess === 'Đã thanh toán, chờ giao hàng') {
        trangThai = 'Đã thanh toán, đang giao hàng';
    } else {
        trangThai = 'Chưa thanh toán, đang giao hàng'
    }

    const idHD = liElement.querySelector('input[class="idDH"]').value.trim();

    const data = {
        trangThai: trangThai,
        idHD: idHD
    }
    console.log('trangthai: ' + trangThai);
    console.log('idDH' + idHD);

    Swal.fire({
        title: "Bạn có chắc chắn muốn xác nhận đơn hàng này?",
        icon: "question",
        showCancelButton: true,
        cancelButtonText: "không",
        confirmButtonText: "Có",
        cancelButtonColor: "#d33",
        confirmButtonColor: "#3085d6",
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/api/update-status', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json' // Đặt kiểu Content-Type là JSON
                },
                body: JSON.stringify(data)
            }).then(response => response.text())
                .then((result) => {
                    console.log(result);
                })
                .catch((error) => {
                    alert('ERROR' + error);
                });
            Swal.fire({
                title: "Xác nhận đơn hàng thành công",
                icon: "success",
            });
            setTimeout(() => {
                window.location.href = "http://localhost:8080/admin/customer-management/";
            }, 1000);
        }
    })
}

function unconfirmOrder(event, liElement) {
    event.preventDefault();
    let trangThai = "Hủy đơn hàng";

    const idHD = liElement.querySelector('input[class="idDH"]').value.trim();

    const data = {
        trangThai: trangThai,
        idHD: idHD
    }
    console.log('trangthai: ' + trangThai);
    console.log('idDH' + idHD);

    Swal.fire({
        title: "Bạn có chắc chắn muốn hủy đơn hàng này?",
        icon: "question",
        showCancelButton: true,
        cancelButtonText: "không",
        confirmButtonText: "Có",
        cancelButtonColor: "#d33",
        confirmButtonColor: "#3085d6",
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/api/update-status', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json' // Đặt kiểu Content-Type là JSON
                },
                body: JSON.stringify(data)
            }).then(response => response.text())
                .then((result) => {
                    console.log(result);
                })
                .catch((error) => {
                    alert('ERROR' + error);
                });
            Swal.fire({
                title: "Hủy đơn hàng thành công",
                icon: "success",
            });
            setTimeout(() => {
                window.location.href = "http://localhost:8080/admin/customer-management/";
            }, 1000);
        }
    })
}