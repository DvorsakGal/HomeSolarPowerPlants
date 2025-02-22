package si.um.feri.aiv.vao;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import si.um.feri.aiv.observer.SkupnostObserver;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "skupnosti")
public class Skupnost extends Observable implements Serializable {
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
    private List<SkupnostObserver> opazovalci = new ArrayList<>();
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

    public void addObserver(SkupnostObserver observer) {
        this.opazovalci.add(observer);
    }
    public void removeObserver(SkupnostObserver observer) {
        this.opazovalci.remove(observer);
    }
    //OBVESTI()
    public void notifyObservers(MalaSoncnaElektrarna mse) {
        for (SkupnostObserver observer : opazovalci) {
            observer.update(mse);
        }
    }

//    public void setStanje(String stanje) {
//        this.stanje = stanje;
//        for (Observer observer : this.opazovalci) {
//            observer.update(this.stanje);
//        }
//    }
}



