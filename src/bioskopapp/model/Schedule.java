
package bioskopapp.model;

import java.time.LocalDate;
import java.time.LocalTime;


public class Schedule {
    private int id;
    private int filmId;
    private String filmJudul; 
    private LocalDate tanggalTayang;
    private LocalTime waktuMulai;
    private double hargaTiket;
    private String studio;

    
    public Schedule(int filmId, LocalDate tanggalTayang, LocalTime waktuMulai, double hargaTiket, String studio) {
        this.filmId = filmId;
        this.tanggalTayang = tanggalTayang;
        this.waktuMulai = waktuMulai;
        this.hargaTiket = hargaTiket;
        this.studio = studio;
    }

    
    public Schedule(int id, int filmId, String filmJudul, LocalDate tanggalTayang, LocalTime waktuMulai, double hargaTiket, String studio) {
        this.id = id;
        this.filmId = filmId;
        this.filmJudul = filmJudul; 
        this.tanggalTayang = tanggalTayang;
        this.waktuMulai = waktuMulai;
        this.hargaTiket = hargaTiket;
        this.studio = studio;
    }
    
    
     public Schedule(int id, int filmId, LocalDate tanggalTayang, LocalTime waktuMulai, double hargaTiket, String studio) {
        this.id = id;
        this.filmId = filmId;
        this.tanggalTayang = tanggalTayang;
        this.waktuMulai = waktuMulai;
        this.hargaTiket = hargaTiket;
        this.studio = studio;
        this.filmJudul = ""; 
    }


    
    public int getId() { return id; }
    public int getFilmId() { return filmId; }
    public String getFilmJudul() { return filmJudul; } 
    public LocalDate getTanggalTayang() { return tanggalTayang; }
    public LocalTime getWaktuMulai() { return waktuMulai; }
    public double getHargaTiket() { return hargaTiket; }
    public String getStudio() { return studio; }

    public void setId(int id) { this.id = id; }
    public void setFilmId(int filmId) { this.filmId = filmId; }
    public void setFilmJudul(String filmJudul) { this.filmJudul = filmJudul; }
    public void setTanggalTayang(LocalDate tanggalTayang) { this.tanggalTayang = tanggalTayang; }
    public void setWaktuMulai(LocalTime waktuMulai) { this.waktuMulai = waktuMulai; }
    public void setHargaTiket(double hargaTiket) { this.hargaTiket = hargaTiket; }
    public void setStudio(String studio) { this.studio = studio; }
}

