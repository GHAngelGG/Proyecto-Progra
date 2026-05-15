/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package login.proyecto;

/**
 *
 * @author josea
 */
public class LoginProyecto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Apply FlatLaf modern look and feel
        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            try {
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {}
        }
        java.awt.EventQueue.invokeLater(() -> new Inicio().setVisible(true));
    }
    
}
