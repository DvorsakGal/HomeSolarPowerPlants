package si.um.feri.aiv.jsf;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.Serializable;
import java.util.EventListener;

@Named("email")
@SessionScoped
@Getter
@Setter
public class Email implements Serializable {

    private static final long serialVersionUID = -275099322001980924L;

    InitialContext initialContext = new InitialContext();
    private Session mySession = (Session) initialContext.lookup("java:jboss/mail/MojMail");

    private String to = "gal.dvorsak2002@gmail.com";    //gal.dvorsak@student.um.si

    private String from = "gal.dvorsak@student.um.si";

    private String subject = "Test3";

    private String body = "testing testing 3.0...";

    private String pop3User = "gal.dvorsak2002@gmail.com";  //gal.dvorsak@student.um.si

    private String pop3Password = "novogeslo10";    //Dvorsak20

    private String pop3Emails;

    private String imapEmails;

    public Email() throws NamingException {
    }


    public void send() {
        try {
            Message message = new MimeMessage(mySession);
            message.setFrom(new InternetAddress(from));
            Address toAddress = new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            Transport.send(message);

            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Email sent to " + to);
            context.addMessage(null, facesMessage);
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error sending the Email. " + e.getMessage());
            context.addMessage(null, facesMessage);
        }
    }

    public void resetSmtp() {
        from = "gal.dvorsak@student.um.si";
        to = "gal.dvorsak@student.um.si";
        subject = null;
        body = null;
    }

    public void retrievePop3() throws Exception {
        try {
            pop3Emails = retrieveEmails("pop3", pop3User, pop3Password);
            if (pop3Emails == null) {
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage facesMessage = new FacesMessage("No message found.");
                context.addMessage(null, facesMessage);
            }
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error retrieving emails using POP3. " + e.getMessage());
            context.addMessage(null, facesMessage);
        }
    }

    public void resetPop3() {
        pop3User = "gal.dvorsak@student.um.si";
        pop3Password = "Dvorsak20";
        pop3Emails = null;
    }

    public void retrieveImap() throws Exception {
        try {
            imapEmails = retrieveEmails("imap");
            if (imapEmails == null) {
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage facesMessage = new FacesMessage("No message found.");
                context.addMessage(null, facesMessage);
            }
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error retrieving emails using IMAP. " + e.getMessage());
            context.addMessage(null, facesMessage);
        }
    }

    public void resetImap() {
        imapEmails = null;
    }

    private String retrieveEmails(String protocol) throws MessagingException, IOException {
        return retrieveEmails(protocol, null, null);
    }

    private String retrieveEmails(String protocol, String user, String password) throws MessagingException, IOException {
        Store store = mySession.getStore(protocol);
        if (user != null && !user.trim().isEmpty()) {
            store.connect(user, password);
        } else {
            // Users the credentials configured in the Mail Subsystem
            store.connect();
        }

        Folder inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_ONLY);

        // get the list of inbox messages
        Message[] messages = inbox.getMessages();

        if (messages.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder("Emails retrieved via ").append(protocol).append("\n");
        for (int i = 0; i < messages.length; i++) {
            // stop after listing ten messages
            if (i > 10) {
                inbox.close(true);
                store.close();
            }

            sb.append("Message ").append((i + 1)).append("\n");
            sb.append("From : ").append(messages[i].getFrom()[0]).append("\n");
            sb.append("Subject : ").append(messages[i].getSubject()).append("\n");
            sb.append("Body : ").append(messages[i].getContent().toString()).append("\n");
            sb.append("Sent Date : ").append(messages[i].getSentDate()).append("\n");
            sb.append("----------------------------------").append("\n");
        }

        inbox.close(true);
        store.close();

        return sb.toString();
    }

}
