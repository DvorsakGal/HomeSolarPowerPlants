package si.um.feri.aiv.vao;

import jakarta.mail.MessagingException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import si.um.feri.aiv.observer.Observable;
import si.um.feri.aiv.observer.Observer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "skupnosti")
public class Skupnost implements Serializable, Observable {
    private static final long serialVersionUID = -1069198470521711077L;
    @NotBlank
    private String ime;
    private String imeSkrbnika;
    private String priimekSkrbnika;
    private String emailSkrbnika;
    @OneToMany(fetch = FetchType.EAGER)
    private List<MalaSoncnaElektrarna> elektrarne;
    private LocalDateTime timestamp=LocalDateTime.now();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSkupnost", nullable = false)
    private int id;

    @Transient
    private List<Observer> opazovalci = new ArrayList<>();
//    private final NotificationService notificationService;
//    private List<Observer> opazovalci = new ArrayList<>();
//    private String stanje;

//    public Skupnost() {
//        notificationService = new NotificationService();
//    }

    //public Skupnost() {
        //this("", "", "", "");
    //}

    public Skupnost(String ime, String imeSkrbnika, String priimekSkrbnika, String emailSkrbnika) {
        this.ime = ime;
        this.imeSkrbnika = imeSkrbnika;
        this.priimekSkrbnika = priimekSkrbnika;
        this.emailSkrbnika = emailSkrbnika;
        this.elektrarne = new ArrayList<>();
        //notificationService = new NotificationService();
    }

    public Skupnost(String ime, String imeSkrbnika, String priimekSkrbnika, String emailSkrbnika, List<MalaSoncnaElektrarna> elektrarne) {
        this.ime = ime;
        this.imeSkrbnika = imeSkrbnika;
        this.priimekSkrbnika = priimekSkrbnika;
        this.emailSkrbnika = emailSkrbnika;
        this.elektrarne = elektrarne;
        //notificationService = new NotificationService();
    }

    public synchronized int getNaslednjiIdMSE() {
        int maxId=1;
        for (MalaSoncnaElektrarna mse : getElektrarne())
            if (mse.getId()>=maxId) maxId=mse.getId();
        return maxId+1;
    }

    @Override
    public void addObserver(Observer observer) {
        this.opazovalci.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.opazovalci.remove(observer);
    }

    @Override
    public void notifyObservers() throws MessagingException {
        System.out.println("KLICANA NOTIFY V SKUPNOSTI");
        System.out.println("Checkpoint 1------------------------------");
        for (Observer observer : opazovalci) {
            System.out.println(observer + " ");
        }
        System.out.println("Checkpoint 2------------------------------");
        for (Observer observer : opazovalci) {
            observer.update(this);
        }
        System.out.println("Checkpoint 3------------------------------");
    }


//    public void setStanje(String stanje) {
//        this.stanje = stanje;
//        for (Observer observer : this.opazovalci) {
//            observer.update(this.stanje);
//        }
//    }


}



