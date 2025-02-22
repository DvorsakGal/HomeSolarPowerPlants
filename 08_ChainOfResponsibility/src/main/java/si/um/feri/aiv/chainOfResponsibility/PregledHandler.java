package si.um.feri.aiv.chainOfResponsibility;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import si.um.feri.aiv.vao.MalaSoncnaElektrarna;
import si.um.feri.aiv.vao.Skupnost;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PregledHandler extends Handler{

    private String subject;
    private String body;
    private String to;
    private Session mailSession;

    public PregledHandler() {
        try {
            InitialContext initialContext = new InitialContext();
            mailSession = (Session) initialContext.lookup("java:jboss/mail/MojMail");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleRequest(MalaSoncnaElektrarna mse) {
        System.out.println("Prejem zahteve PREGLED");
        LocalDate zadnjiPregled = mse.getDatumZadnjegaPregleda();
        LocalDate currentDate = LocalDate.now();
        long monthsSinceLastPregled = ChronoUnit.MONTHS.between(zadnjiPregled, currentDate);

        if (monthsSinceLastPregled >= 6 && monthsSinceLastPregled <= 10) {
            System.out.println("Približuje se pregled");
        } else if (monthsSinceLastPregled > 10) {
            System.out.println("POTREBEN PREGLED!");
            int toPregled = (int) (12-monthsSinceLastPregled);
            String emailBody = "Pregled izvedite v naslednjih " + toPregled + " mesecih.";
            sendEmail(mse, emailBody);
        }

        if (successor != null) {
            successor.handleRequest(mse);
        }
    }

    private void sendEmail(MalaSoncnaElektrarna mse, String body) {
        try {
            System.out.println("POSILJAM MAIL::::::::::::::::::::::::::::::::::::::::::::::::::::");
            Message message = new MimeMessage(mailSession);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mse.getEmail()));
            message.setSubject("🚨POTREBEN PREGLED!🚨");
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
