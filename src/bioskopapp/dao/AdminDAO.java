/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.dao;

import bioskopapp.model.Admin;
import bioskopapp.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public boolean authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            return rs.next(); // Jika ada baris yang cocok, berarti otentikasi berhasil
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Metode untuk menambahkan admin baru (hanya untuk keperluan inisialisasi awal).
     *
     * @param admin Objek Admin yang akan ditambahkan.
     */
    public void addAdmin(Admin admin) throws SQLException {
        String sql = "INSERT INTO admins (username, password) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, admin.getPassword());
            pstmt.executeUpdate();
            System.out.println("Admin added successfully: " + admin.getUsername());
        } finally {
            if (pstmt != null) pstmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    // Anda bisa menambahkan metode lain seperti:
    // - getAdminByUsername(String username)
    // - updatePassword(Admin admin, String newPassword)
    // - deleteAdmin(int adminId)
    // Namun, untuk aplikasi bioskop sederhana, otentikasi mungkin sudah cukup.
}
