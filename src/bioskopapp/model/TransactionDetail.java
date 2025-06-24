/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.model;

import java.time.LocalDateTime;

/**
 * Kelas ini adalah model untuk menampung data gabungan dari beberapa tabel
 * untuk keperluan laporan penjualan.
 */
public class TransactionDetail {
    private int transactionId;
    private String filmTitle;
    private String scheduleInfo; // Contoh: "10 Jun 2025, 19:00"
    private String seatNumbers;
    private String customerName;
    private double totalPrice;
    private LocalDateTime transactionTime;

    public TransactionDetail(int transactionId, String filmTitle, String scheduleInfo, String seatNumbers, String customerName, double totalPrice, LocalDateTime transactionTime) {
        this.transactionId = transactionId;
        this.filmTitle = filmTitle;
        this.scheduleInfo = scheduleInfo;
        this.seatNumbers = seatNumbers;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.transactionTime = transactionTime;
    }

    // Getters
    public int getTransactionId() { return transactionId; }
    public String getFilmTitle() { return filmTitle; }
    public String getScheduleInfo() { return scheduleInfo; }
    public String getSeatNumbers() { return seatNumbers; }
    public String getCustomerName() { return customerName; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDateTime getTransactionTime() { return transactionTime; }
}
