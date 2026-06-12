package dao;

import entity.Department;
import utils.JdbcV1;
import utils.jdbcv2; // Giữ nguyên import lỗi đặt tên của bạn để tránh lỗi dự án
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
    private final String callSELECT = "exec spSelectAll";
    private final String callSELECT_byId = "exec spSelectById ?";
    private final String callSELECT_byName = "exec spSelectByName ?";
    private final String callINSERT = "exec spInsert ?,?,?";
    private final String callUPDATE = "exec spUpdate ?,?,?";
    private final String callDELETE_byId = "exec spDeleteById ?";

    /**
     * 1. Lấy toàn bộ danh sách phòng ban (Đã chuyển sang V3)
     */
    public List<Department> findAll() {
        List<Department> list = new ArrayList<>();
        // Đổi từ jdbcv2 sang JdbcV3 và truyền vào biến callSELECT
        try (ResultSet resultSet = JdbcV3.executeQuery(callSELECT)) {
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
     * 2. Tìm kiếm phòng ban theo ID (Đã chuyển sang V3)
     */
    public Department findById(String id) {
        Department dept = null;
        // Đổi sang JdbcV3 và truyền biến callSELECT_byId cùng tham số id
        try (ResultSet resultSet = JdbcV3.executeQuery(callSELECT_byId, id)) {
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
     * 3. Tìm kiếm phòng ban theo tên (Đã chuyển sang V3)
     */
    public List<Department> findByName(String name) {
        List<Department> list = new ArrayList<>();
        // Đổi sang JdbcV3 và dùng Stored Procedure tìm kiếm theo tên
        try (ResultSet resultSet = JdbcV3.executeQuery(callSELECT_byName, "%" + name + "%")) {
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
     * 4. Thêm mới phòng ban (Giữ nguyên V3)
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
     * 5. Cập nhật thông tin phòng ban (Giữ nguyên V3)
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
     * 6. Xóa phòng ban theo ID (Giữ nguyên V3)
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