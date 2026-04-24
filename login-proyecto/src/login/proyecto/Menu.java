package login.proyecto;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Menu extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(Menu.class.getName());

    // ── Design constants ─────────────────────────────────────────────────────
    private static final java.awt.Color C_HEADER  = new java.awt.Color(28,  28,  28);
    private static final java.awt.Color C_TOOLBAR = new java.awt.Color(50,  50,  50);
    private static final java.awt.Color C_BTN     = new java.awt.Color(70,  70,  70);
    private static final java.awt.Color C_BTN_OUT = new java.awt.Color(110, 110, 110);
    private static final java.awt.Color C_BG      = new java.awt.Color(245, 245, 245);
    private static final java.awt.Color C_TEXT    = new java.awt.Color(40,  40,  40);
    private static final java.awt.Color C_SUCCESS = new java.awt.Color(34,  139, 60);
    private static final java.awt.Color C_DANGER  = new java.awt.Color(180, 30,  30);

    // ── State ─────────────────────────────────────────────────────────────────
    private String loggedInUser;

    public Menu() {
        this("User");
    }

    public Menu(String username) {
        this.loggedInUser = username;
        initComponents();
        applyStyles();
        setupMenuBar();
        refreshUserTable();
        setLocationRelativeTo(null);
    }

    // ── Styling ───────────────────────────────────────────────────────────────
    private void applyStyles() {
        getContentPane().setBackground(C_TOOLBAR);

        // Header
        jLabelHeader.setOpaque(true);
        jLabelHeader.setBackground(C_HEADER);
        jLabelHeader.setForeground(java.awt.Color.WHITE);
        jLabelHeader.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 17));
        jLabelHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Toolbar buttons
        styleBtn(jButtonMaint,  C_BTN,     "User Maintenance");
        styleBtn(jButtonReset,  C_BTN,     "Password Reset");
        styleBtn(jButtonLogout, new java.awt.Color(160, 30, 30), "Logout");

        // Table section background
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(
            new java.awt.Color(200, 200, 200)));

        // Table title
        jLabelTableTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        jLabelTableTitle.setForeground(java.awt.Color.WHITE);

        // Style the table
        styleTable(jTable1);

        setSize(605, 490);
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

    private void styleTable(javax.swing.JTable t) {
        t.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        t.setRowHeight(28);
        t.setShowGrid(false);
        t.setIntercellSpacing(new java.awt.Dimension(0, 0));
        t.setBackground(java.awt.Color.WHITE);

        JTableHeader th = t.getTableHeader();
        th.setPreferredSize(new java.awt.Dimension(0, 34));
        th.setDefaultRenderer(new DefaultTableCellRenderer() {
            { setOpaque(true); }
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable tbl, Object val, boolean sel,
                    boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                setBackground(new java.awt.Color(45, 45, 45));
                setForeground(java.awt.Color.WHITE);
                setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
                setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        DefaultTableCellRenderer cell = new DefaultTableCellRenderer() {
            { setOpaque(true); }
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable tbl, Object val, boolean sel,
                    boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
                if (sel) {
                    setBackground(new java.awt.Color(220, 220, 220));
                    setForeground(new java.awt.Color(28, 28, 28));
                } else {
                    setBackground(row % 2 == 0 ? java.awt.Color.WHITE
                                               : new java.awt.Color(248, 248, 248));
                    if (col == 2) {
                        boolean active = "Active".equals(val);
                        setForeground(active ? C_SUCCESS : C_DANGER);
                        setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
                    } else {
                        setForeground(new java.awt.Color(40, 40, 40));
                    }
                }
                return this;
            }
        };
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(cell);
        }
    }

    private void setupMenuBar() {
        javax.swing.JMenuBar bar = new javax.swing.JMenuBar();
        bar.setBackground(new java.awt.Color(28, 28, 28));
        bar.setOpaque(true);

        javax.swing.JMenu mgmt = new javax.swing.JMenu("Management");
        mgmt.setForeground(java.awt.Color.WHITE);
        mgmt.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        mgmt.setOpaque(true);
        mgmt.setBackground(new java.awt.Color(28, 28, 28));

        javax.swing.JMenuItem miMaint = new javax.swing.JMenuItem("User Maintenance");
        javax.swing.JMenuItem miReset = new javax.swing.JMenuItem("Password Reset");
        miMaint.addActionListener(e -> new Mantenimiento(this).setVisible(true));
        miReset.addActionListener(e -> new Reinicio(this, loggedInUser).setVisible(true));
        mgmt.add(miMaint);
        mgmt.add(miReset);

        javax.swing.JMenu session = new javax.swing.JMenu("Session");
        session.setForeground(java.awt.Color.WHITE);
        session.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        session.setOpaque(true);
        session.setBackground(new java.awt.Color(28, 28, 28));

        javax.swing.JMenuItem miLogout = new javax.swing.JMenuItem("Logout");
        miLogout.addActionListener(e -> doLogout());
        session.add(miLogout);

        bar.add(mgmt);
        bar.add(session);
        setJMenuBar(bar);
    }

    public void refreshUserTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (UserManager.User u : UserManager.getInstance().getUsers()) {
            model.addRow(new Object[]{u.username, u.role, u.status});
        }
    }

    private void doLogout() {
        int r = javax.swing.JOptionPane.showConfirmDialog(this,
            "Are you sure you want to log out?",
            "Confirm Logout", javax.swing.JOptionPane.YES_NO_OPTION);
        if (r == javax.swing.JOptionPane.YES_OPTION) {
            new Inicio().setVisible(true);
            dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelHeader = new javax.swing.JLabel();
        jButtonMaint = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();
        jButtonLogout = new javax.swing.JButton();
        jLabelTableTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Main Menu");
        getContentPane().setLayout(null);

        jLabelHeader.setText("User Management System");
        getContentPane().add(jLabelHeader);
        jLabelHeader.setBounds(0, 0, 580, 55);

        jButtonMaint.setText("User Maintenance");
        jButtonMaint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMaintActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonMaint);
        jButtonMaint.setBounds(12, 62, 170, 32);

        jButtonReset.setText("Password Reset");
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonReset);
        jButtonReset.setBounds(192, 62, 160, 32);

        jButtonLogout.setText("Logout");
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonLogout);
        jButtonLogout.setBounds(362, 62, 110, 32);

        jLabelTableTitle.setText("Registered Users");
        getContentPane().add(jLabelTableTitle);
        jLabelTableTitle.setBounds(12, 108, 200, 20);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Username", "Role", "Status"}
        ) {
            boolean[] canEdit = {false, false, false};
            public boolean isCellEditable(int r, int c) { return canEdit[c]; }
        });
        jScrollPane1.setViewportView(jTable1);
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(12, 132, 556, 296);

        getContentPane().setPreferredSize(new java.awt.Dimension(580, 440));
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonMaintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMaintActionPerformed
        new Mantenimiento(this).setVisible(true);
    }//GEN-LAST:event_jButtonMaintActionPerformed

    private void jButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetActionPerformed
        new Reinicio(this, loggedInUser).setVisible(true);
    }//GEN-LAST:event_jButtonResetActionPerformed

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        doLogout();
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonMaint;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelTableTitle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
