
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

    
    public void addSeats(int scheduleId, int rows, int seatsPerRow) throws SQLException {
        String sql = "INSERT INTO seats (schedule_id, nomor_kursi, status) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            for (char rowChar = 'A'; rowChar < ('A' + rows); rowChar++) {
                for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                    String nomorKursi = String.valueOf(rowChar) + seatNum;
                    pstmt.setInt(1, scheduleId);
                    pstmt.setString(2, nomorKursi);
                    pstmt.setString(3, "available"); 
                    pstmt.addBatch(); 
                }
            }
            pstmt.executeBatch(); 
            conn.commit(); 
            System.out.println("Seats added successfully for schedule ID: " + scheduleId);
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (pstmt != null) pstmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    
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