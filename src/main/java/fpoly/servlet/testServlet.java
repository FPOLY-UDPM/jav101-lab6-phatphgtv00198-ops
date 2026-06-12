package fpoly.servlet;

import dao.DepartmentDAO;
import entity.Department;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/testdb")
public class testServlet extends HttpServlet {

    private final DepartmentDAO dao = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Lấy danh sách dữ liệu từ Model (Gọi hàm findAll() hợp lệ trong DAO)
        List<Department> list = dao.findAll();

        // 2. [Tùy chọn] In ra Console để bạn kiểm tra nhanh dữ liệu giống như hàm check cũ
        System.out.println("--- Kiểm tra dữ liệu lấy từ Database ---");
        for (Department dept : list) {
            System.out.println(dept.getId() + " | " + dept.getName() + " | " + dept.getDescription());
        }
        System.out.println("----------------------------------------");

        // 3. Đóng gói danh sách vào Request Attribute để gửi cho View (JSP)
        req.setAttribute("departments", list);

        // 4. Chuyển hướng sang file hiển thị department-list.jsp
        req.getRequestDispatcher("/view_dept/department-list.jsp").forward(req, resp);
    }
}