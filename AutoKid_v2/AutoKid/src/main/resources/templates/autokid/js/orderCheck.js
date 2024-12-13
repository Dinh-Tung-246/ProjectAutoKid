async function searchOrderByMaHD(event) {
    event.preventDefault();
    const maHD = document.getElementById('search__order').value;
    const payload= {
        maHD: maHD,
    }
    console.log("maHD: ", maHD);
    const response = await fetch('/autokid/account/search-order', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
    });

    const result = await response.json();
    if (result.idHD === 'null') {
        Swal.fire({
            title: 'Không tìm thấy đơn hàng!',
            text: 'Mã hóa đơn sai hãy kiểm tra lại',
            icon: 'warning',
            confirmButtonText: 'OK',
        });
        return;
    } else {
        const idHD = result.idHD;
        window.location.href = `http://localhost:8080/autokid/account/detail-order?idDH=${idHD}`;
    }
}