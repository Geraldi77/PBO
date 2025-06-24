/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.model;

public class Film {
    private int id;
    private String judul;
    private String genre;
    private int durasi; // dalam menit
    private String sinopsis;
    private String posterPath; // path ke file gambar poster

    // Constructor untuk menambahkan film baru (ID akan di-generate oleh DB)
    public Film(String judul, String genre, int durasi, String sinopsis, String posterPath) {
        this.judul = judul;
        this.genre = genre;
        this.durasi = durasi;
        this.sinopsis = sinopsis;
        this.posterPath = posterPath;
    }

    // Constructor untuk mengambil film dari DB (sudah ada ID)
    public Film(int id, String judul, String genre, int durasi, String sinopsis, String posterPath) {
        this.id = id;
        this.judul = judul;
        this.genre = genre;
        this.durasi = durasi;
        this.sinopsis = sinopsis;
        this.posterPath = posterPath;
    }

    // Getters
    public int getId() { return id; }
    public String getJudul() { return judul; }
    public String getGenre() { return genre; }
    public int getDurasi() { return durasi; }
    public String getSinopsis() { return sinopsis; }
    public String getPosterPath() { return posterPath; }

    // Setters (jika diperlukan, untuk modifikasi objek sebelum update ke DB)
    public void setId(int id) { this.id = id; }
    public void setJudul(String judul) { this.judul = judul; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDurasi(int durasi) { this.durasi = durasi; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    @Override
    public String toString() {
        return "Film{" + "id=" + id + ", judul=" + judul + ", genre=" + genre + ", durasi=" + durasi + ", sinopsis=" + sinopsis + ", posterPath=" + posterPath + '}';
    }
}
