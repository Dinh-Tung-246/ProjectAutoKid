const KhachHang = JSON.parse(localStorage.getItem('KH')) || [];

if (KhachHang) {
    document.querySelector('.tenKH').value = KhachHang.tenKH || '';
    document.querySelector('.emailKH').value = KhachHang.emailKH || '';
    document.querySelector('.idKH').value = KhachHang.idKH || '';
    document.querySelector('.diaChiKH').value = KhachHang.diaChiKH || '';
    document.querySelector('.sdtKH').value = KhachHang.sdtKH || '';
} else {
    console.log("No data")
}

document.querySelector('#update-info').addEventListener('submit', async function (event) {
    event.preventDefault();

    const idKH = document.querySelector('input[class="idKH"]').value.trim();
    const hoTenKH = document.querySelector('input[class="tenKH"]').value.trim();
    const emailKH = document.querySelector('input[class="emailKH"]').value.trim();
    const sdtKH = document.querySelector('input[class="sdtKH"]').value.trim();
    const diaChiKH = document.querySelector('input[class="diaChiKH"]').value.trim();

    const dataKH = JSON.parse(localStorage.getItem('KH')) || [];
    const matKhau = dataKH['pass'];

    if (!hoTenKH || !emailKH || !sdtKH || !diaChiKH) {
        Swal.fire({
            title: "Bạn không được để trống các trường!!!",
            icon: "warning",
            confirmButtonText: "OK",
        });
    } else {

        const khachHang = {
            idKH: idKH,
            tenKH: hoTenKH,
            emailKH: emailKH,
            matKhau: matKhau,
            sdtKH: sdtKH,
            diaChiKH: diaChiKH,
        }

        localStorage.setItem("KH", JSON.stringify(khachHang));

        console.log(khachHang);

        Swal.fire({
            title: "Bạn có chắc chắn muốn cập nhật thông tin không?",
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes"
        }).then(async (result) => {
            if (result.isConfirmed) {
                try {
                    const response = await fetch('/admin/customer-management/update', {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify({khachHang}),
                    });

                    if (response.ok) {
                        Swal.fire({
                            title: "Cập nhật thông tin thành công!",
                            icon: "success",
                            confirmButtonText: "OK",
                        }).then(() => {
                            window.location.href = "http://localhost:8080/autokid/home";
                        });
                    }
                } catch (error) {
                    console.log("ERROR", error);
                }
            }
        });
    }
})