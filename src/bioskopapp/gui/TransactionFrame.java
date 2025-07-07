package bioskopapp.gui;

import bioskopapp.dao.SeatDAO;
import bioskopapp.dao.TransactionDAO;
import bioskopapp.model.Film;
import bioskopapp.model.Schedule;
import bioskopapp.model.Seat;
import bioskopapp.model.Transaction;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class TransactionFrame extends JDialog {

    private SeatSelectionFrame parentFrame;
    private TransactionDAO transactionDAO;
    private SeatDAO seatDAO;

    private Schedule selectedSchedule;
    private List<Seat> selectedSeats;
    private String customerName;
    private double totalPrice;
    
    private String selectedPaymentMethod = "QRIS";

    public TransactionFrame(SeatSelectionFrame parent, Film film, Schedule schedule, List<Seat> seats, String customer, double price) {
        super(parent, "Proses Pembayaran", true);
        this.parentFrame = parent;
        this.selectedSchedule = schedule;
        this.selectedSeats = seats;
        this.customerName = customer;
        this.totalPrice = price;
        this.transactionDAO = new TransactionDAO();
        this.seatDAO = new SeatDAO();
        
        setupDialog();
        createUI();
    }

    private void setupDialog() {
        setSize(450, 500);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }

    private void createUI() {
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        methodPanel.setBorder(BorderFactory.createTitledBorder("Metode Pembayaran"));
        JRadioButton rbQris = new JRadioButton("QRIS", true);
        JRadioButton rbVA = new JRadioButton("Virtual Account");
        ButtonGroup group = new ButtonGroup();
        group.add(rbQris);
        group.add(rbVA);
        methodPanel.add(rbQris);
        methodPanel.add(rbVA);
        
        CardLayout paymentCardLayout = new CardLayout();
        JPanel paymentDetailsPanel = new JPanel(paymentCardLayout);

        JPanel qrisPanel = new JPanel(new BorderLayout());
        JLabel qrisLabel = new JLabel();
        try {
            URL qrisUrl = new URL("https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=https://github.com/rgaj-xyz");
            qrisLabel.setIcon(new ImageIcon(qrisUrl));
        } catch (Exception e) {
            qrisLabel.setText("QR Code tidak tersedia");
        }
        qrisLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qrisPanel.add(qrisLabel, BorderLayout.CENTER);
        qrisPanel.add(new JLabel("Silakan pindai QR Code di atas", SwingConstants.CENTER), BorderLayout.SOUTH);

        JPanel vaPanel = new JPanel(new GridLayout(3,1, 10, 10));
        vaPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        vaPanel.add(new JLabel("Nomor Virtual Account:", SwingConstants.CENTER));
        JLabel vaNumberLabel = new JLabel("8808 " + "1234 5678 9012", SwingConstants.CENTER);
        vaNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        vaPanel.add(vaNumberLabel);
        vaPanel.add(new JLabel("a.n. Bioskop RGAJ", SwingConstants.CENTER));

        paymentDetailsPanel.add(qrisPanel, "QRIS");
        paymentDetailsPanel.add(vaPanel, "VA");
        
        rbQris.addActionListener(e -> {
            paymentCardLayout.show(paymentDetailsPanel, "QRIS");
            selectedPaymentMethod = "QRIS";
        });
        rbVA.addActionListener(e -> {
            paymentCardLayout.show(paymentDetailsPanel, "VA");
            selectedPaymentMethod = "Virtual Account";
        });

        JButton confirmButton = new JButton("Saya Sudah Bayar");
        confirmButton.addActionListener(e -> completeTransaction());
        
        add(methodPanel, BorderLayout.NORTH);
        add(paymentDetailsPanel, BorderLayout.CENTER);
        add(confirmButton, BorderLayout.SOUTH);
    }
    
    private void completeTransaction() {
        try {
            StringBuilder seatNumbers = new StringBuilder();
            for (Seat seat : selectedSeats) {
                seatNumbers.append(seat.getNomorKursi()).append(", ");
            }
            seatNumbers.setLength(seatNumbers.length() - 2);
            
            Transaction transaction = new Transaction(
                selectedSchedule.getId(), 
                seatNumbers.toString(), 
                customerName, 
                LocalDateTime.now(), 
                totalPrice
            );
            transaction.setMetodePembayaran(selectedPaymentMethod);
            transaction.setStatusPembayaran("paid");
            
            transactionDAO.addTransaction(transaction);
            
            for (Seat seat : selectedSeats) {
                seat.setStatus("booked");
                seatDAO.updateSeatStatus(seat);
            }
            
            JOptionPane.showMessageDialog(this, "Pembayaran berhasil! Tiket Anda sudah diamankan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            parentFrame.transactionCompleted();
            parentFrame.setVisible(true);
            this.dispose();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TransactionFrame().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
