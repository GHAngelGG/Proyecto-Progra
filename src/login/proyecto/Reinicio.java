package login.proyecto;

public class Reinicio extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(Reinicio.class.getName());

    // ── Design constants ─────────────────────────────────────────────────────
    private static final java.awt.Color C_HEADER  = new java.awt.Color(28, 28, 28);
    private static final java.awt.Color C_BTN     = new java.awt.Color(50, 50, 50);
    private static final java.awt.Color C_CANCEL  = new java.awt.Color(110, 110, 110);
    private static final java.awt.Color C_BG      = new java.awt.Color(245, 245, 245);
    private static final java.awt.Color C_TEXT    = new java.awt.Color(40, 40, 40);
    private static final java.awt.Color C_MUTED   = new java.awt.Color(120, 120, 120);
    private static final java.awt.Color C_BORDER  = new java.awt.Color(200, 200, 200);
    private static final java.awt.Color C_SUCCESS = new java.awt.Color(34, 139, 60);
    private static final java.awt.Color C_DANGER  = new java.awt.Color(180, 30, 30);
    private static final String SPECIAL = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

    // ── State ─────────────────────────────────────────────────────────────────
    private final Menu   parentMenu;
    private final String username;

    public Reinicio() {
        this(null, "Unknown");
    }

    public Reinicio(Menu parentMenu, String username) {
        this.parentMenu = parentMenu;
        this.username   = username;
        initComponents();
        applyStyles();
        attachDocumentListener();
        setLocationRelativeTo(null);
    }

    // ── Styling ───────────────────────────────────────────────────────────────
    private void applyStyles() {
        getContentPane().setBackground(C_BG);

        jLabelHeader.setOpaque(true);
        jLabelHeader.setBackground(C_HEADER);
        jLabelHeader.setForeground(java.awt.Color.WHITE);
        jLabelHeader.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        jLabelHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));

        jLabelInfo.setText("Changing password for: " + username);
        jLabelInfo.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 12));
        jLabelInfo.setForeground(C_MUTED);

        for (javax.swing.JLabel l : new javax.swing.JLabel[]{jLabelNewPass, jLabelConfirm}) {
            l.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
            l.setForeground(C_TEXT);
        }

        styleField(jPasswordFieldNew);
        styleField(jPasswordFieldConfirm);

        // Requirements
        jLabelReqTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        jLabelReqTitle.setForeground(C_TEXT);
        for (javax.swing.JLabel l : new javax.swing.JLabel[]{
                jLabelReqLength, jLabelReqUpper, jLabelReqSpecial}) {
            l.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
            l.setForeground(C_DANGER);
        }

        // Buttons
        styleBtn(jButtonSave,   C_BTN,    "Save Password");
        styleBtn(jButtonCancel, C_CANCEL, "Cancel");

        setSize(425, 425);
    }

    private void styleField(javax.swing.JPasswordField f) {
        f.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        f.setBackground(java.awt.Color.WHITE);
        f.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(C_BORDER, 1),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    private void styleBtn(javax.swing.JButton b, java.awt.Color bg, String text) {
        b.setText(text);
        b.setBackground(bg);
        b.setForeground(java.awt.Color.WHITE);
        b.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
    }

    // ── Live requirement checker ───────────────────────────────────────────────
    private void attachDocumentListener() {
        jPasswordFieldNew.getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e)  { checkReqs(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e)  { checkReqs(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { checkReqs(); }
            });
    }

    private void checkReqs() {
        String p = new String(jPasswordFieldNew.getPassword());
        setReq(jLabelReqLength,  p.length() >= 13, "Minimum 13 characters");
        setReq(jLabelReqUpper,   p.chars().anyMatch(Character::isUpperCase), "At least 1 uppercase letter");
        setReq(jLabelReqSpecial, p.chars().anyMatch(c -> SPECIAL.indexOf(c) >= 0),
               "At least 1 special character  (!@#$%...)");
    }

    private void setReq(javax.swing.JLabel lbl, boolean met, String text) {
        lbl.setText((met ? "\u2713  " : "\u2717  ") + text);
        lbl.setForeground(met ? C_SUCCESS : C_DANGER);
    }

    private void doSave() {
        String newPass  = new String(jPasswordFieldNew.getPassword());
        String confPass = new String(jPasswordFieldConfirm.getPassword());

        if (newPass.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Please enter a new password.", "Missing Field",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!newPass.equals(confPass)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Passwords do not match. Please try again.", "Mismatch",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            jPasswordFieldConfirm.setText("");
            jPasswordFieldConfirm.requestFocus();
            return;
        }
        if (newPass.length() < 13) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Password must be at least 13 characters long.", "Weak Password",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newPass.chars().noneMatch(Character::isUpperCase)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Password must contain at least 1 uppercase letter.", "Weak Password",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newPass.chars().noneMatch(c -> SPECIAL.indexOf(c) >= 0)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Password must contain at least 1 special character (!@#$%...)", "Weak Password",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserManager.getInstance().updatePassword(username, newPass);
        javax.swing.JOptionPane.showMessageDialog(this,
            "Password updated successfully!", "Success",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelHeader = new javax.swing.JLabel();
        jLabelInfo = new javax.swing.JLabel();
        jLabelNewPass = new javax.swing.JLabel();
        jPasswordFieldNew = new javax.swing.JPasswordField();
        jLabelConfirm = new javax.swing.JLabel();
        jPasswordFieldConfirm = new javax.swing.JPasswordField();
        jLabelReqTitle = new javax.swing.JLabel();
        jLabelReqLength = new javax.swing.JLabel();
        jLabelReqUpper = new javax.swing.JLabel();
        jLabelReqSpecial = new javax.swing.JLabel();
        jButtonSave = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Password Reset");
        getContentPane().setLayout(null);

        jLabelHeader.setText("Password Reset");
        getContentPane().add(jLabelHeader);
        jLabelHeader.setBounds(0, 0, 400, 55);

        jLabelInfo.setText("Changing password for: ...");
        getContentPane().add(jLabelInfo);
        jLabelInfo.setBounds(20, 66, 360, 18);

        jLabelNewPass.setText("New password:");
        getContentPane().add(jLabelNewPass);
        jLabelNewPass.setBounds(20, 98, 130, 18);
        getContentPane().add(jPasswordFieldNew);
        jPasswordFieldNew.setBounds(20, 120, 360, 30);

        jLabelConfirm.setText("Confirm password:");
        getContentPane().add(jLabelConfirm);
        jLabelConfirm.setBounds(20, 163, 130, 18);
        getContentPane().add(jPasswordFieldConfirm);
        jPasswordFieldConfirm.setBounds(20, 185, 360, 30);

        jLabelReqTitle.setText("Password requirements:");
        getContentPane().add(jLabelReqTitle);
        jLabelReqTitle.setBounds(20, 228, 360, 18);

        jLabelReqLength.setText("○  Minimum 13 characters");
        getContentPane().add(jLabelReqLength);
        jLabelReqLength.setBounds(32, 250, 340, 18);

        jLabelReqUpper.setText("○  At least 1 uppercase letter");
        getContentPane().add(jLabelReqUpper);
        jLabelReqUpper.setBounds(32, 272, 340, 18);

        jLabelReqSpecial.setText("○  At least 1 special character  (!@#$%...)");
        getContentPane().add(jLabelReqSpecial);
        jLabelReqSpecial.setBounds(32, 294, 340, 18);

        jButtonSave.setText("Save Password");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSave);
        jButtonSave.setBounds(75, 335, 130, 34);

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonCancel);
        jButtonCancel.setBounds(222, 335, 100, 34);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        doSave();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JLabel jLabelConfirm;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelInfo;
    private javax.swing.JLabel jLabelNewPass;
    private javax.swing.JLabel jLabelReqLength;
    private javax.swing.JLabel jLabelReqSpecial;
    private javax.swing.JLabel jLabelReqTitle;
    private javax.swing.JLabel jLabelReqUpper;
    private javax.swing.JPasswordField jPasswordFieldConfirm;
    private javax.swing.JPasswordField jPasswordFieldNew;
    // End of variables declaration//GEN-END:variables
}
