
package bioskopapp.model;

import java.time.LocalDateTime;


public class Transaction {
    private int id;
    private int scheduleId;
    private String nomorKursi;
    private String namaPemesan;
    private LocalDateTime tanggalPesan;
    private double totalBayar;
    private String metodePembayaran; 
    private String statusPembayaran; 

    public Transaction(int scheduleId, String nomorKursi, String namaPemesan, LocalDateTime tanggalPesan, double totalBayar) {
        this.scheduleId = scheduleId;
        this.nomorKursi = nomorKursi;
        this.namaPemesan = namaPemesan;
        this.tanggalPesan = tanggalPesan;
        this.totalBayar = totalBayar;
        this.statusPembayaran = "pending"; // Default status
    }

    public Transaction(int id, int scheduleId, String nomorKursi, String namaPemesan, LocalDateTime tanggalPesan, double totalBayar, String metodePembayaran, String statusPembayaran) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.nomorKursi = nomorKursi;
        this.namaPemesan = namaPemesan;
        this.tanggalPesan = tanggalPesan;
        this.totalBayar = totalBayar;
        this.metodePembayaran = metodePembayaran;
        this.statusPembayaran = statusPembayaran;
    }

    public int getId() { return id; }
    public int getScheduleId() { return scheduleId; }
    public String getNomorKursi() { return nomorKursi; }
    public String getNamaPemesan() { return namaPemesan; }
    public LocalDateTime getTanggalPesan() { return tanggalPesan; }
    public double getTotalBayar() { return totalBayar; }
    public String getMetodePembayaran() { return metodePembayaran; }
    public String getStatusPembayaran() { return statusPembayaran; }

    public void setId(int id) { this.id = id; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }
}