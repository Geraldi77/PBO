
package bioskopapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane; 

public class DatabaseConnection {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bioskop_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";     

    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Database connected!");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "JDBC Driver tidak ditemukan! Pastikan Anda telah menambahkan MySQL Connector/J ke Libraries proyek Anda.\nError: " + e.getMessage(), "Kesalahan Koneksi Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found", e);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal terhubung ke database. Pastikan MySQL berjalan dan pengaturan koneksi sudah benar.\nError: " + e.getMessage(), "Kesalahan Koneksi Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            throw e; 
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}