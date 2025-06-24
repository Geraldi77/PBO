/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskopapp.main;

import bioskopapp.gui.MainMenuFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Jalankan aplikasi di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenuFrame().setVisible(true);
            }
        });
    }
}