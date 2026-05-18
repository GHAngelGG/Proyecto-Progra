package ui;

import logic.AppContext;
import model.Casa;
import model.Condominio;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class RegisterPayment extends JFrame {

    private static final Color C_HEADER   = new Color(28,  28,  28);
    private static final Color C_BG       = new Color(245, 245, 245);
    private static final Color C_TOOLBAR  = new Color(50,  50,  50);
    private static final Color C_BORDER   = new Color(200, 200, 200);
    private static final Color C_SAVE     = new Color(34,  139, 60);
    private static final Color C_DANGER   = new Color(160, 30,  30);
    private static final Color C_NO_OWNER = new Color(140, 140, 140);
    private static final Color C_PAID_UP  = new Color(34,  139, 60);
    private static final Color C_PENDING  = new Color(200, 70,  40);
    private static final Color C_SELECTED = new Color(210, 150, 0);

    private static final String[] MONTHS = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    private int selectedHouse = 0;
    private final JButton[] houseButtons = new JButton[30];
    private final Condominio condominio;
    private final int currentMonth;
    private final int currentYear;

    private JTextField tfHouseNumber;
    private JTextField tfOwnerName;
    private JComboBox<String> cbMonth;
    private JSpinner spYear;
    private JTextField tfFee;
    private JTextArea taHistory;
    private JLabel lblStatus;

    public RegisterPayment(JFrame parent) {
        this.condominio   = AppContext.getInstance().getCondominio();
        this.currentMonth = LocalDate.now().getMonthValue();
        this.currentYear  = LocalDate.now().getYear();

        setTitle("Register Payment — Vista Verde");
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

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_HEADER);
        p.setPreferredSize(new Dimension(0, 52));
        JLabel lbl = new JLabel("Register Payment", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 17));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildCenter() {
        JPanel p = new JPanel(new GridLayout(1, 2, 16, 0));
        p.setBackground(C_BG);
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        p.add(buildMapPanel());
        p.add(buildFormPanel());
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
        p.add(legendItem(C_PAID_UP,  "Paid up"));
        p.add(legendItem(C_PENDING,  "Pending"));
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

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        addSectionTitle(panel, "House Information");
        panel.add(Box.createVerticalStrut(10));
        tfHouseNumber = addReadOnlyField(panel, "House number:");
        tfOwnerName   = addReadOnlyField(panel, "Owner:");

        panel.add(Box.createVerticalStrut(6));
        addSectionTitle(panel, "Payment Details");
        panel.add(Box.createVerticalStrut(10));

        // Month
        addLabel(panel, "Month:");
        cbMonth = new JComboBox<>(MONTHS);
        cbMonth.setSelectedIndex(currentMonth - 1);
        cbMonth.setFont(new Font("Arial", Font.PLAIN, 13));
        cbMonth.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        cbMonth.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(cbMonth);
        panel.add(Box.createVerticalStrut(12));

        // Year
        addLabel(panel, "Year:");
        spYear = new JSpinner(new SpinnerNumberModel(currentYear, 2024, currentYear, 1));
        spYear.setFont(new Font("Arial", Font.PLAIN, 13));
        spYear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        spYear.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(spYear);
        panel.add(Box.createVerticalStrut(12));

        // Fee
        tfFee = addReadOnlyField(panel, "Monthly fee:");
        tfFee.setText(String.format("Q %.2f", condominio.getCuotaMensual()));

        // Payment history
        panel.add(Box.createVerticalStrut(4));
        addSectionTitle(panel, "Paid months this year:");
        panel.add(Box.createVerticalStrut(6));

        taHistory = new JTextArea(3, 20);
        taHistory.setEditable(false);
        taHistory.setFont(new Font("Arial", Font.PLAIN, 11));
        taHistory.setBackground(new Color(245, 245, 245));
        taHistory.setWrapStyleWord(true);
        taHistory.setLineWrap(true);
        JScrollPane sp = new JScrollPane(taHistory);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        sp.setBorder(BorderFactory.createLineBorder(C_BORDER));
        panel.add(sp);

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
        parent.add(Box.createVerticalStrut(12));
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

        JButton btnRegister = makeBtn("Register", C_SAVE);
        JButton btnClear    = makeBtn("Clear",    new Color(100, 100, 100));
        JButton btnClose    = makeBtn("Close",    C_DANGER);

        btnRegister.addActionListener(e -> doRegister());
        btnClear.addActionListener(e -> doClear());
        btnClose.addActionListener(e -> dispose());

        btns.add(btnRegister);
        btns.add(btnClear);
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
            clearForm();
            setStatus("Selection cleared.", true);
            return;
        }

        selectedHouse = number;
        houseButtons[number - 1].setBackground(C_SELECTED);
        tfHouseNumber.setText(String.valueOf(number));
        tfOwnerName.setText(house.getPropietario().getNombre());
        updateHistory(house);
        setStatus("House " + number + " selected. Choose month and year, then click Register.", true);
    }

    private void doRegister() {
        if (selectedHouse == 0) {
            setStatus("Please select a house on the map first.", false);
            return;
        }

        int month = cbMonth.getSelectedIndex() + 1;
        int year  = (int) spYear.getValue();

        String result = condominio.registrarPago(selectedHouse, month, year);

        if ("OK".equals(result)) {
            refreshButtonColor(selectedHouse);
            updateHistory(condominio.getCasa(selectedHouse));
            setStatus("Payment registered: House " + selectedHouse
                    + " — " + MONTHS[month - 1] + " " + year + ".", true);
        } else {
            setStatus(result, false);
        }
    }

    private void doClear() {
        if (selectedHouse > 0) {
            refreshButtonColor(selectedHouse);
            selectedHouse = 0;
        }
        clearForm();
        setStatus(" ", true);
    }

    private void clearForm() {
        tfHouseNumber.setText("");
        tfOwnerName.setText("");
        taHistory.setText("");
    }

    private void updateHistory(Casa house) {
        java.util.ArrayList<Integer> paid = house.getMesesPagados(currentYear);
        if (paid.isEmpty()) {
            taHistory.setText("No payments recorded for " + currentYear + ".");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int m : paid) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(MONTHS[m - 1]);
            }
            taHistory.setText(sb.toString());
        }
    }

    public void refreshMapColors() {
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
        Color color;
        if (!house.tienePropietario()) {
            color = C_NO_OWNER;
        } else if (house.tienePago(currentMonth, currentYear)) {
            color = C_PAID_UP;
        } else {
            color = C_PENDING;
        }
        houseButtons[number - 1].setBackground(color);
    }

    private void setStatus(String message, boolean success) {
        lblStatus.setText(message);
        lblStatus.setForeground(success
                ? new Color(120, 220, 120)
                : new Color(255, 110, 110));
    }
}
