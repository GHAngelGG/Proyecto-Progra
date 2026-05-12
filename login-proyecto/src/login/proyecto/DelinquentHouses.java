package login.proyecto;

import vistaverde.AppContext;
import vistaverde.model.Casa;
import vistaverde.model.Condominio;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DelinquentHouses extends JFrame {

    private static final Color C_HEADER   = new Color(28,  28,  28);
    private static final Color C_BG       = new Color(245, 245, 245);
    private static final Color C_TOOLBAR  = new Color(50,  50,  50);
    private static final Color C_BORDER   = new Color(200, 200, 200);
    private static final Color C_DANGER   = new Color(160, 30,  30);
    private static final Color C_ROW_ALT  = new Color(255, 240, 240);

    private static final String[] MONTHS = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    private final Condominio condominio;
    private final int currentYear;
    private final int currentMonth;

    private JComboBox<String> cbMonth;
    private JSpinner spYear;
    private DefaultTableModel tableModel;
    private JLabel lblCount;
    private JLabel lblAmount;

    public DelinquentHouses(JFrame parent) {
        this.condominio   = AppContext.getInstance().getCondominio();
        this.currentYear  = LocalDate.now().getYear();
        this.currentMonth = LocalDate.now().getMonthValue();

        setTitle("Delinquent Houses — Vista Verde");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        setPreferredSize(new Dimension(620, 500));
        pack();
        setLocationRelativeTo(parent);
        loadDelinquent();
    }

    // ── Header ────────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_HEADER);
        p.setPreferredSize(new Dimension(0, 52));
        JLabel lbl = new JLabel("Delinquent Houses", SwingConstants.CENTER);
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
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setBackground(C_BG);

        JLabel lblM = new JLabel("Month:");
        lblM.setFont(new Font("Arial", Font.BOLD, 13));
        p.add(lblM);

        cbMonth = new JComboBox<>(MONTHS);
        cbMonth.setSelectedIndex(currentMonth - 1);
        cbMonth.setFont(new Font("Arial", Font.PLAIN, 13));
        cbMonth.setPreferredSize(new Dimension(130, 28));
        p.add(cbMonth);

        JLabel lblY = new JLabel("Year:");
        lblY.setFont(new Font("Arial", Font.BOLD, 13));
        p.add(lblY);

        spYear = new JSpinner(new SpinnerNumberModel(currentYear, 2024, currentYear, 1));
        spYear.setFont(new Font("Arial", Font.PLAIN, 13));
        spYear.setPreferredSize(new Dimension(80, 28));
        p.add(spYear);

        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 12));
        btnSearch.setBackground(C_DANGER);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSearch.setPreferredSize(new Dimension(90, 28));
        btnSearch.addActionListener(e -> loadDelinquent());
        p.add(btnSearch);

        return p;
    }

    private JScrollPane buildTable() {
        String[] columns = {"House", "Owner", "Phone", "Pending months", "Amount owed (Q)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(26);
        table.setGridColor(C_BORDER);
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(255, 200, 200));

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(50, 50, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(col == 0 || col == 4 ? CENTER : LEFT);
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : C_ROW_ALT);
                    setForeground(col == 4 ? C_DANGER : Color.BLACK);
                }
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(C_BORDER));
        return sp;
    }

    private JPanel buildSummary() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        p.setBackground(C_BG);
        p.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        lblCount = new JLabel("Delinquent houses: 0");
        lblCount.setFont(new Font("Arial", Font.BOLD, 13));
        lblCount.setForeground(C_DANGER);

        lblAmount = new JLabel("  |  Total owed: Q 0.00");
        lblAmount.setFont(new Font("Arial", Font.BOLD, 13));
        lblAmount.setForeground(new Color(80, 80, 80));

        p.add(lblCount);
        p.add(lblAmount);
        return p;
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

    private void loadDelinquent() {
        tableModel.setRowCount(0);
        int month = cbMonth.getSelectedIndex() + 1;
        int year  = (int) spYear.getValue();

        ArrayList<Casa> morosas = condominio.getCasasMorosas(month, year);
        double totalOwed = 0;

        for (Casa c : morosas) {
            ArrayList<Integer> pending = c.getMesesPendientes(month, year);
            double owed = pending.size() * condominio.getCuotaMensual();
            totalOwed += owed;

            StringBuilder pendingStr = new StringBuilder();
            for (int m : pending) {
                if (pendingStr.length() > 0) pendingStr.append(", ");
                pendingStr.append(MONTHS[m - 1]);
            }

            tableModel.addRow(new Object[]{
                c.getNumero(),
                c.getPropietario().getNombre(),
                c.getPropietario().getTelefono(),
                pendingStr.toString(),
                String.format("Q %.2f", owed)
            });
        }

        lblCount.setText("Delinquent houses: " + morosas.size());
        lblAmount.setText("  |  Total owed: " + String.format("Q %.2f", totalOwed));
    }
}
