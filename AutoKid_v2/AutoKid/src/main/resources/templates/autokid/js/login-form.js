window.addEventListener('load', function () {
    // Hiển thị loader khi trang bắt đầu tải
    var loader = document.getElementById('preloder');

    // Sau khi trang tải xong, làm mờ loader trong 2 giây
    setTimeout(function () {
        var opacity = 1;  // Khởi tạo opacity
        var fadeInterval = setInterval(function () {
            if (opacity <= 0) {
                clearInterval(fadeInterval); // Dừng hiệu ứng khi opacity đạt 0
                loader.style.display = 'none'; // Ẩn loader khi hiệu ứng kết thúc
            } else {
                opacity -= 0.05; // Giảm opacity dần
                loader.style.opacity = opacity; // Cập nhật opacity
            }
        }, 20); // Mỗi 50ms giảm opacity một chút
    }, 200); // Sau 2 giây mới bắt đầu hiệu ứng mờ
});

const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});

function register(event) {
    event.preventDefault();

    const form = document.getElementById('registerForm');
    const formData = new FormData(form);

    fetch('/autokid/login/register', {
        method: 'POST',
        body: formData
    }).then(response => response.text())
        .then(result => {
            if (result === 'sc') {
                Swal.fire({
                    title: "Đăng ký thành công!",
                    icon: "success",
                    confirmButtonText: 'OK',
                }).then(() => {
                    window.location.href = "http://localhost:8080/autokid/login/";
                });
            } else {
                Swal.fire({
                    title: result,
                    icon: "warning",
                    confirmButtonText: "OK",
                })
            }
        })
        .catch(error => {
            console.log('ERROR: ', error);
        });
}

function login(event) {
    event.preventDefault();

    const form = document.getElementById('loginForm');
    const formData = new FormData(form);

    fetch('/autokid/login/login', {
        method: 'POST',
        body: formData
    }).then(response => response.json())
        .then(result => {
            console.log(result['result']);
            if (result['result'] === 'sc') {
                localStorage.removeItem("KH");
                const KHObject = {idKH: result['idKH']
                    , tenKH: result['tenKH']
                    , emailKH: result['emailKH']
                    , sdtKH: result['sdtKH']
                    , diaChiKH: result['diaChiKH']
                    , pass: result['pass']
                    , tenNN: result['tenNN']
                    , sdtNN: result['sdtNN']
                    , diaChiNN: result['diaChiNN']
                }
                console.log(result['tenKH']);
                localStorage.setItem("KH",JSON.stringify(KHObject));
                Swal.fire({
                    title: "Đăng nhập thành công!",
                    icon: "success",
                    confirmButtonText: null,
                });
                setTimeout(function () {
                    window.location.href = "http://localhost:8080/autokid/home";
                }, 1000);

            } else {
                Swal.fire({
                    title: result['result'],
                    icon: 'warning',
                    confirmButtonText: 'OK'
                })
            }
        })
        .catch(error => {
            console.log('ERROR: ', error);
        });
}