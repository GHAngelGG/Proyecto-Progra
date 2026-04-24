package login.proyecto;

public class Inicio extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(Inicio.class.getName());

    // ── Design constants (white / gray / black) ──────────────────────────────
    private static final java.awt.Color C_HEADER = new java.awt.Color(28, 28, 28);
    private static final java.awt.Color C_BTN    = new java.awt.Color(50, 50, 50);
    private static final java.awt.Color C_BG     = new java.awt.Color(245, 245, 245);
    private static final java.awt.Color C_TEXT   = new java.awt.Color(40, 40, 40);
    private static final java.awt.Color C_DANGER = new java.awt.Color(180, 30, 30);
    private static final java.awt.Color C_BORDER = new java.awt.Color(200, 200, 200);

    // ── State ────────────────────────────────────────────────────────────────
    private int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    public Inicio() {
        initComponents();
        applyStyles();
        setLocationRelativeTo(null);
    }

    // ── Styling (runs after initComponents, does not conflict with designer) ─
    private void applyStyles() {
        getContentPane().setBackground(C_BG);

        jLabelHeader.setOpaque(true);
        jLabelHeader.setBackground(C_HEADER);
        jLabelHeader.setForeground(java.awt.Color.WHITE);
        jLabelHeader.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));

        jLabelUser.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        jLabelUser.setForeground(C_TEXT);
        jLabelPass.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        jLabelPass.setForeground(C_TEXT);

        styleField(jTextFieldUser);
        styleField(jPasswordField1);

        jLabelError.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
        jLabelError.setForeground(C_DANGER);

        jButtonSignIn.setBackground(C_BTN);
        jButtonSignIn.setForeground(java.awt.Color.WHITE);
        jButtonSignIn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        jButtonSignIn.setFocusPainted(false);
        jButtonSignIn.setBorderPainted(false);
        jButtonSignIn.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        getRootPane().setDefaultButton(jButtonSignIn);
        setSize(390, 340);
    }

    private void styleField(javax.swing.JTextField f) {
        f.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        f.setBackground(java.awt.Color.WHITE);
        f.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(C_BORDER, 1),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelHeader = new javax.swing.JLabel();
        jLabelUser = new javax.swing.JLabel();
        jTextFieldUser = new javax.swing.JTextField();
        jLabelPass = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabelError = new javax.swing.JLabel();
        jButtonSignIn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("System Login");
        getContentPane().setLayout(null);

        jLabelHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHeader.setText("System Login");
        getContentPane().add(jLabelHeader);
        jLabelHeader.setBounds(0, 0, 370, 60);

        jLabelUser.setText("Username");
        getContentPane().add(jLabelUser);
        jLabelUser.setBounds(60, 82, 90, 18);

        getContentPane().add(jTextFieldUser);
        jTextFieldUser.setBounds(60, 104, 250, 32);

        jLabelPass.setText("Password");
        getContentPane().add(jLabelPass);
        jLabelPass.setBounds(60, 150, 90, 18);

        getContentPane().add(jPasswordField1);
        jPasswordField1.setBounds(60, 172, 250, 32);

        jLabelError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelError.setText(" ");
        getContentPane().add(jLabelError);
        jLabelError.setBounds(60, 213, 250, 18);

        jButtonSignIn.setText("Sign In");
        jButtonSignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSignInActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSignIn);
        jButtonSignIn.setBounds(110, 241, 150, 36);

        getContentPane().setPreferredSize(new java.awt.Dimension(370, 300));
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSignInActionPerformed
        String username = jTextFieldUser.getText().trim();
        String password = new String(jPasswordField1.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            jLabelError.setText("Please enter your username and password.");
            return;
        }

        if (UserManager.getInstance().authenticate(username, password)) {
            loginAttempts = 0;
            new Menu(username).setVisible(true);
            dispose();
        } else {
            loginAttempts++;
            jPasswordField1.setText("");
            jPasswordField1.requestFocus();
            if (loginAttempts >= MAX_ATTEMPTS) {
                jButtonSignIn.setEnabled(false);
                jTextFieldUser.setEnabled(false);
                jPasswordField1.setEnabled(false);
                jLabelError.setText("Account locked. Contact your administrator.");
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Account locked after " + MAX_ATTEMPTS + " failed attempts.",
                    "Account Locked", javax.swing.JOptionPane.ERROR_MESSAGE);
            } else {
                jLabelError.setText("Invalid credentials — "
                    + (MAX_ATTEMPTS - loginAttempts) + " attempt(s) remaining.");
            }
        }
    }//GEN-LAST:event_jButtonSignInActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSignIn;
    private javax.swing.JLabel jLabelError;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelPass;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextFieldUser;
    // End of variables declaration//GEN-END:variables
}
