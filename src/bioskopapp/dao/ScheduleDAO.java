/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.dao;

import bioskopapp.model.Schedule;
import bioskopapp.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {

    public void addSchedule(Schedule schedule) throws SQLException {
        String sql = "INSERT INTO schedules (film_id, tanggal_tayang, waktu_mulai, harga_tiket, studio) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Ambil ID yang di-generate
            pstmt.setInt(1, schedule.getFilmId());
            pstmt.setDate(2, java.sql.Date.valueOf(schedule.getTanggalTayang()));
            pstmt.setTime(3, java.sql.Time.valueOf(schedule.getWaktuMulai()));
            pstmt.setDouble(4, schedule.getHargaTiket());
            pstmt.setString(5, schedule.getStudio());
            pstmt.executeUpdate();

            // Ambil ID jadwal yang baru dibuat
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                schedule.setId(rs.getInt(1)); // Set ID ke objek schedule
            }
            if (rs != null) rs.close();
            System.out.println("Schedule added successfully.");
        } finally {
            DatabaseConnection.closeConnection(conn);
            if (pstmt != null) pstmt.close();
        }
    }

    /**
     * KODE YANG DIPERBAIKI:
     * Metode ini sekarang melakukan JOIN dengan tabel 'films' untuk mendapatkan judul film.
     */
    public List<Schedule> getAllSchedules() throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        // SQL JOIN untuk mengambil judul film berdasarkan film_id
        String sql = "SELECT s.schedule_id, s.film_id, f.judul AS film_judul, s.tanggal_tayang, s.waktu_mulai, s.harga_tiket, s.studio " +
                     "FROM schedules s JOIN films f ON s.film_id = f.film_id ORDER BY s.tanggal_tayang, s.waktu_mulai";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Panggil constructor Schedule yang sudah bisa menerima judul film
                Schedule schedule = new Schedule(
                    rs.getInt("schedule_id"),
                    rs.getInt("film_id"),
                    rs.getString("film_judul"), // <-- Mengambil judul film dari hasil JOIN
                    rs.getDate("tanggal_tayang").toLocalDate(),
                    rs.getTime("waktu_mulai").toLocalTime(),
                    rs.getDouble("harga_tiket"),
                    rs.getString("studio")
                );
                schedules.add(schedule);
            }
        } finally {
            DatabaseConnection.closeConnection(conn);
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        return schedules;
    }

    public void updateSchedule(Schedule schedule) throws SQLException {
        String sql = "UPDATE schedules SET film_id = ?, tanggal_tayang = ?, waktu_mulai = ?, harga_tiket = ?, studio = ? WHERE schedule_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, schedule.getFilmId());
            pstmt.setDate(2, java.sql.Date.valueOf(schedule.getTanggalTayang()));
            pstmt.setTime(3, java.sql.Time.valueOf(schedule.getWaktuMulai()));
            pstmt.setDouble(4, schedule.getHargaTiket());
            pstmt.setString(5, schedule.getStudio());
            pstmt.setInt(6, schedule.getId());
            pstmt.executeUpdate();
            System.out.println("Schedule updated successfully.");
        } finally {
            DatabaseConnection.closeConnection(conn);
            if (pstmt != null) pstmt.close();
        }
    }

    public void deleteSchedule(int scheduleId) throws SQLException {
        String sql = "DELETE FROM schedules WHERE schedule_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, scheduleId);
            pstmt.executeUpdate();
            System.out.println("Schedule deleted successfully.");
        } finally {
            DatabaseConnection.closeConnection(conn);
            if (pstmt != null) pstmt.close();
        }
    }
    
    public List<Schedule> getSchedulesByFilmId(int filmId) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT schedule_id, film_id, tanggal_tayang, waktu_mulai, harga_tiket, studio " +
                     "FROM schedules WHERE film_id = ? ORDER BY tanggal_tayang, waktu_mulai";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, filmId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule(
                    rs.getInt("schedule_id"),
                    rs.getInt("film_id"),
                    rs.getDate("tanggal_tayang").toLocalDate(),
                    rs.getTime("waktu_mulai").toLocalTime(),
                    rs.getDouble("harga_tiket"),
                    rs.getString("studio")
                );
                schedules.add(schedule);
            }
        } finally {
            DatabaseConnection.closeConnection(conn);
            if (pstmt != null) pstmt.close();
            if (rs != null) rs.close();
        }
        return schedules;
    }
}