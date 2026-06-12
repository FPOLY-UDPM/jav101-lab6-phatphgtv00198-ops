package dao;

import entity.Department;
import utils.JdbcV1;
import utils.jdbcv2;
import utils.JdbcV3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    // --- SQL Queries (JDBC V1 & V2) ---
    private final String stmSELECT = "SELECT [Id], [Name], [Description] FROM [dbo].[Departments]";
    private final String stmSELECT_byId = """
            SELECT [Id], [Name], [Description] FROM [dbo].[Departments]
            WHERE [Id] = ?
            """;
    private final String stmSELECT_byName = """
            SELECT [Id], [Name], [Description] FROM [dbo].[Departments]
            WHERE [Name] LIKE ?
            """;

    private final String stmINSERT = "INSERT INTO [dbo].[Departments] ([Id], [Name], [Description]) VALUES (?, ?, ?)";
    private final String stmUPDATE = "UPDATE [dbo].[Departments] SET [Name] = ?, [Description] = ? WHERE [Id] = ?";
    private final String stmDELETE = "DELETE FROM [dbo].[Departments] WHERE [Id] = ?";

    // --- Stored Procedures (JDBC V3) ---
    // Sử dụng cú pháp {call ...} là chuẩn JDBC giúp Tomcat và Driver tối ưu tốt nhất
    private final String callSELECT = "{call spSelectAll}";
    private final String callSELECT_byId = "{call spSelectById(?)}";
    private final String callINSERT = "{call spInsert(?,?,?)}";
    private final String callUPDATE = "{call spUpdate(?,?,?)}";
    private final String callDELETE_byId = "{call spDeleteById(?)}";

    /**
     * 1. Lấy toàn bộ danh sách phòng ban
     */
    public List<Department> findAll() {
        List<Department> list = new ArrayList<>();
        // Sử dụng try-with-resources để tự động đóng ResultSet tránh leak kết nối trên Tomcat
        try (ResultSet resultSet = jdbcv2.executeQuery(stmSELECT)) {
            while (resultSet.next()) {
                Department dept = new Department();
                dept.setId(resultSet.getString("Id"));
                dept.setName(resultSet.getString("Name"));
                dept.setDescription(resultSet.getString("Description"));
                list.add(dept);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 2. Tìm kiếm phòng ban theo ID
     */
    public Department findById(String id) {
        Department dept = null; // Trả về null nếu không tìm thấy để Servlet dễ xử lý
        try (ResultSet resultSet = jdbcv2.executeQuery(stmSELECT_byId, id)) {
            if (resultSet.next()) {
                dept = new Department();
                dept.setId(resultSet.getString("Id"));
                dept.setName(resultSet.getString("Name"));
                dept.setDescription(resultSet.getString("Description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dept;
    }

    /**
     * 3. Tìm kiếm phòng ban theo tên (Tìm kiếm tương đối)
     */
    public List<Department> findByName(String name) {
        List<Department> list = new ArrayList<>();
        try (ResultSet resultSet = jdbcv2.executeQuery(stmSELECT_byName, "%" + name + "%")) {
            while (resultSet.next()) {
                Department dept = new Department();
                dept.setId(resultSet.getString("Id"));
                dept.setName(resultSet.getString("Name"));
                dept.setDescription(resultSet.getString("Description"));
                list.add(dept);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 4. Thêm mới phòng ban
     */
    public int insert(Department dept) {
        try {
            return JdbcV3.executeUpdate(callINSERT, dept.getId(), dept.getName(), dept.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 5. Cập nhật thông tin phòng ban
     */
    public int update(Department dept) {
        try {
            return JdbcV3.executeUpdate(callUPDATE, dept.getName(), dept.getDescription(), dept.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 6. Xóa phòng ban theo ID
     */
    public int delete(String id) {
        try {
            return JdbcV3.executeUpdate(callDELETE_byId, id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 7. Hàm lấy toàn bộ dữ liệu viết theo chuẩn đóng tài nguyên tự động nâng cao
     */
    public List<Department> getAll() {
        List<Department> list = new ArrayList<>();
        try (Connection conn = JdbcV3.getConnection();
             PreparedStatement stmt = conn.prepareStatement(callSELECT);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Department dept = new Department(
                        resultSet.getString("Id"),
                        resultSet.getString("Name"),
                        resultSet.getString("Description")
                );
                list.add(dept);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}