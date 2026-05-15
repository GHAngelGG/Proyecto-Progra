package vistaverde;

import vistaverde.model.Pago;
import vistaverde.model.Propietario;
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
 * Uses Gmail SMTP. The sender credentials must be configured below.
 * For Gmail you need an App Password (NOT your regular Gmail password):
 *   https://myaccount.google.com/apppasswords
 *
 * If credentials are not set, the email is silently skipped so the app
 * still works without crashing.
 */
public class EmailSender {

    // ── CONFIGURATION ─────────────────────────────────────────────────────────
    // Change these two lines with your Gmail and the 16-char app password.
    private static final String SENDER_EMAIL    = "joseangmil100@gmail.com";
    private static final String SENDER_APP_PASS = "your_app_password_here";

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    /** Sends a payment confirmation email to the given owner. */
    public static void sendPaymentConfirmation(Propietario owner, Pago pago) {
        // Skip if credentials are not configured yet
        if (SENDER_APP_PASS == null || SENDER_APP_PASS.equals("your_app_password_here")) {
            System.out.println("[EmailSender] App password not configured, skipping email.");
            return;
        }

        // Skip if owner has no email
        String to = owner.getCorreo();
        if (to == null || to.isEmpty()) {
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            SMTP_HOST);
        props.put("mail.smtp.port",            SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_APP_PASS);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SENDER_EMAIL, "Vista Verde Administration"));
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
