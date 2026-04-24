package login.proyecto;

public class Mantenimiento extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(Mantenimiento.class.getName());

    // ── Design constants ─────────────────────────────────────────────────────
    private static final java.awt.Color C_HEADER = new java.awt.Color(28, 28, 28);
    private static final java.awt.Color C_BTN    = new java.awt.Color(50, 50, 50);
    private static final java.awt.Color C_CANCEL = new java.awt.Color(110, 110, 110);
    private static final java.awt.Color C_BG     = new java.awt.Color(245, 245, 245);
    private static final java.awt.Color C_TEXT   = new java.awt.Color(40, 40, 40);
    private static final java.awt.Color C_BORDER = new java.awt.Color(200, 200, 200);
    private static final java.awt.Color C_SEP    = new java.awt.Color(210, 210, 210);

    // ── State ─────────────────────────────────────────────────────────────────
    private final Menu parentMenu;

    public Mantenimiento() {
        this(null);
    }

    public Mantenimiento(Menu parentMenu) {
        this.parentMenu = parentMenu;
        initComponents();
        applyStyles();
        populateSelectUser();
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

        java.awt.Font labelFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 12);
        java.awt.Color labelColor = C_TEXT;
        for (javax.swing.JLabel l : new javax.swing.JLabel[]{
                jLabelSelectUser, jLabelUsername, jLabelPassword,
                jLabelRole, jLabelStatus}) {
            l.setFont(labelFont);
            l.setForeground(labelColor);
        }

        // Separator line
        jLabelSep.setOpaque(true);
        jLabelSep.setBackground(C_SEP);

        // Fields
        styleField(jTextFieldUsername);
        styleField(jPasswordField1);

        // Combos
        for (javax.swing.JComboBox<?> c : new javax.swing.JComboBox[]{
                jComboBoxSelectUser, jComboBoxRole, jComboBoxStatus}) {
            c.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
            c.setBackground(java.awt.Color.WHITE);
        }
        jComboBoxRole.setSelectedIndex(2); // default: User

        // Buttons
        styleBtn(jButtonSave,   C_BTN,    "Save");
        styleBtn(jButtonCancel, C_CANCEL, "Cancel");

        setSize(445, 380);
    }

    private void styleField(javax.swing.JTextField f) {
        f.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        f.setBackground(java.awt.Color.WHITE);
        f.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(C_BORDER, 1),
            javax.swing.BorderFactory.createEmptyBorder(3, 7, 3, 7)));
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

    private void populateSelectUser() {
        jComboBoxSelectUser.removeAllItems();
        jComboBoxSelectUser.addItem("-- New User --");
        for (UserManager.User u : UserManager.getInstance().getUsers()) {
            jComboBoxSelectUser.addItem(u.username);
        }
    }

    private void onUserSelected() {
        String sel = (String) jComboBoxSelectUser.getSelectedItem();
        boolean isNew = sel == null || sel.equals("-- New User --");

        jTextFieldUsername.setEditable(isNew);
        jLabelPassword.setVisible(isNew);
        jPasswordField1.setVisible(isNew);
        jLabelStatus.setVisible(!isNew);
        jComboBoxStatus.setVisible(!isNew);

        if (isNew) {
            jTextFieldUsername.setText("");
            jPasswordField1.setText("");
            jComboBoxRole.setSelectedIndex(2);
            jComboBoxStatus.setSelectedIndex(0);
        } else {
            UserManager.User u = UserManager.getInstance().findByUsername(sel);
            if (u != null) {
                jTextFieldUsername.setText(u.username);
                selectItem(jComboBoxRole,   u.role);
                selectItem(jComboBoxStatus, u.status);
            }
        }
    }

    private void selectItem(javax.swing.JComboBox<String> combo, String value) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equals(value)) { combo.setSelectedIndex(i); return; }
        }
    }

    private void doSave() {
        String sel      = (String) jComboBoxSelectUser.getSelectedItem();
        boolean isNew   = sel == null || sel.equals("-- New User --");
        String username = jTextFieldUsername.getText().trim();
        String role     = (String) jComboBoxRole.getSelectedItem();
        String status   = (String) jComboBoxStatus.getSelectedItem();

        if (username.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Username cannot be empty.", "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isNew) {
            String password = new String(jPasswordField1.getPassword());
            if (password.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Password is required for new users.", "Validation Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            String err = UserManager.getInstance().addUser(username, password, role);
            if (err != null) {
                javax.swing.JOptionPane.showMessageDialog(this, err,
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            javax.swing.JOptionPane.showMessageDialog(this,
                "User '" + username + "' added successfully.",
                "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            UserManager.getInstance().updateUser(username, role, status);
            String action = "Inactive".equals(status) ? "inactivated" : "updated";
            javax.swing.JOptionPane.showMessageDialog(this,
                "User '" + username + "' " + action + " successfully.",
                "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

        if (parentMenu != null) parentMenu.refreshUserTable();
        dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelHeader = new javax.swing.JLabel();
        jLabelSelectUser = new javax.swing.JLabel();
        jComboBoxSelectUser = new javax.swing.JComboBox<>();
        jLabelSep = new javax.swing.JLabel();
        jLabelUsername = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabelRole = new javax.swing.JLabel();
        jComboBoxRole = new javax.swing.JComboBox<>();
        jLabelStatus = new javax.swing.JLabel();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        jButtonSave = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("User Maintenance");
        getContentPane().setLayout(null);

        jLabelHeader.setText("User Maintenance");
        getContentPane().add(jLabelHeader);
        jLabelHeader.setBounds(0, 0, 420, 55);

        jLabelSelectUser.setText("Select User:");
        getContentPane().add(jLabelSelectUser);
        jLabelSelectUser.setBounds(20, 72, 100, 18);

        jComboBoxSelectUser.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[] { "-- New User --" }));
        jComboBoxSelectUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSelectUserActionPerformed(evt);
            }
        });
        getContentPane().add(jComboBoxSelectUser);
        jComboBoxSelectUser.setBounds(130, 70, 200, 28);

        jLabelSep.setText(" ");
        getContentPane().add(jLabelSep);
        jLabelSep.setBounds(20, 110, 380, 1);

        jLabelUsername.setText("Username:");
        getContentPane().add(jLabelUsername);
        jLabelUsername.setBounds(20, 125, 90, 18);

        getContentPane().add(jTextFieldUsername);
        jTextFieldUsername.setBounds(130, 122, 200, 28);

        jLabelPassword.setText("Password:");
        getContentPane().add(jLabelPassword);
        jLabelPassword.setBounds(20, 162, 90, 18);

        getContentPane().add(jPasswordField1);
        jPasswordField1.setBounds(130, 159, 200, 28);

        jLabelRole.setText("Role:");
        getContentPane().add(jLabelRole);
        jLabelRole.setBounds(20, 199, 90, 18);

        jComboBoxRole.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[] { "Admin", "Manager", "User" }));
        getContentPane().add(jComboBoxRole);
        jComboBoxRole.setBounds(130, 196, 200, 28);

        jLabelStatus.setText("Status:");
        getContentPane().add(jLabelStatus);
        jLabelStatus.setBounds(20, 236, 90, 18);

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[] { "Active", "Inactive" }));
        getContentPane().add(jComboBoxStatus);
        jComboBoxStatus.setBounds(130, 233, 200, 28);

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSave);
        jButtonSave.setBounds(95, 285, 110, 34);

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonCancel);
        jButtonCancel.setBounds(220, 285, 110, 34);

        getContentPane().setPreferredSize(new java.awt.Dimension(420, 340));
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxSelectUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSelectUserActionPerformed
        onUserSelected();
    }//GEN-LAST:event_jComboBoxSelectUserActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        doSave();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox<String> jComboBoxRole;
    private javax.swing.JComboBox<String> jComboBoxSelectUser;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelRole;
    private javax.swing.JLabel jLabelSelectUser;
    private javax.swing.JLabel jLabelSep;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelUsername;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables
}
