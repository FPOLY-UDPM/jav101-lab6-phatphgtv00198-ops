package fpoly.servlet;

import dao.DepartmentDAO;
import entity.Department;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Hứng toàn bộ các đầu URL bắt đầu bằng /departments hoặc /department/id/{x} hoặc /department/name/{x}
@WebServlet(urlPatterns = {"/departments", "/department/*"})
public class DepartmentServlet extends HttpServlet {

    // Khởi tạo đối tượng DAO để gọi các hàm truy vấn dữ liệu
    DepartmentDAO deptDAO = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Luôn khởi tạo danh sách rỗng để tránh lỗi NullPointerException ở tầng hiển thị
        List<Department> resultList = new ArrayList<>();

        // 1. Lấy Servlet Path để nhận diện hành vi gốc
        // Ví dụ: gọi /departments -> trả về "/departments"
        // Ví dụ: gọi /department/id/IT -> trả về "/department"
        String servletPath = req.getServletPath();

        // 2. Lấy Path Info để lấy phần tham số mở rộng phía sau
        // Ví dụ: gọi /department/id/IT -> trả về "/id/IT"
        // Ví dụ: gọi /departments thô -> trả về null
        String pathInfo = req.getPathInfo();

        try {
            // TRƯỜNG HỢP 1: Người dùng gọi thẳng vào /departments -> Liệt kê tất cả
            if (servletPath.equals("/departments")) {
                resultList = deptDAO.findAll();
            }
            // TRƯỜNG HỢP 2: Người dùng gọi vào nhánh lọc dữ liệu /department/*
            else if (servletPath.equals("/department")) {

                if (pathInfo != null && !pathInfo.equals("/")) {
                    // Tách chuỗi pathInfo (Ví dụ: "/id/IT" -> ["", "id", "IT"])
                    String[] pathParts = pathInfo.split("/");

                    // KIỂM TRA AN TOÀN: Phải đủ tối thiểu 3 phần tử trong mảng mới xử lý
                    if (pathParts.length >= 3) {
                        String fieldname = pathParts[1].trim().toLowerCase(); // Lấy: id, name...
                        String keyword = pathParts[2].trim();                 // Lấy giá trị tìm kiếm

                        // RẼ NHÁNH ĐIỀU PHỐI TÌM KIẾM ĐỘNG
                        switch (fieldname) {
                            case "id":
                                Department d = deptDAO.findById(keyword);
                                if (d != null) {
                                    resultList.add(d);
                                }
                                break;

                            case "name":
                                resultList = deptDAO.findByName(keyword);
                                break;

                            default:
                                req.setAttribute("errorMessage", "Tiêu chí tìm kiếm '" + fieldname + "' không được hỗ trợ!");
                                break;
                        }
                    } else {
                        // Người dùng gõ thiếu (Ví dụ: /department/id hoặc /department/)
                        req.setAttribute("errorMessage", "Đường dẫn không hợp lệ! Vui lòng nhập đúng cấu trúc: /department/{tiêu_chí}/{từ_khóa}");
                    }
                } else {
                    // Người dùng gõ /department hoặc /department/ thô không có tham số
                    req.setAttribute("errorMessage", "Vui lòng chỉ định tiêu chí lọc dữ liệu (Ví dụ: /department/id/IT)!");
                }
            }

            // Đẩy duy nhất mảng kết quả ra cho JSP hiển thị
            req.setAttribute("departments", resultList);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Hệ thống gặp lỗi khi truy vấn: " + e.getMessage());
        }

        // Forward sang duy nhất một view hiển thị danh sách dùng chung
        req.getRequestDispatcher("/view_dept/department-list.jsp").forward(req, resp);
    }
}