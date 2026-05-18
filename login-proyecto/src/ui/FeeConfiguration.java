package ui;

import logic.AppContext;
import model.Condominio;
import javax.swing.*;
import java.awt.*;

public class FeeConfiguration extends JFrame {

    private static final Color C_HEADER  = new Color(28,  28,  28);
    private static final Color C_BG      = new Color(245, 245, 245);
    private static final Color C_TOOLBAR = new Color(50,  50,  50);
    private static final Color C_BORDER  = new Color(200, 200, 200);
    private static final Color C_SAVE    = new Color(34,  139, 60);
    private static final Color C_DANGER  = new Color(160, 30,  30);

    private final Condominio condominio;
    private JTextField tfCurrentFee;
    private JTextField tfNewFee;
    private JLabel lblStatus;

    public FeeConfiguration(JFrame parent) {
        this.condominio = AppContext.getInstance().getCondominio();

        setTitle("Fee Configuration — Vista Verde");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        setPreferredSize(new Dimension(440, 420));
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_HEADER);
        p.setPreferredSize(new Dimension(0, 52));
        JLabel lbl = new JLabel("Fee Configuration", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 17));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildCenter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(16, 24, 12, 24),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
            )
        ));

        addSectionTitle(panel, "Current Fee");
        panel.add(Box.createVerticalStrut(8));

        tfCurrentFee = addReadOnlyField(panel, "Monthly fee (Q):");
        tfCurrentFee.setText(String.format("%.2f", condominio.getCuotaMensual()));

        panel.add(Box.createVerticalStrut(12));
        addSectionTitle(panel, "Update Fee");
        panel.add(Box.createVerticalStrut(8));

        addLabel(panel, "New monthly fee (Q):");
        tfNewFee = new JTextField();
        tfNewFee.setFont(new Font("Arial", Font.PLAIN, 13));
        tfNewFee.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        tfNewFee.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfNewFee.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        panel.add(tfNewFee);

        return panel;
    }

    private void addSectionTitle(JPanel parent, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(28, 28, 28));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(lbl);
    }

    private void addLabel(JPanel parent, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(new Color(40, 40, 40));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(4));
    }

    private JTextField addReadOnlyField(JPanel parent, String label) {
        addLabel(parent, label);
        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setFont(new Font("Arial", Font.PLAIN, 13));
        tf.setBackground(new Color(235, 235, 235));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        parent.add(tf);
        parent.add(Box.createVerticalStrut(8));
        return tf;
    }

    private JPanel buildFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(C_TOOLBAR);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(180, 180, 180));
        panel.add(lblStatus, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(C_TOOLBAR);

        JButton btnSave  = makeBtn("Save",  C_SAVE);
        JButton btnClose = makeBtn("Close", C_DANGER);

        btnSave.addActionListener(e -> doSave());
        btnClose.addActionListener(e -> dispose());

        btns.add(btnSave);
        btns.add(btnClose);
        panel.add(btns, BorderLayout.EAST);
        return panel;
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 32));
        return btn;
    }

    private void doSave() {
        String raw = tfNewFee.getText().trim();

        if (raw.isEmpty()) {
            setStatus("Please enter a value.", false);
            return;
        }

        double newFee;
        try {
            newFee = Double.parseDouble(raw);
        } catch (NumberFormatException ex) {
            setStatus("Invalid amount. Enter a number (e.g. 1500.00).", false);
            return;
        }

        if (newFee <= 0) {
            setStatus("Fee must be greater than zero.", false);
            return;
        }

        condominio.setCuotaMensual(newFee);
        tfCurrentFee.setText(String.format("%.2f", newFee));
        tfNewFee.setText("");
        setStatus(String.format("Fee updated to Q %.2f successfully.", newFee), true);
    }

    private void setStatus(String message, boolean success) {
        lblStatus.setText(message);
        lblStatus.setForeground(success
                ? new Color(120, 220, 120)
                : new Color(255, 110, 110));
    }
}
