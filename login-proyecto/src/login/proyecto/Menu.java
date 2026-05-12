package login.proyecto;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class Menu extends JFrame {

    private static final Color C_HEADER  = new Color(28,  28,  28);
    private static final Color C_BG      = new Color(245, 245, 245);
    private static final Color C_TOOLBAR = new Color(50,  50,  50);
    private static final Color C_BTN     = new Color(60,  60,  60);
    private static final Color C_DANGER  = new Color(160, 30,  30);

    private final String loggedInUser;

    public Menu() {
        this("User");
    }

    public Menu(String username) {
        this.loggedInUser = username;

        setTitle("Vista Verde Condominium — Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildModules(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        setPreferredSize(new Dimension(580, 460));
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_HEADER);
        p.setPreferredSize(new Dimension(0, 70));

        JPanel texts = new JPanel(new GridLayout(2, 1));
        texts.setBackground(C_HEADER);
        texts.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel title = new JLabel("Condominium Vista Verde", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        texts.add(title);

        String monthYear = LocalDate.now().getMonth()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + LocalDate.now().getYear();
        JLabel subtitle = new JLabel("Administration System  —  " + monthYear, SwingConstants.CENTER);
        subtitle.setForeground(new Color(180, 180, 180));
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        texts.add(subtitle);

        p.add(texts, BorderLayout.CENTER);

        JLabel userLabel = new JLabel("  Logged in: " + loggedInUser + "  ");
        userLabel.setForeground(new Color(160, 160, 160));
        userLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        p.add(userLabel, BorderLayout.EAST);

        return p;
    }

    private JPanel buildModules() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(C_BG);
        outer.setBorder(BorderFactory.createEmptyBorder(24, 32, 16, 32));

        JLabel section = new JLabel("System Modules");
        section.setFont(new Font("Arial", Font.BOLD, 13));
        section.setForeground(new Color(80, 80, 80));
        section.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        outer.add(section, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 2, 12, 12));
        grid.setBackground(C_BG);

        grid.add(moduleButton("Register Owner",        "Add or view property owners",       this::openRegisterOwner));
        grid.add(moduleButton("Register Payment",      "Record monthly fee payments",        this::openRegisterPayment));
        grid.add(moduleButton("Fee Configuration",     "Update the monthly fee amount",      this::openFeeConfiguration));
        grid.add(moduleButton("Account Statement",     "View payment history by house",      this::openAccountStatement));
        grid.add(moduleButton("General Report",        "Summary of condominium income",      this::openGeneralReport));
        grid.add(moduleButton("Delinquent Houses",     "List of houses with pending fees",   this::openDelinquentHouses));

        outer.add(grid, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
        p.setBackground(C_TOOLBAR);

        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.setBackground(C_DANGER);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(100, 32));
        logoutBtn.addActionListener(e -> doLogout());
        p.add(logoutBtn);

        return p;
    }

    private JButton moduleButton(String title, String description, Runnable action) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(new Font("Arial", Font.BOLD, 13));
                g2.setColor(Color.WHITE);
                g2.drawString(title, 12, 28);
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.setColor(new Color(200, 200, 200));
                g2.drawString(description, 12, 46);
            }
        };
        btn.setBackground(C_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 64));
        btn.addActionListener(e -> action.run());
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(80, 80, 80)); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(C_BTN); }
        });
        return btn;
    }

    /** Opens a sub-screen and blocks the menu until the sub-screen is closed. */
    private void openAndBlock(JFrame screen) {
        setEnabled(false);
        screen.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                setEnabled(true);
                toFront();
            }
        });
        screen.setVisible(true);
    }

    private void openRegisterOwner() {
        openAndBlock(new RegistroPropietario(this));
    }

    private void openRegisterPayment() {
        openAndBlock(new RegisterPayment(this));
    }

    private void openFeeConfiguration() {
        openAndBlock(new FeeConfiguration(this));
    }

    private void openAccountStatement() {
        openAndBlock(new AccountStatement(this));
    }

    private void openGeneralReport() {
        openAndBlock(new GeneralReport(this));
    }

    private void openDelinquentHouses() {
        openAndBlock(new DelinquentHouses(this));
    }

    private void openComingSoon() {
        JOptionPane.showMessageDialog(this,
                "This feature is currently under development.",
                "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
    }

    private void doLogout() {
        int r = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            new Inicio().setVisible(true);
            dispose();
        }
    }
}
