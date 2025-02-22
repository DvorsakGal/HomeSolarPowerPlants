package si.um.feri.aiv.observer;

import jakarta.mail.MessagingException;
import si.um.feri.aiv.vao.MalaSoncnaElektrarna;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers() throws MessagingException;
}
