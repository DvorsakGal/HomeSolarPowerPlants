package si.um.feri.aiv.observer;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailMessageService {

    private String subject;
    private String body;
    private String to;

    public Message build() throws MessagingException {
        Session session = Session.getDefaultInstance(System.getProperties(), null);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("gal.dvorsak@student.um.si"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        return message;
    }
}
