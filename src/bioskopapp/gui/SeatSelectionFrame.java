package bioskopapp.gui;

import bioskopapp.dao.ScheduleDAO;
import bioskopapp.dao.SeatDAO;
import bioskopapp.model.Film;
import bioskopapp.model.Schedule;
import bioskopapp.model.Seat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class SeatSelectionFrame extends JFrame {

    private ScheduleDAO scheduleDAO;
    private SeatDAO seatDAO;

    private Film selectedFilm;
    private Schedule selectedSchedule;
    private List<Seat> selectedSeats = new ArrayList<>();
    private double ticketPrice = 0;

    private JLabel lblFilmTitle;
    private JLabel lblFilmDetails;
    private JComboBox<ScheduleItem> cmbSchedules;
    private JPanel seatGridPanel;
    private JLabel lblSelectedSeats;
    private JLabel lblTotalPrice;
    private JTextField txtCustomerName;

    public SeatSelectionFrame(Film film) {
        this.selectedFilm = film;
        this.scheduleDAO = new ScheduleDAO();
        this.seatDAO = new SeatDAO();

        setupFrame();
        createUI();
        loadFilmDetails();
        loadSchedules();
    }

    private void setupFrame() {
        setTitle("Pilih Kursi & Jadwal - " + selectedFilm.getJudul());
        setSize(800, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close to go back
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                goBackToFilmSelection();
            }
        });
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        lblFilmTitle = new JLabel("Judul Film");
        lblFilmTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblFilmDetails = new JLabel("Genre | Durasi");
        cmbSchedules = new JComboBox<>();
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(lblFilmTitle);
        titlePanel.add(lblFilmDetails);

        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(cmbSchedules, BorderLayout.EAST);

        seatGridPanel = new JPanel();
        seatGridPanel.setBorder(BorderFactory.createTitledBorder("Pilih Kursi"));


        
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        JPanel confirmationPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        confirmationPanel.setBorder(BorderFactory.createTitledBorder("Detail Pemesanan"));
        
        lblSelectedSeats = new JLabel("Kursi Dipilih: -");
        lblTotalPrice = new JLabel("Total Harga: Rp 0");
        JLabel lblCustomerName = new JLabel("Nama Pemesan:");
        txtCustomerName = new JTextField();

        confirmationPanel.add(lblSelectedSeats);
        confirmationPanel.add(lblTotalPrice);
        confirmationPanel.add(lblCustomerName);
        confirmationPanel.add(txtCustomerName);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Kembali ke Daftar Film");
        JButton bookButton = new JButton("Lanjutkan ke Pembayaran");
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookButton.setBackground(new Color(40, 167, 69));
        bookButton.setForeground(Color.WHITE);

        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);

        bottomPanel.add(confirmationPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(seatGridPanel), BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        
        cmbSchedules.addActionListener(e -> {
            ScheduleItem item = (ScheduleItem) cmbSchedules.getSelectedItem();
            if (item != null && item.getSchedule() != null) {
                selectedSchedule = item.getSchedule();
                ticketPrice = selectedSchedule.getHargaTiket();
                loadSeatLayout();
            }
        });

        backButton.addActionListener(e -> goBackToFilmSelection());
        bookButton.addActionListener(e -> processToPayment());
    }

    private void loadFilmDetails() {
        if (selectedFilm == null) return;
        lblFilmTitle.setText(selectedFilm.getJudul());
        lblFilmDetails.setText(selectedFilm.getGenre() + " | " + selectedFilm.getDurasi() + " menit");
    }

    private void loadSchedules() {
        try {
            List<Schedule> schedules = scheduleDAO.getSchedulesByFilmId(selectedFilm.getId());
            cmbSchedules.removeAllItems();
            if (schedules.isEmpty()) {
                cmbSchedules.addItem(new ScheduleItem(null, "Tidak ada jadwal tersedia"));
                seatGridPanel.removeAll();
                seatGridPanel.revalidate();
                seatGridPanel.repaint();
            } else {
                for (Schedule schedule : schedules) {
                    cmbSchedules.addItem(new ScheduleItem(schedule, ""));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat jadwal.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSeatLayout() {
        if (selectedSchedule == null) return;
        seatGridPanel.removeAll();
        selectedSeats.clear();
        updateBookingDetails();

        try {
            List<Seat> seats = seatDAO.getSeatsByScheduleId(selectedSchedule.getId());
            int rows = 5; // Asumsi denah 5x10
            int cols = 10;
            seatGridPanel.setLayout(new GridLayout(rows, cols, 5, 5));

            for (Seat seat : seats) {
                SeatButton seatButton = new SeatButton(seat);
                if ("booked".equals(seat.getStatus())) {
                    seatButton.setBackground(Color.RED);
                    seatButton.setEnabled(false);
                } else {
                    seatButton.setBackground(Color.GREEN);
                }

                seatButton.addActionListener(e -> {
                    SeatButton source = (SeatButton) e.getSource();
                    if (source.isSelected()) {
                        selectedSeats.add(source.getSeat());
                    } else {
                        selectedSeats.remove(source.getSeat());
                    }
                    updateBookingDetails();
                });
                seatGridPanel.add(seatButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat layout kursi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        seatGridPanel.revalidate();
        seatGridPanel.repaint();
    }

    private void updateBookingDetails() {
        if (selectedSeats.isEmpty()) {
            lblSelectedSeats.setText("Kursi Dipilih: -");
            lblTotalPrice.setText("Total Harga: Rp 0");
        } else {
            StringBuilder seatNumbers = new StringBuilder();
            for (Seat seat : selectedSeats) {
                seatNumbers.append(seat.getNomorKursi()).append(", ");
            }
            seatNumbers.setLength(seatNumbers.length() - 2);
            lblSelectedSeats.setText("Kursi Dipilih: " + seatNumbers.toString());
            double totalPrice = selectedSeats.size() * ticketPrice;
            DecimalFormat df = new DecimalFormat("#,##0");
            lblTotalPrice.setText("Total Harga: Rp " + df.format(totalPrice));
        }
    }

    private void processToPayment() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan pilih minimal satu kursi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String customerName = txtCustomerName.getText().trim();
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan nama pemesan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double totalPrice = selectedSeats.size() * ticketPrice;

        
        TransactionFrame transactionFrame = new TransactionFrame(this, selectedFilm, selectedSchedule, selectedSeats, customerName, totalPrice);
        transactionFrame.setVisible(true);
        this.setVisible(false); 
    }
    
    
    public void transactionCompleted() {
        loadSeatLayout(); 
        txtCustomerName.setText("");
        selectedSeats.clear();
        updateBookingDetails();
    }
    
    private void goBackToFilmSelection() {
        new UserBookingFrame().setVisible(true);
        this.dispose();
    }
    
    
    private class ScheduleItem {
        private Schedule schedule;
        private String displayText;

        public ScheduleItem(Schedule schedule, String defaultText) {
            this.schedule = schedule;
            if (schedule != null) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM uuuu");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                DecimalFormat currencyFormatter = new DecimalFormat("'Rp '###,##0");
                this.displayText = schedule.getTanggalTayang().format(dateFormatter) + " - " +
                                   schedule.getWaktuMulai().format(timeFormatter) + " - " +
                                   currencyFormatter.format(schedule.getHargaTiket());
            } else {
                this.displayText = defaultText;
            }
        }
        public Schedule getSchedule() { return schedule; }
        @Override
        public String toString() { return displayText; }
    }

    
    private class SeatButton extends JToggleButton {
        private Seat seat;
        public SeatButton(Seat seat) {
            super(seat.getNomorKursi());
            this.seat = seat;
            setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
        public Seat getSeat() { return seat; }
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
//            java.util.logging.Logger.getLogger(SeatSelectionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(SeatSelectionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(SeatSelectionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(SeatSelectionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new SeatSelectionFrame().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
