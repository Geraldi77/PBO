/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Model untuk merepresentasikan data jadwal tayang (schedule).
 */
public class Schedule {
    private int id;
    private int filmId;
    private String filmJudul; // <-- KUNCI: Variabel untuk menyimpan judul film
    private LocalDate tanggalTayang;
    private LocalTime waktuMulai;
    private double hargaTiket;
    private String studio;

    /**
     * Constructor untuk membuat jadwal BARU (tidak perlu judul film).
     */
    public Schedule(int filmId, LocalDate tanggalTayang, LocalTime waktuMulai, double hargaTiket, String studio) {
        this.filmId = filmId;
        this.tanggalTayang = tanggalTayang;
        this.waktuMulai = waktuMulai;
        this.hargaTiket = hargaTiket;
        this.studio = studio;
    }

    /**
     * KUNCI: Constructor untuk membaca data DARI DATABASE, termasuk judul film dari hasil JOIN.
     * ScheduleDAO akan memanggil constructor ini.
     */
    public Schedule(int id, int filmId, String filmJudul, LocalDate tanggalTayang, LocalTime waktuMulai, double hargaTiket, String studio) {
        this.id = id;
        this.filmId = filmId;
        this.filmJudul = filmJudul; // Menyimpan judul film
        this.tanggalTayang = tanggalTayang;
        this.waktuMulai = waktuMulai;
        this.hargaTiket = hargaTiket;
        this.studio = studio;
    }
    
    /**
     * Constructor alternatif yang digunakan oleh getSchedulesByFilmId (tidak selalu butuh judul).
     */
     public Schedule(int id, int filmId, LocalDate tanggalTayang, LocalTime waktuMulai, double hargaTiket, String studio) {
        this.id = id;
        this.filmId = filmId;
        this.tanggalTayang = tanggalTayang;
        this.waktuMulai = waktuMulai;
        this.hargaTiket = hargaTiket;
        this.studio = studio;
        this.filmJudul = ""; // Bisa diisi default atau dibiarkan null
    }


    // --- Getters ---
    public int getId() { return id; }
    public int getFilmId() { return filmId; }
    public String getFilmJudul() { return filmJudul; } // <-- KUNCI: Getter untuk menampilkan judul di tabel
    public LocalDate getTanggalTayang() { return tanggalTayang; }
    public LocalTime getWaktuMulai() { return waktuMulai; }
    public double getHargaTiket() { return hargaTiket; }
    public String getStudio() { return studio; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setFilmId(int filmId) { this.filmId = filmId; }
    public void setFilmJudul(String filmJudul) { this.filmJudul = filmJudul; }
    public void setTanggalTayang(LocalDate tanggalTayang) { this.tanggalTayang = tanggalTayang; }
    public void setWaktuMulai(LocalTime waktuMulai) { this.waktuMulai = waktuMulai; }
    public void setHargaTiket(double hargaTiket) { this.hargaTiket = hargaTiket; }
    public void setStudio(String studio) { this.studio = studio; }
}

