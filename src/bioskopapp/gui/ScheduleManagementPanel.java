/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package bioskopapp.gui;


import bioskopapp.dao.FilmDAO;
import bioskopapp.dao.ScheduleDAO;
import bioskopapp.dao.SeatDAO;
import bioskopapp.model.Film;
import bioskopapp.model.Schedule;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.Calendar;

/**
 * Panel untuk manajemen data jadwal tayang (CRUD).
 *
 * @author Gemini
 */
public class ScheduleManagementPanel extends javax.swing.JPanel {
    
    private ScheduleDAO scheduleDAO;
    private FilmDAO filmDAO;
    private SeatDAO seatDAO;

    private DefaultTableModel tableModel;
    private DefaultComboBoxModel<FilmItem> filmComboBoxModel;
    
    private static class FilmItem {
        private int id;
        private String judul;

        public FilmItem(int id, String judul) {
            this.id = id;
            this.judul = judul;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return judul;
        }
    }

    public ScheduleManagementPanel() {
        initComponents();
        scheduleDAO = new ScheduleDAO();
        filmDAO = new FilmDAO();
        seatDAO = new SeatDAO();
        
        applyCustomStyles();
        setupTable();
        setupComboBox();
        loadComboBoxData();
        loadTableData();
        setupTableSelectionListener();
    }
    
