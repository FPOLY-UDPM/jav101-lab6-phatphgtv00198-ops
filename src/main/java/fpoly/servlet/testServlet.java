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

        // Chạy hàm test in ra console của bạn (nếu cần xem ở dưới)
        dao.checkDepartmentDAO();

        // 1. Lấy mảng dữ liệu từ Model (Hàm findAll đã gộp của bạn và thầy)
        List<Department> list = dao.findAll();

        // 2. Đóng gói danh sách vào Request Attribute để gửi cho View (JSP)
        req.setAttribute("departments", list);

        // 3. Chuyển hướng sang file hiển thị department-list.jsp
        req.getRequestDispatcher("/view_dept/department-list.jsp").forward(req, resp);
    }
}