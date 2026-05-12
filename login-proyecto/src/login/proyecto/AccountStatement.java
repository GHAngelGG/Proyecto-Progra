package login.proyecto;

import vistaverde.AppContext;
import vistaverde.model.Casa;
import vistaverde.model.Condominio;
import vistaverde.model.Pago;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AccountStatement extends JFrame {

    private static final Color C_HEADER   = new Color(28,  28,  28);
    private static final Color C_BG       = new Color(245, 245, 245);
    private static final Color C_TOOLBAR  = new Color(50,  50,  50);
    private static final Color C_BORDER   = new Color(200, 200, 200);
    private static final Color C_DANGER   = new Color(160, 30,  30);
    private static final Color C_NO_OWNER = new Color(140, 140, 140);
    private static final Color C_HAS_DATA = new Color(34,  100, 180);
    private static final Color C_SELECTED = new Color(210, 150, 0);

    private static final String[] MONTHS = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    private final Condominio condominio;
    private final JButton[] houseButtons = new JButton[30];
    private int selectedHouse = 0;

    private JTextField tfHouseNumber;
    private JTextField tfOwnerName;
    private JTextField tfTotalPaid;
    private DefaultTableModel tableModel;
    private JTextArea taPending;
    private JLabel lblStatus;

    public AccountStatement(JFrame parent) {
        this.condominio = AppContext.getInstance().getCondominio();

        setTitle("Account Statement — Vista Verde");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        setPreferredSize(new Dimension(820, 560));
        pack();
        setLocationRelativeTo(parent);
        refreshMapColors();
    }

    // ── Header ────────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_HEADER);
        p.setPreferredSize(new Dimension(0, 52));
        JLabel lbl = new JLabel("Account Statement", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 17));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    // ── Center ────────────────────────────────────────────────────────────────

    private JPanel buildCenter() {
        JPanel p = new JPanel(new GridLayout(1, 2, 16, 0));
        p.setBackground(C_BG);
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        p.add(buildMapPanel());
        p.add(buildInfoPanel());
        return p;
    }

    private JPanel buildMapPanel() {
        JPanel outer = new JPanel(new BorderLayout(0, 10));
        outer.setBackground(C_BG);

        JLabel title = new JLabel("Condominium Map");
        title.setFont(new Font("Arial", Font.BOLD, 13));
        title.setForeground(new Color(40, 40, 40));
        outer.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(6, 5, 6, 6));
        grid.setBackground(C_BG);

        for (int i = 0; i < 30; i++) {
            final int num = i + 1;
            JButton btn = new JButton(String.valueOf(num));
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setForeground(Color.WHITE);
            btn.setBackground(C_NO_OWNER);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setToolTipText("House " + num);
            btn.addActionListener(e -> onHouseClick(num));
            houseButtons[i] = btn;
            grid.add(btn);
        }

        outer.add(grid, BorderLayout.CENTER);
        outer.add(buildLegend(), BorderLayout.SOUTH);
        return outer;
    }

    private JPanel buildLegend() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        p.setBackground(C_BG);
        p.add(legendItem(C_NO_OWNER, "No owner"));
        p.add(legendItem(C_HAS_DATA, "Has payments"));
        p.add(legendItem(C_SELECTED, "Selected"));
        return p;
    }

    private JPanel legendItem(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setBackground(C_BG);
        JLabel sq = new JLabel();
        sq.setOpaque(true);
        sq.setBackground(color);
        sq.setPreferredSize(new Dimension(14, 14));
        p.add(sq);
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setForeground(new Color(60, 60, 60));
        p.add(lbl);
        return p;
    }

    private JPanel buildInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        addSectionTitle(panel, "House Information");
        panel.add(Box.createVerticalStrut(10));
        tfHouseNumber = addReadOnlyField(panel, "House number:");
        tfOwnerName   = addReadOnlyField(panel, "Owner:");
        tfTotalPaid   = addReadOnlyField(panel, "Total paid:");

        panel.add(Box.createVerticalStrut(6));
        addSectionTitle(panel, "Paid months");
        panel.add(Box.createVerticalStrut(8));

        String[] columns = {"Month", "Year", "Amount (Q)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(50, 50, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(210, 230, 255));
        table.setGridColor(C_BORDER);
        table.setShowGrid(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(110);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);

        JScrollPane sp = new JScrollPane(table);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setBorder(BorderFactory.createLineBorder(C_BORDER));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panel.add(sp);

        panel.add(Box.createVerticalStrut(8));
        addSectionTitle(panel, "Pending months (this year):");
        panel.add(Box.createVerticalStrut(6));

        taPending = new JTextArea(2, 20);
        taPending.setEditable(false);
        taPending.setFont(new Font("Arial", Font.PLAIN, 12));
        taPending.setBackground(new Color(255, 240, 240));
        taPending.setForeground(new Color(160, 30, 30));
        taPending.setWrapStyleWord(true);
        taPending.setLineWrap(true);
        JScrollPane spPending = new JScrollPane(taPending);
        spPending.setAlignmentX(Component.LEFT_ALIGNMENT);
        spPending.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        spPending.setBorder(BorderFactory.createLineBorder(C_BORDER));
        panel.add(spPending);

        return panel;
    }

    // ── Footer ────────────────────────────────────────────────────────────────

    private JPanel buildFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(C_TOOLBAR);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(180, 180, 180));
        panel.add(lblStatus, BorderLayout.WEST);

        JButton btnClose = makeBtn("Close", C_DANGER);
        btnClose.addActionListener(e -> dispose());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(C_TOOLBAR);
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

    // ── Helpers ───────────────────────────────────────────────────────────────

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
        parent.add(Box.createVerticalStrut(10));
        return tf;
    }

    // ── Logic ─────────────────────────────────────────────────────────────────

    private void onHouseClick(int number) {
        Casa house = condominio.getCasa(number);

        if (!house.tienePropietario()) {
            setStatus("House " + number + " has no registered owner.", false);
            return;
        }

        if (selectedHouse > 0 && selectedHouse != number) {
            refreshButtonColor(selectedHouse);
        }

        if (selectedHouse == number) {
            refreshButtonColor(number);
            selectedHouse = 0;
            clearInfo();
            setStatus("Selection cleared.", true);
            return;
        }

        selectedHouse = number;
        houseButtons[number - 1].setBackground(C_SELECTED);
        loadHouseData(house);
        setStatus("House " + number + " — " + house.getPropietario().getNombre(), true);
    }

    private void loadHouseData(Casa house) {
        tfHouseNumber.setText(String.valueOf(house.getNumero()));
        tfOwnerName.setText(house.getPropietario().getNombre());
        tfTotalPaid.setText(String.format("Q %.2f", house.getTotalPagado()));

        tableModel.setRowCount(0);
        ArrayList<Pago> pagos = house.getPagos();

        if (pagos.isEmpty()) {
            setStatus("House " + house.getNumero() + " has no payments recorded.", false);
        } else {
            for (Pago p : pagos) {
                tableModel.addRow(new Object[]{
                    MONTHS[p.getMes() - 1],
                    p.getAnio(),
                    String.format("Q %.2f", p.getMonto())
                });
            }
        }

        // Pending months for current year
        int currentMonth = java.time.LocalDate.now().getMonthValue();
        int currentYear  = java.time.LocalDate.now().getYear();
        java.util.ArrayList<Integer> pending = house.getMesesPendientes(currentMonth, currentYear);
        if (pending.isEmpty()) {
            taPending.setText("No pending months — all paid up to date.");
            taPending.setForeground(new Color(34, 139, 60));
        } else {
            StringBuilder sb = new StringBuilder();
            for (int m : pending) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(MONTHS[m - 1]);
            }
            taPending.setText(sb.toString());
            taPending.setForeground(new Color(160, 30, 30));
        }
    }

    private void clearInfo() {
        tfHouseNumber.setText("");
        tfOwnerName.setText("");
        tfTotalPaid.setText("");
        tableModel.setRowCount(0);
        taPending.setText("");
    }

    private void refreshMapColors() {
        for (int i = 0; i < 30; i++) {
            if (selectedHouse == i + 1) {
                houseButtons[i].setBackground(C_SELECTED);
            } else {
                refreshButtonColor(i + 1);
            }
        }
    }

    private void refreshButtonColor(int number) {
        Casa house = condominio.getCasa(number);
        if (!house.tienePropietario()) {
            houseButtons[number - 1].setBackground(C_NO_OWNER);
        } else if (!house.getPagos().isEmpty()) {
            houseButtons[number - 1].setBackground(C_HAS_DATA);
        } else {
            houseButtons[number - 1].setBackground(C_NO_OWNER);
        }
    }

    private void setStatus(String message, boolean success) {
        lblStatus.setText(message);
        lblStatus.setForeground(success
                ? new Color(120, 220, 120)
                : new Color(255, 110, 110));
    }
}
