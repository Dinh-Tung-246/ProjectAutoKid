const myModal = document.getElementById('myModal')
const myInput = document.getElementById('myInput')

myModal.addEventListener('shown.bs.modal', () => {
    myInput.focus()
})

function showDetail(button) {

    const dieuKien = formatToNumber(button.getAttribute("data-DK"));
    const giaTri = formatToNumber(button.getAttribute("data-giaTri"));
    const giaTriToiDa = formatToNumber(button.getAttribute("data-giaTriToiDa"));

    document.getElementById('modalIdVC').value = button.getAttribute("data-id");
    document.getElementById('modalMa').value = button.getAttribute("data-ma");
    document.getElementById('modalTen').value = button.getAttribute("data-ten");
    document.getElementById('modalDK').value = dieuKien;
    document.getElementById('modalGT').value = giaTri;
    document.getElementById('modalGTTD').value = giaTriToiDa;
    document.getElementById('modalNBD').value = button.getAttribute("data-NBD");
    document.getElementById('modalNKT').value = button.getAttribute("data-NKT");
    document.getElementById('modalTT').value = button.getAttribute("data-TT");
    document.getElementById('modalLVC').value = button.getAttribute("data-LVC");
    document.getElementById('detailModal').style.display = 'block';
}

function formatToNumber(value) {
    if (!value) return '';
    return value.replace(/,/g, ''); // Loại bỏ dấu phẩy
}

const selectTrangThai = document.getElementById('modalTT');
const selectLoai = document.getElementById('modalLVC');
const statusText = document.getElementById('statusText');

function updateTrangThai() {
    const value = selectTrangThai.value;
    statusText.textContent = value === "1" ? "Trạng thái: Hoạt động" : "Trạng thái: Không hoạt động";
}

function updateLoai() {
    const value = selectLoai.value;
    statusText.textContent = value === "1" ? "Trạng thái: Hoạt động" : "Trạng thái: Không hoạt động";
}

selectElement.addEventListener('change', updateTrangThai, updateLoai);

window.addEventListener('load', updateTrangThai, updateLoai);


async function checkValidateAdd(event) {
    event.preventDefault();

    const formAdd = document.getElementById('formAdd');

    let ma = document.getElementById('ma').value.trim();
    let ten = document.getElementById('ten').value.trim();
    let loaiVoucher = document.getElementById('loaiVoucher').value;
    let dieuKien = document.getElementById('dieuKien').value.trim();
    let giaTri = document.getElementById('giaTri').value.trim();
    let giaTriToiDa = document.getElementById('giaTriToiDa').value.trim();
    let ngayBatDau = document.getElementById('ngayBatDau').value;
    let ngayKetThuc = document.getElementById('ngayKetThuc').value;
    let trangThai = document.getElementById('trangThai').value;

    if (!ma || ma.length < 3 || !/^[A-Za-z0-9]+$/.test(ma)) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi" ,
            text: "Mã voucher phải có ít nhất 3 ký tự và chỉ bao gồm chữ cái, số!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    const mavoucher = {
        ma: ma,
    }
    const response = await fetch('/admin/voucher/check-ma', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(mavoucher),
    });
    if (!response.ok) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi" ,
            text: "Mã không được trùng!",
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
            text: "Tên voucher phải có ít nhất 3 ký tự!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!loaiVoucher) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng chọn loại voucher!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!dieuKien || isNaN(dieuKien) || dieuKien <= -1) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Điều kiện phải là một số lớn hơn 0!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTri || isNaN(giaTri) || giaTri <= 0) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Giá trị tối thiểu một nghìn đồng!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTri || isNaN(giaTri) || giaTri > 5000000) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Giá trị giảm tối đa là 5 triệu!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (loaiVoucher === '2' && giaTriToiDa !== giaTri) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Khi loại voucher là tiền, giá trị tối đa phải bằng giá trị!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTriToiDa || isNaN(giaTriToiDa) || giaTriToiDa <= 0) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Giá trị tối đa phải là một số lớn hơn 0 và lớn hơn hoặc bằng Giá trị!",
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

