
package bioskopapp.gui;


import bioskopapp.dao.FilmDAO;
import bioskopapp.model.Film;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class FilmManagementPanel extends javax.swing.JPanel {

    private FilmDAO filmDAO;
    private DefaultTableModel tableModel;
    private String posterPath; 

    public FilmManagementPanel() {
        initComponents();
        filmDAO = new FilmDAO();
        applyCustomStyles();
        setupTable();
        loadTableData();
        setupTableSelectionListener();
    }
    
    private void applyCustomStyles() {
        lblMainTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                " Form Data Film ",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(51, 51, 51)
        ));
        
        JTableHeader header = tblFilms.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(240, 240, 240));
        tblFilms.setRowHeight(28);

        btnTambah.setText("Tambah");
        btnUpdate.setText("Update");
        btnHapus.setText("Hapus");
        btnClear.setText("Clear");
        btnPilihPoster.setText("Pilih Poster...");
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Judul", "Genre", "Durasi (min)", "Poster Path"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblFilms.setModel(tableModel);
        
        tblFilms.getColumnModel().getColumn(0).setMinWidth(0);
        tblFilms.getColumnModel().getColumn(0).setMaxWidth(0);
        tblFilms.getColumnModel().getColumn(0).setWidth(0);

        tblFilms.getColumnModel().getColumn(4).setMinWidth(0);
        tblFilms.getColumnModel().getColumn(4).setMaxWidth(0);
        tblFilms.getColumnModel().getColumn(4).setWidth(0);
        
        tblFilms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            List<Film> films = filmDAO.getAllFilms();
            for (Film film : films) {
                tableModel.addRow(new Object[]{
                    film.getId(),
                    film.getJudul(),
                    film.getGenre(),
                    film.getDurasi(),
                    film.getPosterPath()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data film: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void setupTableSelectionListener() {
        tblFilms.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting() && tblFilms.getSelectedRow() != -1) {
                int modelRow = tblFilms.convertRowIndexToModel(tblFilms.getSelectedRow());
                int filmId = (int) tableModel.getValueAt(modelRow, 0);

                try {
                     Film selectedFilm = filmDAO.getAllFilms().stream()
                                              .filter(f -> f.getId() == filmId)
                                              .findFirst().orElse(null);
                     if (selectedFilm != null) {
                        txtJudul.setText(selectedFilm.getJudul());
                        txtGenre.setText(selectedFilm.getGenre());
                        txtDurasi.setText(String.valueOf(selectedFilm.getDurasi()));
                        txtSinopsis.setText(selectedFilm.getSinopsis());
                        txtPosterPath.setText(selectedFilm.getPosterPath());
                        posterPath = selectedFilm.getPosterPath();
                        displayImage(posterPath);
                     }
                } catch (Exception ex) {
                     JOptionPane.showMessageDialog(this, "Gagal mengambil detail film: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void clearForm() {
        tblFilms.clearSelection();
        txtJudul.setText("");
        txtGenre.setText("");
        txtDurasi.setText("");
        txtSinopsis.setText("");
        txtPosterPath.setText("");
        posterPath = null;
        lblPosterPreview.setIcon(null);
        lblPosterPreview.setText("Pratinjau Poster");
    }

    private void displayImage(String path) {
        if (path != null && !path.trim().isEmpty() && new File(path).exists()) {
            try {
                ImageIcon icon = new ImageIcon(path);
                Image image = icon.getImage().getScaledInstance(lblPosterPreview.getWidth(), lblPosterPreview.getHeight(), Image.SCALE_SMOOTH);
                lblPosterPreview.setIcon(new ImageIcon(image));
                lblPosterPreview.setText("");
            } catch (Exception e) {
                 lblPosterPreview.setIcon(null);
                 lblPosterPreview.setText("Gagal Memuat");
            }
        } else {
            lblPosterPreview.setIcon(null);
            lblPosterPreview.setText("Poster Tidak Ada");
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

        lblMainTitle = new javax.swing.JLabel();
        formPanel = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        lblGenre = new javax.swing.JLabel();
        lblSinopsis = new javax.swing.JLabel();
        lblDurasi = new javax.swing.JLabel();
        txtJudul = new javax.swing.JTextField();
        txtGenre = new javax.swing.JTextField();
        txtDurasi = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSinopsis = new javax.swing.JTextArea();
        btnTambah = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        posterPreviewPanel = new javax.swing.JPanel();
        lblPosterPreview = new javax.swing.JLabel();
        btnPilihPoster = new javax.swing.JButton();
        txtPosterPath = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFilms = new javax.swing.JTable();
        lblPosterPath = new javax.swing.JLabel();

        lblMainTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblMainTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMainTitle.setText("Manajemen Data Film");

        lblJudul.setText("Judul");

        lblGenre.setText("Genre");

        lblSinopsis.setText("Sinopsis");

        lblDurasi.setText("Durasi");

        txtSinopsis.setColumns(20);
        txtSinopsis.setRows(5);
        jScrollPane2.setViewportView(txtSinopsis);

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addComponent(lblDurasi, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDurasi, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblJudul)
                            .addComponent(lblGenre))
                        .addGap(30, 30, 30)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtGenre, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                            .addComponent(txtJudul)))
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addComponent(lblSinopsis)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formPanelLayout.setVerticalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJudul)
                    .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGenre)
                    .addComponent(txtGenre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDurasi)
                    .addComponent(txtDurasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSinopsis)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        posterPreviewPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout posterPreviewPanelLayout = new javax.swing.GroupLayout(posterPreviewPanel);
        posterPreviewPanel.setLayout(posterPreviewPanelLayout);
        posterPreviewPanelLayout.setHorizontalGroup(
            posterPreviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPosterPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
        );
        posterPreviewPanelLayout.setVerticalGroup(
            posterPreviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPosterPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btnPilihPoster.setText("Pilih Poster");
        btnPilihPoster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihPosterActionPerformed(evt);
            }
        });

        tblFilms.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblFilms);

        jScrollPane3.setViewportView(jScrollPane1);

        lblPosterPath.setText("Path Poster :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(297, 297, 297)
                        .addComponent(lblMainTitle)
                        .addGap(283, 283, 283))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(formPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(73, 73, 73)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(posterPreviewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPosterPath, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(35, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus)
                        .addGap(18, 18, 18)
                        .addComponent(btnClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPosterPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(btnPilihPoster)))
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(posterPreviewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblMainTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(formPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnClear)
                            .addComponent(btnHapus)
                            .addComponent(btnUpdate)
                            .addComponent(btnTambah)
                            .addComponent(lblPosterPath)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPilihPoster)
                            .addComponent(txtPosterPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        String judul = txtJudul.getText().trim();
        String genre = txtGenre.getText().trim();
        String durasiStr = txtDurasi.getText().trim();
        String sinopsis = txtSinopsis.getText().trim();

        if (judul.isEmpty() || genre.isEmpty() || durasiStr.isEmpty() || sinopsis.isEmpty() || posterPath == null || posterPath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi dan poster harus dipilih!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int durasi = Integer.parseInt(durasiStr);
            Film film = new Film(judul, genre, durasi, sinopsis, posterPath);
            filmDAO.addFilm(film);
            JOptionPane.showMessageDialog(this, "Film berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            clearForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Durasi harus berupa angka yang valid!", "Error Input", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan film ke database: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int selectedRow = tblFilms.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih film yang akan diupdate dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int modelRow = tblFilms.convertRowIndexToModel(selectedRow);
            int filmId = (int) tableModel.getValueAt(modelRow, 0);
            String judul = txtJudul.getText().trim();
            String genre = txtGenre.getText().trim();
            int durasi = Integer.parseInt(txtDurasi.getText().trim());
            String sinopsis = txtSinopsis.getText().trim();
            
            if (judul.isEmpty() || genre.isEmpty() || sinopsis.isEmpty() || posterPath == null || posterPath.trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Semua field harus diisi dan poster harus dipilih!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                 return;
            }
            
            Film film = new Film(filmId, judul, genre, durasi, sinopsis, posterPath);
            filmDAO.updateFilm(film);
            JOptionPane.showMessageDialog(this, "Film berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            clearForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Durasi harus berupa angka yang valid!", "Error Input", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate film: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
         int selectedRow = tblFilms.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih film yang akan dihapus dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus film ini?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
                
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int modelRow = tblFilms.convertRowIndexToModel(selectedRow);
                int filmId = (int) tableModel.getValueAt(modelRow, 0);
                filmDAO.deleteFilm(filmId);
                JOptionPane.showMessageDialog(this, "Film berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus film. Pastikan tidak ada jadwal terkait.\nError: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();

    }//GEN-LAST:event_btnClearActionPerformed

    private void btnPilihPosterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihPosterActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih Gambar Poster");
        fileChooser.setFileFilter(new FileNameExtensionFilter("File Gambar (.jpg, .png, .gif)", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            posterPath = selectedFile.getAbsolutePath();
            txtPosterPath.setText(posterPath);
            displayImage(posterPath);
    }//GEN-LAST:event_btnPilihPosterActionPerformed
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnPilihPoster;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel formPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblDurasi;
    private javax.swing.JLabel lblGenre;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblMainTitle;
    private javax.swing.JLabel lblPosterPath;
    private javax.swing.JLabel lblPosterPreview;
    private javax.swing.JLabel lblSinopsis;
    private javax.swing.JPanel posterPreviewPanel;
    private javax.swing.JTable tblFilms;
    private javax.swing.JTextField txtDurasi;
    private javax.swing.JTextField txtGenre;
    private javax.swing.JTextField txtJudul;
    private javax.swing.JTextField txtPosterPath;
    private javax.swing.JTextArea txtSinopsis;
    // End of variables declaration//GEN-END:variables
}
