/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bioskopapp.gui;

import bioskopapp.dao.FilmDAO;
import bioskopapp.dao.ScheduleDAO;
import bioskopapp.dao.SeatDAO;
import bioskopapp.dao.TransactionDAO;
import bioskopapp.model.Film;
import bioskopapp.model.Schedule;
import bioskopapp.model.Seat;
import bioskopapp.model.Transaction;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class UserBookingFrame extends javax.swing.JFrame {

    private FilmDAO filmDAO;
    private ScheduleDAO scheduleDAO;
    private SeatDAO seatDAO;
    private TransactionDAO transactionDAO;
    private JPanel mainCardPanel;
    private CardLayout cardLayout;
    private JPanel filmSelectionPanel;
    private JPanel seatSelectionPanel;
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

    public UserBookingFrame() {
        filmDAO = new FilmDAO();
        scheduleDAO = new ScheduleDAO();
        seatDAO = new SeatDAO();
        transactionDAO = new TransactionDAO();
        setupFrame();
        createUI();
        loadFilmSelectionView();
    }

    private void setupFrame() {
        setTitle("Pemesanan Tiket Bioskop");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                goBackToMainMenu();
            }
        });
    }
    
    private void goBackToMainMenu() {
        new MainMenuFrame().setVisible(true);
        this.dispose();
    }
    
    private void createUI() {
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        createFilmSelectionPanel();
        createSeatSelectionPanel();
        mainCardPanel.add(new JScrollPane(filmSelectionPanel), "FILMS");
        mainCardPanel.add(seatSelectionPanel, "SEATS");
        add(mainCardPanel);
    }

    private void createFilmSelectionPanel() {
        filmSelectionPanel = new JPanel();
        filmSelectionPanel.setLayout(new BoxLayout(filmSelectionPanel, BoxLayout.Y_AXIS));
        filmSelectionPanel.setBackground(Color.WHITE);
        filmSelectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Sedang Tayang");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        filmSelectionPanel.add(titleLabel);
        filmSelectionPanel.add(new JLabel(" "));
    }
    
    private void loadFilmSelectionView() {
        JPanel filmGrid = new JPanel(new GridLayout(0, 4, 20, 20));
        filmGrid.setBackground(Color.WHITE);
        try {
            List<Film> films = filmDAO.getAllFilms();
            if (films.isEmpty()) {
                filmGrid.add(new JLabel("Tidak ada film yang sedang tayang."));
            } else {
                for (Film film : films) {
                    filmGrid.add(createFilmCard(film));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data film.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (filmSelectionPanel.getComponentCount() > 2) {
             filmSelectionPanel.remove(2);
        }
        filmSelectionPanel.add(filmGrid);
        cardLayout.show(mainCardPanel, "FILMS");
        mainCardPanel.revalidate();
        mainCardPanel.repaint();
    }

    private JPanel createFilmCard(Film film) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(180, 250));
        if (film.getPosterPath() != null && new File(film.getPosterPath()).exists()) {
            ImageIcon icon = new ImageIcon(film.getPosterPath());
            Image image = icon.getImage().getScaledInstance(180, 250, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(image));
        } else {
            posterLabel.setText("No Poster");
        }
        JLabel titleLabel = new JLabel("<html><center>" + film.getJudul() + "</center></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(posterLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedFilm = film;
                loadSeatSelectionView();
                cardLayout.show(mainCardPanel, "SEATS");
            }
        });
        return card;
    }

    private void createSeatSelectionPanel() {
        seatSelectionPanel = new JPanel(new BorderLayout(20, 20));
        seatSelectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
        JPanel bottomPanel = new JPanel(new BorderLayout(10,10));
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
        JButton bookButton = new JButton("Pesan Tiket");
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookButton.setBackground(new Color(40, 167, 69));
        bookButton.setForeground(Color.WHITE);
        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);
        bottomPanel.add(confirmationPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        seatSelectionPanel.add(topPanel, BorderLayout.NORTH);
        seatSelectionPanel.add(new JScrollPane(seatGridPanel), BorderLayout.CENTER);
        seatSelectionPanel.add(bottomPanel, BorderLayout.SOUTH);
        cmbSchedules.addActionListener(e -> {
            ScheduleItem item = (ScheduleItem) cmbSchedules.getSelectedItem();
            if (item != null && item.getSchedule() != null) {
                selectedSchedule = item.getSchedule();
                ticketPrice = selectedSchedule.getHargaTiket();
                loadSeatLayout();
            }
        });
        backButton.addActionListener(e -> {
             loadFilmSelectionView();
             cardLayout.show(mainCardPanel, "FILMS");
        });
        bookButton.addActionListener(e -> processBooking());
    }
    
    private void loadSeatSelectionView() {
        if (selectedFilm == null) return;
        lblFilmTitle.setText(selectedFilm.getJudul());
        lblFilmDetails.setText(selectedFilm.getGenre() + " | " + selectedFilm.getDurasi() + " menit");
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
            int rows = 5;
            int cols = 10;
            seatGridPanel.setLayout(new GridLayout(rows, cols, 5, 5));
            for (Seat seat : seats) {
                SeatButton seatButton = new SeatButton(seat);
                if (seat.getStatus().equals("booked")) {
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
    
    private void processBooking() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan pilih minimal satu kursi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String customerName = txtCustomerName.getText().trim();
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan nama pemesan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String confirmationMessage = "Anda akan memesan tiket:\n\n" +
                                     "Film: " + selectedFilm.getJudul() + "\n" +
                                     "Jadwal: " + cmbSchedules.getSelectedItem().toString() + "\n" +
                                     lblSelectedSeats.getText() + "\n" +
                                     lblTotalPrice.getText() + "\n\n" +
                                     "Lanjutkan ke pembayaran?";
        int choice = JOptionPane.showConfirmDialog(this, confirmationMessage, "Konfirmasi Pemesanan", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Tampilkan dialog pembayaran
            PaymentDialog paymentDialog = new PaymentDialog(this);
            paymentDialog.setVisible(true);
        }
    }
    
    // Kelas internal untuk dialog pembayaran
    class PaymentDialog extends JDialog {
        private String selectedPaymentMethod = "QRIS";

        public PaymentDialog(JFrame parent) {
            super(parent, "Pilih Metode Pembayaran", true);
            setSize(450, 500);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));

            // Panel Pilihan Metode
            JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            methodPanel.setBorder(BorderFactory.createTitledBorder("Metode Pembayaran"));
            JRadioButton rbQris = new JRadioButton("QRIS", true);
            JRadioButton rbVA = new JRadioButton("Virtual Account");
            ButtonGroup group = new ButtonGroup();
            group.add(rbQris);
            group.add(rbVA);
            methodPanel.add(rbQris);
            methodPanel.add(rbVA);
            
            // Panel Detail Pembayaran (menggunakan CardLayout)
            CardLayout paymentCardLayout = new CardLayout();
            JPanel paymentDetailsPanel = new JPanel(paymentCardLayout);

            // Detail QRIS
            JPanel qrisPanel = new JPanel(new BorderLayout());
            JLabel qrisLabel = new JLabel();
            try {
                // Menggunakan placeholder untuk gambar QR Code
                URL qrisUrl = new URL("https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=https://github.com/rgaj-xyz");
                qrisLabel.setIcon(new ImageIcon(qrisUrl));
            } catch (Exception e) {
                qrisLabel.setText("QR Code tidak tersedia");
            }
            qrisLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qrisPanel.add(qrisLabel, BorderLayout.CENTER);
            qrisPanel.add(new JLabel("Silakan pindai QR Code di atas", SwingConstants.CENTER), BorderLayout.SOUTH);

            // Detail Virtual Account
            JPanel vaPanel = new JPanel(new GridLayout(3,1, 10, 10));
            vaPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
            vaPanel.add(new JLabel("Nomor Virtual Account:", SwingConstants.CENTER));
            JLabel vaNumberLabel = new JLabel("8808 " + "1234 5678 9012", SwingConstants.CENTER);
            vaNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            vaPanel.add(vaNumberLabel);
            vaPanel.add(new JLabel("a.n. Bioskop RGAJ", SwingConstants.CENTER));

            paymentDetailsPanel.add(qrisPanel, "QRIS");
            paymentDetailsPanel.add(vaPanel, "VA");
            
            // Action Listener untuk radio button
            rbQris.addActionListener(e -> {
                paymentCardLayout.show(paymentDetailsPanel, "QRIS");
                selectedPaymentMethod = "QRIS";
            });
            rbVA.addActionListener(e -> {
                paymentCardLayout.show(paymentDetailsPanel, "VA");
                selectedPaymentMethod = "Virtual Account";
            });

            // Tombol Konfirmasi Pembayaran
            JButton confirmButton = new JButton("Saya Sudah Bayar");
            confirmButton.addActionListener(e -> completeTransaction());
            
            add(methodPanel, BorderLayout.NORTH);
            add(paymentDetailsPanel, BorderLayout.CENTER);
            add(confirmButton, BorderLayout.SOUTH);
        }

        private void completeTransaction() {
            try {
                String seatNumbersStr = lblSelectedSeats.getText().replace("Kursi Dipilih: ", "");
                double totalPrice = selectedSeats.size() * ticketPrice;
                Transaction transaction = new Transaction(selectedSchedule.getId(), seatNumbersStr, txtCustomerName.getText(), LocalDateTime.now(), totalPrice);
                transaction.setMetodePembayaran(selectedPaymentMethod);
                transaction.setStatusPembayaran("paid");
                transactionDAO.addTransaction(transaction);
                for (Seat seat : selectedSeats) {
                    seat.setStatus("booked");
                    seatDAO.updateSeatStatus(seat);
                }
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil! Tiket Anda sudah diamankan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Tutup dialog pembayaran
                loadSeatLayout(); // Refresh denah kursi
                txtCustomerName.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
            .addGap(0, 778, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 414, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserBookingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserBookingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserBookingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserBookingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserBookingFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