    private void applyCustomStyles() {
        lblMainTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
                null, "Form Data Jadwal", 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                new Font("Segoe UI", Font.BOLD, 14)));
        
        JTableHeader header = tblSchedules.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(240, 240, 240));
        tblSchedules.setRowHeight(28);
    }
    
    private void setupTable() {
        tableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID Jadwal", "Judul Film", "Tanggal Tayang", "Waktu Mulai", "Harga Tiket", "Studio", "ID Film"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSchedules.setModel(tableModel);

        tblSchedules.getColumnModel().getColumn(0).setMinWidth(0);
        tblSchedules.getColumnModel().getColumn(0).setMaxWidth(0);
        tblSchedules.getColumnModel().getColumn(0).setWidth(0);
        
        tblSchedules.getColumnModel().getColumn(6).setMinWidth(0);
        tblSchedules.getColumnModel().getColumn(6).setMaxWidth(0);
        tblSchedules.getColumnModel().getColumn(6).setWidth(0);
        
        tblSchedules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setupComboBox() {
        filmComboBoxModel = new DefaultComboBoxModel<>();
        cmbFilm.setModel(filmComboBoxModel);
    }
    
    public void loadComboBoxData() {
        filmComboBoxModel.removeAllElements();
        try {
            List<Film> films = filmDAO.getAllFilms();
            for (Film film : films) {
                filmComboBoxModel.addElement(new FilmItem(film.getId(), film.getJudul()));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data film: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadTableData() {
        tableModel.setRowCount(0);
        try {
            List<Schedule> schedules = scheduleDAO.getAllSchedules();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            for (Schedule schedule : schedules) {
                tableModel.addRow(new Object[]{
                    schedule.getId(),
                    schedule.getFilmJudul(),
                    schedule.getTanggalTayang(),
                    timeFormat.format(java.sql.Time.valueOf(schedule.getWaktuMulai())),
                    String.format("Rp %,.0f", schedule.getHargaTiket()),
                    schedule.getStudio(),
                    schedule.getFilmId()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data jadwal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void setupTableSelectionListener() {
        tblSchedules.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting() && tblSchedules.getSelectedRow() != -1) {
                int selectedRow = tblSchedules.convertRowIndexToModel(tblSchedules.getSelectedRow());
                
                LocalDate tanggal = (LocalDate) tableModel.getValueAt(selectedRow, 2);
                String waktuStr = tableModel.getValueAt(selectedRow, 3).toString();
                String hargaStr = tableModel.getValueAt(selectedRow, 4).toString().replaceAll("[^\\d]", "");
                String studio = tableModel.getValueAt(selectedRow, 5).toString();
                int filmId = (int) tableModel.getValueAt(selectedRow, 6);
                
                for (int i = 0; i < filmComboBoxModel.getSize(); i++) {
                    if (filmComboBoxModel.getElementAt(i).getId() == filmId) {
                        cmbFilm.setSelectedIndex(i);
                        break;
                    }
                }
                
                dateChooser.setDate(Date.from(tanggal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                
                try {
                    Date time = new SimpleDateFormat("HH:mm").parse(waktuStr);
                    spinnerWaktu.setValue(time);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                txtHarga.setText(hargaStr);
                txtStudio.setText(studio);
            }
        });
    }
    
    private void clearForm() {
        tblSchedules.clearSelection();
        cmbFilm.setSelectedIndex(-1);
        dateChooser.setDate(null);
        txtHarga.setText("");
        txtStudio.setText("");
        spinnerWaktu.setValue(new Date());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblMainTitle = new javax.swing.JLabel();
        formPanel = new javax.swing.JPanel();
        cmbFilm = new javax.swing.JComboBox();
        dateChooser = new com.toedter.calendar.JDateChooser();
        spinnerWaktu = new javax.swing.JSpinner();
        txtHarga = new javax.swing.JTextField();
        txtStudio = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSchedules = new javax.swing.JTable();
        btnTambah = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();

        lblMainTitle.setText("Manajemen Data Jadwal");

        formPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Data Jadwal"));

        cmbFilm.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFilm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFilmActionPerformed(evt);
            }
        });

        spinnerWaktu.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.HOUR));

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbFilm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(206, 206, 206)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHarga)
                    .addComponent(txtStudio)
                    .addComponent(spinnerWaktu))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formPanelLayout.setVerticalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbFilm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStudio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        tblSchedules.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblSchedules);

        jScrollPane2.setViewportView(jScrollPane1);

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(222, 222, 222)
                                .addComponent(lblMainTitle))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnTambah)
                                .addGap(18, 18, 18)
                                .addComponent(btnUpdate)
                                .addGap(18, 18, 18)
                                .addComponent(btnHapus)
                                .addGap(27, 27, 27)
                                .addComponent(btnClear)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblMainTitle)
                .addGap(30, 30, 30)
                .addComponent(formPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah)
                    .addComponent(btnUpdate)
                    .addComponent(btnHapus)
                    .addComponent(btnClear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
       FilmItem selectedFilm = (FilmItem) cmbFilm.getSelectedItem();
        if (selectedFilm == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih film!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih tanggal tayang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String hargaStr = txtHarga.getText().trim();
        if (hargaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harga tiket harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String studio = txtStudio.getText().trim();
        if (studio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Studio harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int filmId = selectedFilm.getId();
            LocalDate tanggal = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            Date time = (Date) spinnerWaktu.getValue();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            LocalTime waktu = LocalTime.parse(timeFormat.format(time));
            
            double harga = Double.parseDouble(hargaStr);

            Schedule schedule = new Schedule(filmId, tanggal, waktu, harga, studio);
            
            scheduleDAO.addSchedule(schedule);
            
            seatDAO.addSeats(schedule.getId(), 5, 10);
            
            JOptionPane.showMessageDialog(this, "Jadwal berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            clearForm();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka yang valid!", "Error Input", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan jadwal: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int selectedRow = tblSchedules.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih jadwal yang akan diupdate.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int modelRow = tblSchedules.convertRowIndexToModel(selectedRow);
            int scheduleId = (int) tableModel.getValueAt(modelRow, 0);
            
            FilmItem selectedFilm = (FilmItem) cmbFilm.getSelectedItem();
            int filmId = selectedFilm.getId();
            LocalDate tanggal = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            Date time = (Date) spinnerWaktu.getValue();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            LocalTime waktu = LocalTime.parse(timeFormat.format(time));
            
            double harga = Double.parseDouble(txtHarga.getText().trim());
            String studio = txtStudio.getText().trim();
            
            Schedule schedule = new Schedule(scheduleId, filmId, tanggal, waktu, harga, studio);
            scheduleDAO.updateSchedule(schedule);
            
            JOptionPane.showMessageDialog(this, "Jadwal berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            clearForm();

        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Gagal mengupdate jadwal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        int selectedRow = tblSchedules.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih jadwal yang akan dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus jadwal ini?\nSemua data kursi terkait akan ikut terhapus.", 
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int modelRow = tblSchedules.convertRowIndexToModel(selectedRow);
                int scheduleId = (int) tableModel.getValueAt(modelRow, 0);
                
                seatDAO.deleteSeatsByScheduleId(scheduleId);
                scheduleDAO.deleteSchedule(scheduleId);
                
                JOptionPane.showMessageDialog(this, "Jadwal berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus jadwal: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void cmbFilmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFilmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFilmActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox cmbFilm;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JPanel formPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblMainTitle;
    private javax.swing.JSpinner spinnerWaktu;
    private javax.swing.JTable tblSchedules;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtStudio;
    // End of variables declaration//GEN-END:variables
}
