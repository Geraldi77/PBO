
package bioskopapp.model;

public class Film {
    private int id;
    private String judul;
    private String genre;
    private int durasi; 
    private String sinopsis;
    private String posterPath;
    
    public Film(String judul, String genre, int durasi, String sinopsis, String posterPath) {
        this.judul = judul;
        this.genre = genre;
        this.durasi = durasi;
        this.sinopsis = sinopsis;
        this.posterPath = posterPath;
    }

    public Film(int id, String judul, String genre, int durasi, String sinopsis, String posterPath) {
        this.id = id;
        this.judul = judul;
        this.genre = genre;
        this.durasi = durasi;
        this.sinopsis = sinopsis;
        this.posterPath = posterPath;
    }

    public int getId() { return id; }
    public String getJudul() { return judul; }
    public String getGenre() { return genre; }
    public int getDurasi() { return durasi; }
    public String getSinopsis() { return sinopsis; }
    public String getPosterPath() { return posterPath; }

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
