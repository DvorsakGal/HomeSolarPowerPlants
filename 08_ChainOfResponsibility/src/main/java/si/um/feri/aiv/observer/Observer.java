package si.um.feri.aiv.observer;

import jakarta.mail.MessagingException;

public interface Observer {

    void update(Observable o) throws MessagingException;
}
