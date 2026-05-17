package vistaverde;

import vistaverde.model.Pago;
import vistaverde.model.Propietario;
import java.io.FileInputStream;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Sends a payment confirmation email to the owner.
 *
 * Reads sender credentials from "email.properties" in the project root.
 * The file is gitignored so the password never gets committed.
 *
 * Required keys:
 *   sender.email     = your.gmail@gmail.com
 *   sender.password  = 16-char Gmail app password (no spaces)
 *
 * If the file is missing or empty, the email is silently skipped.
 */
public class EmailSender {

    private static final String CONFIG_FILE = "email.properties";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    private static String senderEmail = null;
    private static String senderPass  = null;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            Properties cfg = new Properties();
            cfg.load(in);
            senderEmail = cfg.getProperty("sender.email");
            senderPass  = cfg.getProperty("sender.password");
        } catch (Exception e) {
            System.out.println("[EmailSender] email.properties not found, emails disabled.");
        }
    }

    /** Sends a payment confirmation email to the given owner. */
    public static void sendPaymentConfirmation(Propietario owner, Pago pago) {
        // Skip if credentials are not configured yet
        if (senderEmail == null || senderPass == null
                || senderEmail.isEmpty() || senderPass.isEmpty()) {
            System.out.println("[EmailSender] Credenciales no configuradas — saltando envio.");
            return;
        }

        // Skip if owner has no email
        String to = owner.getCorreo();
        if (to == null || to.isEmpty()) {
            System.out.println("[EmailSender] Propietario sin correo — saltando envio.");
            return;
        }

        System.out.println("[EmailSender] Intentando enviar correo a " + to + "...");

        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            SMTP_HOST);
        props.put("mail.smtp.port",            SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPass);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmail, "Vista Verde Administration"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("Payment Confirmation — Vista Verde");
            msg.setContent(buildBody(owner, pago), "text/html; charset=utf-8");

            Transport.send(msg);
            System.out.println("[EmailSender] Email sent to " + to);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            // Do not crash the app if email fails (e.g. no internet)
            System.err.println("[EmailSender] Could not send email: " + e.getMessage());
        }
    }

    private static String buildBody(Propietario owner, Pago pago) {
        return "<div style='font-family: Arial, sans-serif; max-width: 560px;'>"
             + "<div style='background:#1c1c1c; color:white; padding:16px; text-align:center;'>"
             + "<h2 style='margin:0;'>Vista Verde Condominium</h2>"
             + "<p style='margin:4px 0 0 0; color:#bbb;'>Payment Receipt</p>"
             + "</div>"
             + "<div style='padding:20px; background:#f5f5f5;'>"
             + "<p>Dear <b>" + owner.getNombre() + "</b>,</p>"
             + "<p>We confirm that your monthly fee payment has been registered:</p>"
             + "<table style='border-collapse:collapse; width:100%; background:white; margin:10px 0;'>"
             + row("House number", String.valueOf(owner.getNumeroCasa()))
             + row("Month",        pago.getNombreMes())
             + row("Year",         String.valueOf(pago.getAnio()))
             + row("Amount paid",  String.format("Q %.2f", pago.getMonto()))
             + "</table>"
             + "<p style='color:#666; font-size:12px;'>This is an automatic message. "
             + "Please keep this email as proof of payment.</p>"
             + "</div></div>";
    }

    private static String row(String label, String value) {
        return "<tr>"
             + "<td style='padding:8px 12px; border-bottom:1px solid #ddd; color:#666;'>" + label + "</td>"
             + "<td style='padding:8px 12px; border-bottom:1px solid #ddd;'><b>" + value + "</b></td>"
             + "</tr>";
    }
}
