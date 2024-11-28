

function confirmOrder(event, liElement) {
    event.preventDefault();

    const trangThai = 'Đang giao hàng';
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