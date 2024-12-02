document.addEventListener("DOMContentLoaded", async function () {
   const table = document.getElementById("table-donhang");
   const khachHang = JSON.parse(sessionStorage.getItem("KH")) || [];

   try {
       const response = await fetch('/autokid/account/show-tracking', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json'
           },
           body: JSON.stringify({ idKH: khachHang.idKH }),
       });

       if (!response.ok) {
           throw new Error(`HTTP error! status: ${response.status}`);
       } else {
           const data = await response.json();
           console.log(data);
           renderTable(table, data);
       }
   } catch (error) {
       console.log("ERROR: ", error);
   }
});

function renderTable(table, data) {
    table.innerHTML = `
        <table class="table-donhang">
            <thead class="header-donhang">
                <th>Mã đơn hàng</th>
                <th>Tổng tiền</th>
                <th>Ngày mua</th>
                <th>Trạng thái</th>
                <th></th>
            </thead>
            <tbody class="body-donhang"></tbody>
        </table>
    `;
    const tbody = table.querySelector('.body-donhang');

    data.forEach(row => {
        const tr = document.createElement('tr');
        let statusColor;

        switch (row.trangThaiDH) {
            case "Chưa thanh toán, chờ giao hàng":
                statusColor = '#ba8b00';
                break;
            case "Đã thanh toán, chờ giao hàng":
                statusColor = "#5f2eea";
                break;
            case "Đã thanh toán, đang giao hàng":
                statusColor = "#007fff";
                break;
            case "Hủy đơn hàng":
                statusColor = "#c82333";
                break;
            case "Chưa thanh toán, đang giao hàng":
                statusColor = "orange";
                break;
            case "Hoàn thành":
                statusColor = "green";
                break;
            default:
                statusColor = "#000";
        }

        tr.innerHTML = `
            <td>${row.maDH}</td>
            <td>${row.tongTien} VNĐ</td>
            <td>${row.ngayMuaHang}</td>
            <td style="color: ${statusColor};"><strong>${row.trangThaiDH}</strong></td>
            <td>
                <a class="btn-detail-donhang" href="http://localhost:8080/autokid/account/detail-order?idDH=${row.idDH}">Chi tiết</a>
            </td>
        `;
        tbody.appendChild(tr);
    });
}