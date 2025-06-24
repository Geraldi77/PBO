/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.model;

public class Seat {
    private int id;
    private int scheduleId;
    private String nomorKursi;
    private String status; // "available", "booked"

    public Seat(int scheduleId, String nomorKursi, String status) {
        this.scheduleId = scheduleId;
        this.nomorKursi = nomorKursi;
        this.status = status;
    }

    public Seat(int id, int scheduleId, String nomorKursi, String status) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.nomorKursi = nomorKursi;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public int getScheduleId() { return scheduleId; }
    public String getNomorKursi() { return nomorKursi; }
    public String getStatus() { return status; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    public void setNomorKursi(String nomorKursi) { this.nomorKursi = nomorKursi; }
    public void setStatus(String status) { this.status = status; }
}