function checkValidateUpdate(event) {
    event.preventDefault();

    const from1 = document.getElementById("from1");

    let ma = document.getElementById('modalMa').value.trim();
    let ten = document.getElementById('modalTen').value.trim();
    let loaiVoucher= document.getElementById('modalLVC').value;
    let dieuKien = document.getElementById('modalDK').value.trim();
    let giaTri = document.getElementById('modalGT').value.trim();
    let giaTriToiDa = document.getElementById('modalGTTD').value.trim();
    let ngayBatDau = document.getElementById('modalNBD').value;
    let ngayKetThuc = document.getElementById('modalNKT').value;
    let trangThai = document.getElementById('modalTT').value;

    if (!ma || ma.length < 3 || !/^[A-Za-z0-9]+$/.test(ma)) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi" ,
            text: "Mã voucher phải có ít nhất 3 ký tự và chỉ bao gồm chữ cái, số!",
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
            text: "Tên voucher phải có ít nhất 3 ký tự!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!loaiVoucher) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Vui lòng chọn loại voucher!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!dieuKien || isNaN(dieuKien) || dieuKien <= -1) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Điều kiện phải là một số lớn hơn 0!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTri || isNaN(giaTri) || giaTri <= 0) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Giá trị tối thiểu một nghìn đồng!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTri || isNaN(giaTri) || giaTri > 5000000) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Giá trị giảm tối đa là 5 triệu!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (loaiVoucher === '2' && giaTriToiDa !== giaTri) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Khi loại voucher là tiền, giá trị tối đa phải bằng giá trị!",
            customClass: {
                title: 'my-custom-title'
            }
        });
        return;
    }

    if (!giaTriToiDa || isNaN(giaTriToiDa) || giaTriToiDa <= 0) {
        Swal.fire({
            background: "#fff",
            icon: "error",
            title: "Lỗi",
            text: "Giá trị tối đa phải là một số lớn hơn 0 và lớn hơn hoặc bằng Giá trị!",
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
        from1.submit();
    });
}

function deleteConfirm(event, element) {
    event.preventDefault();

    const deleteUrl = element.getAttribute("href");

    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
            confirmButton: "btn btn-success",
            cancelButton: "btn btn-danger"
        },
        buttonsStyling: false
    });

    swalWithBootstrapButtons.fire({
        background: "#fff",
        title: "Bạn có chắc chắn muốn xóa?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy",
        customClass: {
            title: 'my-custom-title',
            cancelButton: 'btn btn-danger',
            confirmButton: 'btn btn-success'
        },
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = deleteUrl;
        } else if (result.dismiss === Swal.DismissReason.cancel) {
            swalWithBootstrapButtons.fire({
                background: "#fff",
                title: "Hủy xóa !",
                icon: "error",
                customClass: {
                    title: 'my-custom-title',
                    confirmButton: 'btn btn-danger'
                },
            });
        }
    });
}

function percentOrCashAdd() {
    const loaiVoucher = document.getElementById("loaiVoucher").value;
    const giaTriInput = document.getElementById("giaTri");

    if (loaiVoucher === "1") {
        giaTriInput.setAttribute("placeholder", "Nhập giá trị phần trăm (0-100)");
        giaTriInput.setAttribute("max", "100");
        giaTriInput.setAttribute("min", "0");
        giaTriInput.value = "";
    } else if (loaiVoucher === "2") {
        giaTriInput.setAttribute("placeholder", "Nhập số tiền");
        giaTriInput.removeAttribute("max");
        giaTriInput.removeAttribute("min");
        giaTriInput.value = "";
    }
}

function percentOrCashUpdate() {
    const loaiVoucher = document.getElementById("modalLVC").value;
    const giaTriInput = document.getElementById("modalGT");
    // const from1 = document.getElementById("from1")

    if (loaiVoucher === "1") {
        giaTriInput.setAttribute("placeholder", "Nhập giá trị phần trăm (0-100)");
        giaTriInput.setAttribute("max", "100");
        giaTriInput.setAttribute("min", "0");
        giaTriInput.value = "";
    } else if (loaiVoucher === "2") {
        giaTriInput.setAttribute("placeholder", "Nhập số tiền");
        giaTriInput.removeAttribute("max");
        giaTriInput.removeAttribute("min");
        giaTriInput.value = "";
    }
}

function validateInputAdd() {
    const loaiVoucher = document.getElementById("loaiVoucher").value;
    const giaTriInput = document.getElementById("giaTri");
    const value = giaTriInput.value;

    if (loaiVoucher === "1") {
        if (value < 0 || value > 100 || isNaN(value)) {
            Swal.fire({
                icon: "error",
                title: "Lỗi",
                text: "Vui lòng nhập giá trị phần trăm hợp lệ (0-100).",
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
    } else if (loaiVoucher === "2") {
        if (isNaN(value) || value <= 0) {
            Swal.fire({
                icon: "error",
                title: "Lỗi nhập liệu",
                text: "Vui lòng nhập số tiền hợp lệ.",
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
}

function validateInputUpdate() {
    const loaiVoucher = document.getElementById("modalLVC").value;
    const giaTriInput = document.getElementById("modalGT");
    const value = giaTriInput.value;
    // const from1 = document.getElementById("from1")

    if (loaiVoucher === "1") {
        if (value < 0 || value > 100 || isNaN(value)) {
            Swal.fire({
                icon: "error",
                title: "Lỗi",
                text: "Vui lòng nhập giá trị phần trăm hợp lệ (0-100).",
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
    } else if (loaiVoucher === "2") {
        if (isNaN(value) || value <= 0) {
            Swal.fire({
                icon: "error",
                title: "Lỗi nhập liệu",
                text: "Vui lòng nhập số tiền hợp lệ.",
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
}