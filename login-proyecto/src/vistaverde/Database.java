package vistaverde;

import vistaverde.model.Casa;
import vistaverde.model.Condominio;
import vistaverde.model.Pago;
import vistaverde.model.Propietario;
import java.sql.*;

/**
 * SQLite persistence layer for Vista Verde.
 *
 * Stores the data in a local file "vistaverde.db" in the project root.
 * On startup, loads all owners, payments and the current fee into the
 * in-memory Condominio. After each user action (register owner, register
 * payment, change fee), the corresponding method in this class is called
 * so the change is written immediately to disk.
 *
 * No data is lost when the app closes.
 */
public class Database {

    private static final String DB_FILE = "vistaverde.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    /** Opens a fresh connection. Caller is responsible for closing it. */
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Creates the tables if they do not exist yet. Called once at startup. */
    public static void init() {
        String createOwners =
            "CREATE TABLE IF NOT EXISTS propietarios (" +
            "  numero_casa INTEGER PRIMARY KEY," +
            "  nombres TEXT NOT NULL," +
            "  apellidos TEXT NOT NULL," +
            "  telefono TEXT," +
            "  email TEXT" +
            ")";

        String createPayments =
            "CREATE TABLE IF NOT EXISTS pagos (" +
            "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  numero_casa INTEGER NOT NULL," +
            "  mes INTEGER NOT NULL," +
            "  anio INTEGER NOT NULL," +
            "  monto REAL NOT NULL," +
            "  UNIQUE(numero_casa, mes, anio)" +
            ")";

        String createConfig =
            "CREATE TABLE IF NOT EXISTS config (" +
            "  clave TEXT PRIMARY KEY," +
            "  valor TEXT NOT NULL" +
            ")";

        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute(createOwners);
            s.execute(createPayments);
            s.execute(createConfig);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /** Loads everything from disk into the given Condominio instance. */
    public static void loadInto(Condominio condominio) {
        // 1. Load monthly fee
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement("SELECT valor FROM config WHERE clave = ?")) {
            ps.setString(1, "cuota_mensual");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    condominio.setCuotaMensual(Double.parseDouble(rs.getString("valor")));
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error loading fee: " + e.getMessage());
        }

        // 2. Load owners
        try (Connection c = connect();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM propietarios")) {
            while (rs.next()) {
                int numero       = rs.getInt("numero_casa");
                String nombres   = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String tel       = rs.getString("telefono");
                String mail      = rs.getString("email");
                Propietario p = new Propietario(nombres, apellidos, numero, tel, mail);
                condominio.getCasa(numero).setPropietario(p);
            }
        } catch (SQLException e) {
            System.err.println("Error loading owners: " + e.getMessage());
        }

        // 3. Load payments
        try (Connection c = connect();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM pagos ORDER BY anio, mes")) {
            while (rs.next()) {
                int casa   = rs.getInt("numero_casa");
                int mes    = rs.getInt("mes");
                int anio   = rs.getInt("anio");
                double mon = rs.getDouble("monto");
                condominio.getCasa(casa).agregarPago(new Pago(mes, anio, mon));
            }
        } catch (SQLException e) {
            System.err.println("Error loading payments: " + e.getMessage());
        }
    }

    /** Saves a newly registered owner. */
    public static void saveOwner(Propietario p) {
        String sql = "INSERT OR REPLACE INTO propietarios " +
                     "(numero_casa, nombres, apellidos, telefono, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, p.getNumeroCasa());
            ps.setString(2, p.getNombres());
            ps.setString(3, p.getApellidos());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getCorreo());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving owner: " + e.getMessage());
        }
    }

    /** Saves a newly registered payment for a given house. */
    public static void savePayment(int numeroCasa, Pago pago) {
        String sql = "INSERT INTO pagos (numero_casa, mes, anio, monto) VALUES (?, ?, ?, ?)";
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, numeroCasa);
            ps.setInt(2, pago.getMes());
            ps.setInt(3, pago.getAnio());
            ps.setDouble(4, pago.getMonto());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving payment: " + e.getMessage());
        }
    }

    /** Saves the current monthly fee. */
    public static void saveFee(double cuota) {
        String sql = "INSERT OR REPLACE INTO config (clave, valor) VALUES (?, ?)";
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "cuota_mensual");
            ps.setString(2, String.valueOf(cuota));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving fee: " + e.getMessage());
        }
    }
}
