function handleButtonClick() {
    const KH = JSON.parse(sessionStorage.getItem("KH")) || [];

    if (KH.length !== 0) {
        const idKH = KH.idKH;
        window.location.href = `http://localhost:8080/autokid/account/order-tracking?idKH=${idKH}`;
    } else {
        window.location.href = "http://localhost:8080/autokid/home";
    }
}