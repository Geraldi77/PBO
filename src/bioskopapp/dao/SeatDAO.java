/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.dao;
import bioskopapp.model.Seat;
import bioskopapp.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    /**
     * Menambahkan sejumlah kursi ke database untuk sebuah jadwal tayang.
     * Asumsi: Nomor kursi dibuat secara berurutan (misal: A1, A2, B1, dst.).
     * Anda bisa menyesuaikan logika penomoran kursi ini.
     *
     * @param scheduleId ID jadwal tayang.
     * @param rows Jumlah baris kursi (misal: A, B, C).
     * @param seatsPerRow Jumlah kursi per baris (misal: 1, 2, 3...).
     * @throws SQLException Jika terjadi kesalahan SQL.
     */
    public void addSeats(int scheduleId, int rows, int seatsPerRow) throws SQLException {
        String sql = "INSERT INTO seats (schedule_id, nomor_kursi, status) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Mulai transaksi
            pstmt = conn.prepareStatement(sql);

            for (char rowChar = 'A'; rowChar < ('A' + rows); rowChar++) {
                for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                    String nomorKursi = String.valueOf(rowChar) + seatNum;
                    pstmt.setInt(1, scheduleId);
                    pstmt.setString(2, nomorKursi);
                    pstmt.setString(3, "available"); // Status default
                    pstmt.addBatch(); // Tambahkan ke batch untuk eksekusi massal
                }
            }
            pstmt.executeBatch(); // Eksekusi semua perintah dalam batch
            conn.commit(); // Komit transaksi
            System.out.println("Seats added successfully for schedule ID: " + scheduleId);
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback jika ada error
            }
            throw e;
        } finally {
            if (pstmt != null) pstmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Mengambil semua kursi untuk jadwal tayang tertentu.
     *
     * @param scheduleId ID jadwal tayang.
     * @return List objek Seat.
     * @throws SQLException Jika terjadi kesalahan SQL.
     */
    public List<Seat> getSeatsByScheduleId(int scheduleId) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT seat_id, schedule_id, nomor_kursi, status FROM seats WHERE schedule_id = ? ORDER BY nomor_kursi";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, scheduleId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Seat seat = new Seat(
                    rs.getInt("seat_id"),
                    rs.getInt("schedule_id"),
                    rs.getString("nomor_kursi"),
                    rs.getString("status")
                );
                seats.add(seat);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return seats;
    }

    /**
     * Memperbarui status kursi (misal: dari 'available' menjadi 'booked').
     *
     * @param seat Objek Seat dengan ID dan status yang diperbarui.
     * @throws SQLException Jika terjadi kesalahan SQL.
     */
    public void updateSeatStatus(Seat seat) throws SQLException {
        String sql = "UPDATE seats SET status = ? WHERE seat_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, seat.getStatus());
            pstmt.setInt(2, seat.getId());
            pstmt.executeUpdate();
            System.out.println("Seat status updated for seat ID: " + seat.getId());
        } finally {
            if (pstmt != null) pstmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }
    
    /**
     * Menghapus semua kursi terkait dengan jadwal tertentu.
     * Ini penting saat jadwal dihapus.
     * @param scheduleId ID jadwal tayang.
     * @throws SQLException Jika terjadi kesalahan SQL.
     */
    public void deleteSeatsByScheduleId(int scheduleId) throws SQLException {
        String sql = "DELETE FROM seats WHERE schedule_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, scheduleId);
            pstmt.executeUpdate();
            System.out.println("Deleted seats for schedule ID: " + scheduleId);
        } finally {
            if (pstmt != null) pstmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }
}