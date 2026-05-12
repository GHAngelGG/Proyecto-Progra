package login.proyecto;

import vistaverde.AppContext;
import vistaverde.model.Casa;
import vistaverde.model.Condominio;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class GeneralReport extends JFrame {

    private static final Color C_HEADER   = new Color(28,  28,  28);
    private static final Color C_BG       = new Color(245, 245, 245);
    private static final Color C_TOOLBAR  = new Color(50,  50,  50);
    private static final Color C_BORDER   = new Color(200, 200, 200);
    private static final Color C_DANGER   = new Color(160, 30,  30);
    private static final Color C_GREEN    = new Color(34,  139, 60);
    private static final Color C_ROW_ALT  = new Color(240, 245, 255);

    private final Condominio condominio;
    private final int currentMonth;
    private final int currentYear;

    private DefaultTableModel tableModel;
    private JLabel lblCollected;
    private JLabel lblExpected;
    private JLabel lblPending;

    public GeneralReport(JFrame parent) {
        this.condominio   = AppContext.getInstance().getCondominio();
        this.currentMonth = LocalDate.now().getMonthValue();
        this.currentYear  = LocalDate.now().getYear();

        setTitle("General Report — Vista Verde");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        setPreferredSize(new Dimension(720, 560));
        pack();
        setLocationRelativeTo(parent);
        loadReport();
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_HEADER);
        p.setPreferredSize(new Dimension(0, 52));

        String monthName = LocalDate.now()
                .getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        JLabel lbl = new JLabel("General Report — " + monthName + " " + currentYear,
                SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 17));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildCenter() {
        JPanel outer = new JPanel(new BorderLayout(0, 10));
        outer.setBackground(C_BG);
        outer.setBorder(BorderFactory.createEmptyBorder(14, 18, 8, 18));
        outer.add(buildTable(),   BorderLayout.CENTER);
        outer.add(buildSummary(), BorderLayout.SOUTH);
        return outer;
    }

    private JScrollPane buildTable() {
        String[] cols = {"House", "Owner", "Status — " + getCurrentMonthName(), "Total Paid (Year)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(24);
        table.setGridColor(C_BORDER);
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(180, 210, 255));

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(50, 50, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(140);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(col == 0 || col == 3 ? CENTER : LEFT);
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : C_ROW_ALT);
                    if (col == 2) {
                        boolean paid = "Paid".equals(val);
                        setForeground(paid ? C_GREEN : C_DANGER);
                        setFont(new Font("Arial", Font.BOLD, 12));
                    } else {
                        setForeground(col == 1 && "— No owner —".equals(val)
                                ? new Color(160, 160, 160) : Color.BLACK);
                        setFont(new Font("Arial", Font.PLAIN, 12));
                    }
                }
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(C_BORDER));
        return sp;
    }

    private JPanel buildSummary() {
        JPanel row = new JPanel(new GridLayout(1, 3, 12, 0));
        row.setBackground(C_BG);
        row.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));

        lblCollected = summaryCard("Collected this month", "Q 0.00", C_GREEN);
        lblExpected  = summaryCard("Expected this month",  "Q 0.00", new Color(60, 60, 60));
        lblPending   = summaryCard("Pending this month",   "Q 0.00", C_DANGER);

        row.add(lblCollected.getParent());
        row.add(lblExpected.getParent());
        row.add(lblPending.getParent());
        return row;
    }

    private JLabel summaryCard(String title, String value, Color accent) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("Arial", Font.BOLD, 11));
        t.setForeground(new Color(100, 100, 100));
        JLabel v = new JLabel(value, SwingConstants.CENTER);
        v.setFont(new Font("Arial", Font.BOLD, 14));
        v.setForeground(accent);
        card.add(t);
        card.add(v);
        return v;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
        p.setBackground(C_TOOLBAR);
        JButton btn = new JButton("Close");
        btn.setBackground(C_DANGER);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 32));
        btn.addActionListener(e -> dispose());
        p.add(btn);
        return p;
    }

    private void loadReport() {
        tableModel.setRowCount(0);
        double collected = 0;

        for (int i = 1; i <= Condominio.TOTAL_CASAS; i++) {
            Casa c = condominio.getCasa(i);
            String owner  = c.tienePropietario() ? c.getPropietario().getNombre() : "— No owner —";
            String status = !c.tienePropietario() ? "No owner"
                          : c.tienePago(currentMonth, currentYear) ? "Paid" : "Pending";
            String total  = c.tienePropietario()
                          ? String.format("Q %.2f", c.getTotalPagadoAnio(currentYear))
                          : "—";

            if (c.tienePago(currentMonth, currentYear)) {
                collected += condominio.getCuotaMensual();
            }

            tableModel.addRow(new Object[]{i, owner, status, total});
        }

        double expected = condominio.getTotalEsperado();
        lblCollected.setText(String.format("Q %.2f", collected));
        lblExpected.setText(String.format("Q %.2f", expected));
        lblPending.setText(String.format("Q %.2f", expected - collected));
    }

    private String getCurrentMonthName() {
        return LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}
