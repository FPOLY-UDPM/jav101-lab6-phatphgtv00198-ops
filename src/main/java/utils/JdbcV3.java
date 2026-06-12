package utils;

import java.sql.*;

public class JdbcV3 {
    static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dburl = "jdbc:sqlserver://127.0.0.1:1433;databaseName=QLSV;encrypt=false";
    static String username = "userQLSV";
    static String password = "password";

    static {
        try { // nạp driver
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /**Mở kết nối*/
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dburl, username, password);
    }
    /**Thao tác dữ liệu*/
    public static int executeUpdate(String sql, Object... values) throws SQLException {
        //String sql : là câu lệnh sql để thực hiện thao tác với dữ liệu
        //Object... values : là CÁC THAM SỐ nếu có kèm theo trong câu lệnh sql

        Connection connection = getConnection();
        // Đổi câu lệnh sử dụng PreparedStatement ...
//        PreparedStatement statement = connection.prepareStatement(sql);
        // ... bằng câu lệnh sử dụng CallableStatement
        CallableStatement statement = connection.prepareCall(sql);

        // Đây là vòng lặp để gán các giá trị value vào tham số tương ứng của câu lệnh sql
        for (int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
        return statement.executeUpdate();
    }
    /**Truy vấn dữ liệu*/
    public static ResultSet executeQuery(String sql, Object... values) throws
            SQLException {
        //String sql : là câu lệnh sql để thực hiện thao tác với dữ liệu
        //Object... values : là CÁC THAM SỐ nếu có kèm theo trong câu lệnh sql

        Connection connection = getConnection();
        // Đổi câu lệnh sử dụng PreparedStatement ...
//        PreparedStatement statement = connection.prepareStatement(sql);
        CallableStatement statement = connection.prepareCall(sql);

        // Đây là vòng lặp để gán các giá trị value vào tham số tương ứng của câu lệnh sql
        for (int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
        return statement.executeQuery();
    }
}
