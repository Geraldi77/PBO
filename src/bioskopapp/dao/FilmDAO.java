
package bioskopapp.dao;

import bioskopapp.model.Film;
import bioskopapp.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO {

    public void addFilm(Film film) throws SQLException {
        String sql = "INSERT INTO films (judul, genre, durasi, sinopsis, poster_path) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, film.getJudul());
            pstmt.setString(2, film.getGenre());
            pstmt.setInt(3, film.getDurasi());
            pstmt.setString(4, film.getSinopsis());
            pstmt.setString(5, film.getPosterPath());
            pstmt.executeUpdate();
            System.out.println("Film added successfully.");
        } finally {
            DatabaseConnection.closeConnection(conn);
            if (pstmt != null) pstmt.close();
        }
    }

    public List<Film> getAllFilms() throws SQLException {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT film_id, judul, genre, durasi, sinopsis, poster_path FROM films";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Film film = new Film(
                    rs.getInt("film_id"),
                    rs.getString("judul"),
                    rs.getString("genre"),
                    rs.getInt("durasi"),
                    rs.getString("sinopsis"),
                    rs.getString("poster_path")
                );
                films.add(film);
            }
        } finally {
            DatabaseConnection.closeConnection(conn);
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        return films;
    }

    public void updateFilm(Film film) throws SQLException {
        String sql = "UPDATE films SET judul = ?, genre = ?, durasi = ?, sinopsis = ?, poster_path = ? WHERE film_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, film.getJudul());
            pstmt.setString(2, film.getGenre());
            pstmt.setInt(3, film.getDurasi());
            pstmt.setString(4, film.getSinopsis());
            pstmt.setString(5, film.getPosterPath());
            pstmt.setInt(6, film.getId());
            pstmt.executeUpdate();
            System.out.println("Film updated successfully.");
        } finally {
            DatabaseConnection.closeConnection(conn);
            if (pstmt != null) pstmt.close();
        }
    }

    public void deleteFilm(int filmId) throws SQLException {
        String sql = "DELETE FROM films WHERE film_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, filmId);
            pstmt.executeUpdate();
            System.out.println("Film deleted successfully.");
        } finally {
            DatabaseConnection.closeConnection(conn);
            if (pstmt != null) pstmt.close();
        }
    }
}