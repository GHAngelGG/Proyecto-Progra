package login.proyecto;

import vistaverde.AppContext;
import vistaverde.model.Condominio;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class GeneralReport extends JFrame {

    private static final Color C_HEADER  = new Color(28,  28,  28);
    private static final Color C_BG      = new Color(245, 245, 245);
    private static final Color C_TOOLBAR = new Color(50,  50,  50);
    private static final Color C_BORDER  = new Color(200, 200, 200);
    private static final Color C_DANGER  = new Color(160, 30,  30);
    private static final Color C_GREEN   = new Color(34,  139, 60);
    private static final Color C_ROW_ALT = new Color(235, 242, 255);
    private static final Color C_TOTAL   = new Color(220, 230, 245);

    private static final String[] MONTHS = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    private final Condominio condominio;
    private final int currentYear;
    private final int currentMonth;

    private JSpinner spYear;
    private DefaultTableModel tableModel;
    private JLabel lblTotalCollected;
    private JLabel lblTotalExpected;
    private JLabel lblTotalPending;

    public GeneralReport(JFrame parent) {
        this.condominio   = AppContext.getInstance().getCondominio();
        this.currentYear  = LocalDate.now().getYear();
        this.currentMonth = LocalDate.now().getMonthValue();

        setTitle("General Report — Vista Verde");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        setPreferredSize(new Dimension(700, 520));
        pack();
        setLocationRelativeTo(parent);
        loadReport();
    }

    // ── Header ────────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_HEADER);
        p.setPreferredSize(new Dimension(0, 52));
        JLabel lbl = new JLabel("General Report — Condominium Income", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 17));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    // ── Center ────────────────────────────────────────────────────────────────

    private JPanel buildCenter() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setBackground(C_BG);
        outer.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        outer.add(buildControls(), BorderLayout.NORTH);
        outer.add(buildTable(),    BorderLayout.CENTER);
        outer.add(buildSummary(),  BorderLayout.SOUTH);

        return outer;
    }

    private JPanel buildControls() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setBackground(C_BG);

        JLabel lblYear = new JLabel("Year:");
        lblYear.setFont(new Font("Arial", Font.BOLD, 13));
        p.add(lblYear);

        spYear = new JSpinner(new SpinnerNumberModel(currentYear, 2024, currentYear, 1));
        spYear.setFont(new Font("Arial", Font.PLAIN, 13));
        spYear.setPreferredSize(new Dimension(80, 28));
        spYear.addChangeListener(e -> loadReport());
        p.add(spYear);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setBackground(C_GREEN);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.setPreferredSize(new Dimension(90, 28));
        btnRefresh.addActionListener(e -> loadReport());
        p.add(btnRefresh);

        return p;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Month", "Paid Houses", "Collected (Q)", "Expected (Q)", "Pending (Q)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(26);
        table.setGridColor(C_BORDER);
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(180, 210, 255));

        // Header style
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(50, 50, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(110);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);

        // Alternating row colors + highlight pending in red
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(col == 0 ? LEFT : RIGHT);
                if (sel) return this;

                // Last row (TOTAL) gets its own color
                if (row == tableModel.getRowCount() - 1) {
                    setBackground(C_TOTAL);
                    setFont(new Font("Arial", Font.BOLD, 12));
                    return this;
                }

                setFont(new Font("Arial", Font.PLAIN, 12));
                // Red text for pending column when > 0
                if (col == 4 && val != null && !val.toString().equals("Q 0.00")) {
                    setForeground(new Color(200, 50, 30));
                } else {
                    setForeground(Color.BLACK);
                }
                setBackground(row % 2 == 0 ? Color.WHITE : C_ROW_ALT);
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(C_BORDER));
        return sp;
    }

    private JPanel buildSummary() {
        JPanel p = new JPanel(new GridLayout(1, 3, 16, 0));
        p.setBackground(C_BG);
        p.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        lblTotalCollected = summaryCard("Total Collected", "Q 0.00", C_GREEN);
        lblTotalExpected  = summaryCard("Total Expected",  "Q 0.00", new Color(60, 60, 60));
        lblTotalPending   = summaryCard("Total Pending",   "Q 0.00", C_DANGER);

        p.add(lblTotalCollected.getParent());
        p.add(lblTotalExpected.getParent());
        p.add(lblTotalPending.getParent());
        return p;
    }

    private JLabel summaryCard(String title, String value, Color accent) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 11));
        lblTitle.setForeground(new Color(100, 100, 100));

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 15));
        lblValue.setForeground(accent);

        card.add(lblTitle);
        card.add(lblValue);
        return lblValue;
    }

    // ── Footer ────────────────────────────────────────────────────────────────

    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
        p.setBackground(C_TOOLBAR);

        JButton btnClose = new JButton("Close");
        btnClose.setBackground(C_DANGER);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 12));
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.setPreferredSize(new Dimension(90, 32));
        btnClose.addActionListener(e -> dispose());
        p.add(btnClose);
        return p;
    }

    // ── Logic ─────────────────────────────────────────────────────────────────

    private void loadReport() {
        tableModel.setRowCount(0);
        int year = (int) spYear.getValue();

        double totalCollected = 0;
        double totalExpected  = 0;
        double totalPending   = 0;

        // Show all 12 months, but only up to current month for current year
        int lastMonth = (year == currentYear) ? currentMonth : 12;

        for (int m = 1; m <= lastMonth; m++) {
            double collected = condominio.getTotalRecaudado(m, year);
            double expected  = condominio.getTotalEsperado();
            double pending   = expected - collected;
            int paidHouses   = (condominio.getCuotaMensual() > 0)
                               ? (int) Math.round(collected / condominio.getCuotaMensual())
                               : 0;

            tableModel.addRow(new Object[]{
                MONTHS[m - 1],
                paidHouses + " / 30",
                String.format("Q %.2f", collected),
                String.format("Q %.2f", expected),
                String.format("Q %.2f", pending)
            });

            totalCollected += collected;
            totalExpected  += expected;
            totalPending   += pending;
        }

        // TOTAL row
        tableModel.addRow(new Object[]{
            "TOTAL",
            "—",
            String.format("Q %.2f", totalCollected),
            String.format("Q %.2f", totalExpected),
            String.format("Q %.2f", totalPending)
        });

        // Summary cards
        lblTotalCollected.setText(String.format("Q %.2f", totalCollected));
        lblTotalExpected.setText(String.format("Q %.2f", totalExpected));
        lblTotalPending.setText(String.format("Q %.2f", totalPending));
    }
}
