package si.um.feri.aiv.observer;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import si.um.feri.aiv.vao.MalaSoncnaElektrarna;
import si.um.feri.aiv.vao.Skupnost;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EmailMessageService implements Observer {

    private String subject;
    private String body;
    private String to;
    private Session mailSession;

    public EmailMessageService() {
        try {
            InitialContext initialContext = new InitialContext();
            mailSession = (Session) initialContext.lookup("java:jboss/mail/MojMail");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public Message build() throws MessagingException {
        Session session = Session.getDefaultInstance(System.getProperties(), null);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("gal.dvorsak@student.um.si"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        return message;
    }

    private void sendEmail(Skupnost skupnost, MalaSoncnaElektrarna mse, MalaSoncnaElektrarna dodana) {
        try {
            System.out.println("POSILJAM MAIL::::::::::::::::::::::::::::::::::::::::::::::::::::");
            Message message = new MimeMessage(mailSession);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mse.getEmail()));
            message.setSubject("🚨Nova MSE je bila dodana v skupnost " + skupnost.getIme() +"!🚨");
            String emailBody = "Nova MSE je bila dodana v skupnost " + skupnost.getIme() + "." + "\n\nPodatki o MSE: "
                    + "\n\nID: " +dodana.getId() + "\n\nNaziv: " +dodana.getNaziv() + "\n\nIme: " + dodana.getImeLastnika() + "\nPriimek: " + dodana.getPriimekLastnika() +"\nEmail: " + dodana.getEmail()
                    +"\nZmogljivost v KW: " + dodana.getZmogljivost() + "\nZemljepisna Sirina: " + dodana.getLatitude() +"\nZemljepisna Dolzina: " + dodana.getLongitude();
            message.setText(emailBody);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o) throws MessagingException {
        System.out.println("KLICAN UPDATE");
        if (o instanceof Skupnost) {
            Skupnost skupnost = (Skupnost) o;
            //List<MalaSoncnaElektrarna> elektrarne = skupnost.getElektrarne();
            MalaSoncnaElektrarna dodanaElektrarna = skupnost.getElektrarne().getLast();
            System.out.println("ZADNJA ELEKTRARNA = " + dodanaElektrarna);
            for (MalaSoncnaElektrarna mse : skupnost.getElektrarne()) {
                System.out.println("Notifying " + mse.getNaziv());
                sendEmail(skupnost, mse, dodanaElektrarna);
            }
        }
    }
}
