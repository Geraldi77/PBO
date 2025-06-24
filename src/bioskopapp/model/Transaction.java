/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.model;

import java.time.LocalDateTime;

/**
 * Model untuk merepresentasikan data transaksi.
 * Diperbarui dengan metode dan status pembayaran.
 */
public class Transaction {
    private int id;
    private int scheduleId;
    private String nomorKursi;
    private String namaPemesan;
    private LocalDateTime tanggalPesan;
    private double totalBayar;
    private String metodePembayaran; // BARU
    private String statusPembayaran; // BARU

    // Constructor untuk membuat transaksi BARU (belum ada di DB)
    public Transaction(int scheduleId, String nomorKursi, String namaPemesan, LocalDateTime tanggalPesan, double totalBayar) {
        this.scheduleId = scheduleId;
        this.nomorKursi = nomorKursi;
        this.namaPemesan = namaPemesan;
        this.tanggalPesan = tanggalPesan;
        this.totalBayar = totalBayar;
        this.statusPembayaran = "pending"; // Default status
    }

    // Constructor untuk mengambil transaksi dari DB (sudah ada ID dan detail pembayaran)
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

    // --- Getters ---
    public int getId() { return id; }
    public int getScheduleId() { return scheduleId; }
    public String getNomorKursi() { return nomorKursi; }
    public String getNamaPemesan() { return namaPemesan; }
    public LocalDateTime getTanggalPesan() { return tanggalPesan; }
    public double getTotalBayar() { return totalBayar; }
    public String getMetodePembayaran() { return metodePembayaran; }
    public String getStatusPembayaran() { return statusPembayaran; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }
}