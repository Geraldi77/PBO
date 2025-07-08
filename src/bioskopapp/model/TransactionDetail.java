
package bioskopapp.model;

import java.time.LocalDateTime;


public class TransactionDetail {
    private int transactionId;
    private String filmTitle;
    private String scheduleInfo; 
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

    
    public int getTransactionId() { return transactionId; }
    public String getFilmTitle() { return filmTitle; }
    public String getScheduleInfo() { return scheduleInfo; }
    public String getSeatNumbers() { return seatNumbers; }
    public String getCustomerName() { return customerName; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDateTime getTransactionTime() { return transactionTime; }
}
