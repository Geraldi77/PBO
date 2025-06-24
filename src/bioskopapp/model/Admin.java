/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.model;

public class Admin {
    private int id; // ID admin, biasanya auto-increment di DB
    private String username;
    private String password; // Dalam aplikasi nyata, ini harus di-hash!

    // Constructor untuk membuat objek Admin baru tanpa ID (saat menambah ke DB)
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Constructor untuk membuat objek Admin dengan ID (saat mengambil dari DB)
    public Admin(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // --- Setters (jika diperlukan, misalnya jika ID di-generate setelah insert) ---
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
               "id=" + id +
               ", username='" + username + '\'' +
               '}'; // Jangan tampilkan password di toString untuk keamanan
    }
}