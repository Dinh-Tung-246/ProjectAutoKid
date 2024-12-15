const myModal = document.getElementById('myModal')
const myInput = document.getElementById('myInput')

myModal.addEventListener('shown.bs.modal', () => {
    myInput.focus()
})

function showDetail(button) {
    document.getElementById('modalIdKM').value = button.getAttribute("data-id");
    document.getElementById('modalMaKM').value = button.getAttribute("data-ma");
    document.getElementById('modalTenKM').value = button.getAttribute("data-ten");
    document.getElementById('modalBD').value = button.getAttribute("data-batDau");
    document.getElementById('modalKT').value = button.getAttribute("data-ketThuc");
    document.getElementById('modalGiaTri').value = button.getAttribute("data-giaTri");
    document.getElementById('modalTT').value = button.getAttribute("data-tt");
    document.getElementById('detailModal').style.display = 'block';
}

const selectElement = document.getElementById('modalTT');
const statusText = document.getElementById('statusText');

function updateStatusText() {
    const value = selectElement.value;
    statusText.textContent = value === "1" ? "Trạng thái: Hoạt động" : "Trạng thái: Không hoạt động";
}

selectElement.addEventListener('change', updateStatusText);

window.addEventListener('load', updateStatusText);

async function checkValidateAdd(event) {
    event.preventDefault();

    const formAdd = document.getElementById('formAdd');
    const decimalRegex = /^[1-9]\d*(\.\d+)?$/;

    let ma = document.getElementById('maKM').value.trim();
    let ten = document.getElementById('tenKM').value.trim();
    let giaTri = document.getElementById('giaTri').value.trim();
    let ngayBatDau = document.getElementById('ngayBatDau').value;
    let ngayKetThuc = document.getElementById('ngayKetThuc').value;
    let trangThai = document.getElementById('trangThaiKM').value;

    if (!ma || ma.length < 3 || !/^[A-Za-z0-9]+$/.test(ma)) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi" ,
            text: "Mã khuyến mãi phải có ít nhất 3 ký tự và chỉ bao gồm chữ cái, số!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    const makhuyenmai = {
        ma: ma
    }
    const response = await fetch('/admin/promotion/check-ma', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(makhuyenmai),
    });
    if (!response.ok) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi" ,
            text: "Mã khuyến mãi không được trùng!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!ten || ten.length < 3) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Tên khuyến mãi phải có ít nhất 3 ký tự!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTri || isNaN(giaTri)) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng nhập giá trị!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTri || !decimalRegex.test(giaTri)) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng nhập đúng định dạng giá trị",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTri || isNaN(giaTri) <= 0) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng nhập từ 1 đến 100",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }


    if (!ngayBatDau || !ngayKetThuc) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng chọn ngày bắt đầu và ngày kết thúc!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }


    if (new Date(ngayBatDau) > new Date(ngayKetThuc)) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Ngày bắt đầu không được lớn hơn ngày kết thúc!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!trangThai) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng chọn trạng thái!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    Swal.fire({
        background: "#fff",
        icon: "success",
        title: "Thành công",
        customClass: {
            title: 'my-custom-title'
        }
    }).then(() => {
        formAdd.submit();
    });
}

function validateInputAdd() {
    const giaTriInput = document.getElementById("giaTri");
    const value = giaTriInput.value.trim();
    const numericValue = parseFloat(value);

    if (isNaN(numericValue) || numericValue <= 0 || numericValue > 100 || !Number.isInteger(numericValue)) {
        Swal.fire({
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng nhập từ 1 đến 100",
            confirmButtonText: "OK",
            background: "#fff",
            customClass: {
                confirmButton: "btn btn-primary",
                title: 'my-custom-title'
            },
            buttonsStyling: false
        });

        giaTriInput.value = "";
    }
}

function checkValidateUpdate(event) {
    event.preventDefault();

    const formUpdate = document.getElementById('formUpdate');

    const decimalRegex = /^[1-9]\d*(\.\d+)?$/;

    let ma = document.getElementById('modalMaKM').value.trim();
    let ten = document.getElementById('modalTenKM').value.trim();
    let giaTri = document.getElementById('modalGiaTri').value.trim();
    let ngayBatDau = document.getElementById('modalBD').value;
    let ngayKetThuc = document.getElementById('modalKT').value;
    let trangThai = document.getElementById('modalTT').value;

    if (!ten || ten.length < 3) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Tên khuyến mãi phải có ít nhất 3 ký tự!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTri || isNaN(giaTri)) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng nhập giá trị!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTri || !decimalRegex.test(giaTri)) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng nhập đúng định dạng giá trị",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTri || isNaN(giaTri) < 0) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng nhập từ 1 đến 100",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }


    if (!ngayBatDau || !ngayKetThuc) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng chọn ngày bắt đầu và ngày kết thúc!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }


    if (new Date(ngayBatDau) > new Date(ngayKetThuc)) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Ngày bắt đầu không được lớn hơn ngày kết thúc!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!trangThai) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng chọn trạng thái!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    Swal.fire({
        background: "#fff",
        icon: "success",
        title: "Thành công",
        customClass: {
            title: 'my-custom-title'
        }
    }).then(() => {
        formUpdate.submit();
    });
}

function validateInputUpdate() {
    const giaTriInput = document.getElementById("modalGiaTri");
    const value = giaTriInput.value.trim();
    const numericValue = parseFloat(value);

    if (isNaN(numericValue) || numericValue <= 0 || numericValue > 100 || !Number.isInteger(numericValue)) {
        Swal.fire({
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng nhập từ 1 đến 100",
            confirmButtonText: "OK",
            background: "#fff",
            customClass: {
                confirmButton: "btn btn-primary",
                title: 'my-custom-title'
            },
            buttonsStyling: false
        });

        giaTriInput.value = "";
    }
}