/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.dao;
import bioskopapp.model.Transaction;
import bioskopapp.model.TransactionDetail;
import bioskopapp.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    /**
     * Menambahkan transaksi pemesanan tiket baru ke database.
     * Diperbarui untuk menyimpan metode dan status pembayaran.
     */
    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (schedule_id, nomor_kursi, nama_pemesan, tanggal_pesan, total_bayar, metode_pembayaran, status_pembayaran) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, transaction.getScheduleId());
            pstmt.setString(2, transaction.getNomorKursi());
            pstmt.setString(3, transaction.getNamaPemesan());
            pstmt.setTimestamp(4, Timestamp.valueOf(transaction.getTanggalPesan()));
            pstmt.setDouble(5, transaction.getTotalBayar());
            pstmt.setString(6, transaction.getMetodePembayaran());
            pstmt.setString(7, transaction.getStatusPembayaran());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                transaction.setId(rs.getInt(1));
            }
            if (rs != null) rs.close();
            System.out.println("Transaction added successfully with ID: " + transaction.getId());
        } finally {
            if (pstmt != null) pstmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }
    
    // Metode getTransactionDetails() dan lainnya tetap sama
    public List<TransactionDetail> getTransactionDetails() throws SQLException {
        List<TransactionDetail> details = new ArrayList<>();
        String sql = "SELECT t.transaction_id, f.judul, s.tanggal_tayang, s.waktu_mulai, " +
                     "t.nomor_kursi, t.nama_pemesan, t.total_bayar, t.tanggal_pesan " +
                     "FROM transactions t " +
                     "JOIN schedules s ON t.schedule_id = s.schedule_id " +
                     "JOIN films f ON s.film_id = f.film_id " +
                     "ORDER BY t.tanggal_pesan DESC";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            while (rs.next()) {
                String scheduleInfo = rs.getDate("tanggal_tayang").toLocalDate().toString() + 
                                      ", " + rs.getTime("waktu_mulai").toLocalTime().format(timeFormatter);

                TransactionDetail detail = new TransactionDetail(
                    rs.getInt("transaction_id"),
                    rs.getString("judul"),
                    scheduleInfo,
                    rs.getString("nomor_kursi"),
                    rs.getString("nama_pemesan"),
                    rs.getDouble("total_bayar"),
                    rs.getTimestamp("tanggal_pesan").toLocalDateTime()
                );
                details.add(detail);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return details;
    }
}