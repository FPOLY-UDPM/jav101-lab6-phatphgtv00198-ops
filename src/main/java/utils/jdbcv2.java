package utils;

import java.sql.*;

public class jdbcv2 {
    // Cập nhật cấu hình mới theo yêu cầu của bạn
    static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dburl = "jdbc:sqlserver://localhost:1433;database=GP;encrypt=false";
    static String username = "sa";
    static String password = "123456";

    static {
        try { // Nạp driver
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /** Mở kết nối */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dburl, username, password);
    }

    /** Thao tác dữ liệu (Insert, Update, Delete) */
    public static int executeUpdate(String sql, Object... values) throws SQLException {
        // Sử dụng try-with-resources để tự động đóng connection và statement khi chạy xong trên Tomcat
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Gán các giá trị vào tham số
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }
            return statement.executeUpdate();
        }
    }

    /** Truy vấn dữ liệu (Select) */
    public static ResultSet executeQuery(String sql, Object... values) throws SQLException {
        // Đối với ResultSet, chúng ta KHÔNG ĐÓNG connection và statement ở đây
        // vì đóng lại sẽ làm ResultSet bị đóng theo, không đọc được dữ liệu ở tầng DAO/Controller.
        // Bạn cần phải đóng Connection thủ công sau khi đã đọc hết dữ liệu từ ResultSet.
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        for (int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
        return statement.executeQuery();
    }
}