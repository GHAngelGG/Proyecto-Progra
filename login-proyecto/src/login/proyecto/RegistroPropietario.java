package login.proyecto;

import vistaverde.AppContext;
import vistaverde.model.Casa;
import vistaverde.model.Condominio;
import vistaverde.model.Propietario;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class RegistroPropietario extends JFrame {

    private static final Color C_HEADER    = new Color(28,  28,  28);
    private static final Color C_BG        = new Color(245, 245, 245);
    private static final Color C_TOOLBAR   = new Color(50,  50,  50);
    private static final Color C_BORDER    = new Color(200, 200, 200);
    private static final Color C_SAVE      = new Color(34,  139, 60);
    private static final Color C_DANGER    = new Color(160, 30,  30);
    private static final Color C_FREE      = new Color(140, 140, 140);
    private static final Color C_OCCUPIED  = new Color(34,  139, 60);
    private static final Color C_SELECTED  = new Color(210, 150, 0);

    private int selectedHouse = 0;
    private final JButton[] houseButtons = new JButton[30];
    private final Condominio condominio;

    private JTextField tfHouseNumber;
    private JTextField tfFirstNames;
    private JTextField tfLastNames;
    private JTextField tfPhone;
    private JTextField tfEmail;
    private JLabel     lblStatus;

    public RegistroPropietario(JFrame parent) {
        this.condominio = AppContext.getInstance().getCondominio();

        setTitle("Register Owner — Vista Verde");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildFooter(),  BorderLayout.SOUTH);

        setPreferredSize(new Dimension(780, 540));
        pack();
        setLocationRelativeTo(parent);

        refreshMapColors();
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_HEADER);
        p.setPreferredSize(new Dimension(0, 52));

        JLabel lbl = new JLabel("Register Owner", SwingConstants.CENTER);
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
            btn.setBackground(C_FREE);
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
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p.setBackground(C_BG);
        p.add(legendItem(C_FREE,     "Available"));
        p.add(legendItem(C_OCCUPIED, "Has owner"));
        p.add(legendItem(C_SELECTED, "Selected"));
        return p;
    }

    private JPanel legendItem(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setBackground(C_BG);
        JLabel square = new JLabel();
        square.setOpaque(true);
        square.setBackground(color);
        square.setPreferredSize(new Dimension(14, 14));
        p.add(square);
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

        JLabel title = new JLabel("Owner Information");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(28, 28, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(4));

        JLabel hint = new JLabel("Select an available house on the map.");
        hint.setFont(new Font("Arial", Font.PLAIN, 11));
        hint.setForeground(new Color(120, 120, 120));
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(hint);
        panel.add(Box.createVerticalStrut(18));

        tfHouseNumber = addField(panel, "House number:",              true);
        tfFirstNames  = addField(panel, "First names:",              false);
        tfLastNames   = addField(panel, "Last names:",               false);
        tfPhone       = addField(panel, "Phone (8 digits):",         false);
        tfEmail       = addField(panel, "Email address:",            false);
        
        //VALIDA MIENTRAS ESCRIBO
        // Only allow digits in phone field, max 8 characters
        ((AbstractDocument) tfPhone.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                    throws BadLocationException {
                String digits = text.replaceAll("[^0-9]", "");
                if (fb.getDocument().getLength() + digits.length() <= 8) {
                    super.insertString(fb, offset, digits, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String digits = text.replaceAll("[^0-9]", "");
                if (fb.getDocument().getLength() - length + digits.length() <= 8) {
                    super.replace(fb, offset, length, digits, attrs);
                }
            }
        });

        // Filtro de solo letras para nombres y apellidos
        DocumentFilter onlyLetters = new DocumentFilter() {
            private String clean(String s) {
                return s.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]", "");
            }
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                    throws BadLocationException {
                super.insertString(fb, offset, clean(text), attr);
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                super.replace(fb, offset, length, clean(text), attrs);
            }
        };
        ((AbstractDocument) tfFirstNames.getDocument()).setDocumentFilter(onlyLetters);
        ((AbstractDocument) tfLastNames.getDocument()).setDocumentFilter(onlyLetters);

        return panel;
    }

    private JTextField addField(JPanel parent, String label, boolean readOnly) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(new Color(40, 40, 40));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(4));

        JTextField tf = new JTextField();
        tf.setFont(new Font("Arial", Font.PLAIN, 13));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        if (readOnly) {
            tf.setEditable(false);
            tf.setBackground(new Color(235, 235, 235));
        }
        parent.add(tf);
        parent.add(Box.createVerticalStrut(14));
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

    private void onHouseClick(int number) {
        Casa house = condominio.getCasa(number);

        if (house.tienePropietario()) {
            setStatus("House " + number + " already has an owner: "
                    + house.getPropietario().getNombreCompleto() + ".", false);
            return;
        }

        if (selectedHouse == number) {
            houseButtons[number - 1].setBackground(C_FREE);
            selectedHouse = 0;
            tfHouseNumber.setText("");
            setStatus("Selection cleared.", true);
            return;
        }

        if (selectedHouse > 0) {
            houseButtons[selectedHouse - 1].setBackground(C_FREE);
        }

        selectedHouse = number;
        houseButtons[number - 1].setBackground(C_SELECTED);
        tfHouseNumber.setText(String.valueOf(number));
        setStatus("House " + number + " selected. Fill in the owner details.", true);
        tfFirstNames.requestFocus();
    }

    private void doRegister() {
        if (selectedHouse == 0) {
            setStatus("Please select an available house on the map.", false);
            return;
        }

        String nombres   = tfFirstNames.getText().trim();
        String apellidos = tfLastNames.getText().trim();
        String phone     = tfPhone.getText().trim();
        String email     = tfEmail.getText().trim();

        if (nombres.isEmpty()) {
            setStatus("First names are required.", false);
            tfFirstNames.requestFocus();
            return;
        }
        if (nombres.length() < 2) {
            setStatus("First names must be at least 2 characters.", false);
            tfFirstNames.requestFocus();
            return;
        }
        if (apellidos.isEmpty()) {
            setStatus("Last names are required.", false);
            tfLastNames.requestFocus();
            return;
        }
        if (apellidos.length() < 2) {
            setStatus("Last names must be at least 2 characters.", false);
            tfLastNames.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            setStatus("Phone number is required.", false);
            tfPhone.requestFocus();
            return;
        }
        if (phone.length() != 8) {
            setStatus("Phone must be exactly 8 digits (Guatemala format).", false);
            tfPhone.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            setStatus("Email address is required.", false);
            tfEmail.requestFocus();
            return;
        }
        if (!email.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
            setStatus("Invalid email. Use format: name@domain.com", false);
            tfEmail.requestFocus();
            return;
        }

        Propietario owner = new Propietario(nombres, apellidos, selectedHouse, phone, email);
        boolean success = condominio.registrarPropietario(owner);

        if (!success) {
            setStatus("House " + selectedHouse + " already has a registered owner.", false);
            return;
        }

        houseButtons[selectedHouse - 1].setBackground(C_OCCUPIED);
        setStatus("Owner registered successfully in house " + selectedHouse + ".", true);

        selectedHouse = 0;
        clearFields();
    }

    private void doClear() {
        if (selectedHouse > 0) {
            if (!condominio.getCasa(selectedHouse).tienePropietario()) {
                houseButtons[selectedHouse - 1].setBackground(C_FREE);
            }
            selectedHouse = 0;
        }
        clearFields();
        setStatus(" ", true);
    }

    private void clearFields() {
        tfHouseNumber.setText("");
        tfFirstNames.setText("");
        tfLastNames.setText("");
        tfPhone.setText("");
        tfEmail.setText("");
    }

    public void refreshMapColors() {
        for (int i = 0; i < 30; i++) {
            Casa house = condominio.getCasa(i + 1);
            if (selectedHouse == i + 1) {
                houseButtons[i].setBackground(C_SELECTED);
            } else if (house.tienePropietario()) {
                houseButtons[i].setBackground(C_OCCUPIED);
            } else {
                houseButtons[i].setBackground(C_FREE);
            }
        }
    }

    private void setStatus(String message, boolean success) {
        lblStatus.setText(message);
        lblStatus.setForeground(success
                ? new Color(120, 220, 120)
                : new Color(255, 110, 110));
    }
}
