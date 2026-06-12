<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.Department" %>
<html>
<head>
    <title>Danh Sách Phòng Ban</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #ffffff;
        }
        .header-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 80%;
            margin-bottom: 20px;
        }
        h2 {
            color: #1a73e8;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin: 0;
        }
        .btn-add {
            background-color: #2ca54e;
            color: white;
            padding: 10px 15px;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            font-size: 14px;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }
        table {
            width: 80%;
            border-collapse: collapse;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        th {
            background-color: #343a40;
            color: white;
            text-align: left;
            padding: 12px;
            font-size: 15px;
        }
        td {
            padding: 12px;
            border-bottom: 1px solid #dee2e6;
            color: #333;
            font-size: 14px;
        }
        tr:nth-child(even) {
            background-color: #f8f9fa;
        }
        .btn-action {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            padding: 6px 12px;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-size: 12px;
            margin-right: 5px;
            border: none;
            cursor: pointer;
        }
        .btn-edit {
            background-color: #ffc107;
            color: #212529;
        }
        .btn-delete {
            background-color: #dc3545;
        }
    </style>
</head>
<body>

    <div class="header-container">
        <h2>Quản Lý Phòng Ban</h2>
        <a href="#" class="btn-add"><i class="fa-solid fa-circle-plus"></i> Thêm Phòng Ban Mới</a>
    </div>

    <table>
        <thead>
            <tr>
                <th style="width: 15%;">Mã Phòng Ban</th>
                <th style="width: 25%;">Tên Phòng Ban</th>
                <th style="width: 35%;">Mô Tả</th>
                <th style="width: 25%;">Hành Động</th>
            </tr>
        </thead>
        <tbody>
            <%
                // Lấy lại danh sách đã được Servlet gửi qua
                List<Department> list = (List<Department>) request.getAttribute("departments");
                if (list != null && !list.isEmpty()) {
                    for (Department dept : list) {
            %>
            <tr>
                <strong><td><%= dept.getId() %></td></strong>
                <td><%= dept.getName() %></td>
                <td><%= (dept.getDescription() != null ? dept.getDescription() : "") %></td>
                <td>
                    <a href="#" class="btn-action btn-edit"><i class="fa-solid fa-pen"></i> Sửa</a>
                    <a href="#" class="btn-action btn-delete"><i class="fa-solid fa-trash-can"></i> Xóa</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="4" style="text-align: center; color: red; font-style: italic;">Không có dữ liệu phòng ban nào!</td>
            </tr>
            <% } %>
        </tbody>
    </table>

</body>
</html>