package si.um.feri.aiv.vao;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import si.um.feri.aiv.state.PolnaMoc;
import si.um.feri.aiv.state.StanjeElektrarne;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "elektrarne")
public class MalaSoncnaElektrarna implements Serializable {
    private static final long serialVersionUID = -6855513100564272903L;

//    public MalaSoncnaElektrarna() {
//        this("");
//        this.urejanje = true;
//    }
//    public MalaSoncnaElektrarna(String s) {
//        naziv = s;
//    }

//    @Id
//    @Column(name = "idElektrarna", nullable = false)
//    private int id=1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idElektrarna", nullable = false)
    private int id;
    private String naziv;
    private float zmogljivost;
    //private String lokacija;
    private double longitude;
    private double latitude;
    private String imeLastnika;
    private String priimekLastnika;
    private String email;
    //private Person lastnik;
    private LocalDateTime timestamp=LocalDateTime.now();
    @Transient
    private boolean urejanje;
    @Transient
    private StanjeElektrarne trenutnoStanje;


    public MalaSoncnaElektrarna(int id, String naziv, float zmogljivost, double longitude, double latitude, String imeLastnika, String priimekLastnika, String email) {
        this.id = id;
        this.naziv = naziv;
        this.zmogljivost = zmogljivost;
        this.longitude = longitude;
        this.latitude = latitude;
        this.imeLastnika = imeLastnika;
        this.priimekLastnika = priimekLastnika;
        this.email = email;
        this.trenutnoStanje = new PolnaMoc();
    }

    public MalaSoncnaElektrarna(int id) {
        this.id = id;
        this.urejanje = true;
        this.trenutnoStanje = new PolnaMoc();
    }

    public MalaSoncnaElektrarna() {
        this.trenutnoStanje = new PolnaMoc();
        this.urejanje = true;
    }


    //METODE GLEDE NA STANJE

    public void pocivaj() {
        this.setZmogljivost(0);
    }

    public void delajSPolovicnoMocjo() {
        this.setZmogljivost(this.zmogljivost / 2);
    }

    public void delajNaPolno() {
        this.setZmogljivost(this.zmogljivost);
    }

    public void ravnajGledeNaStanje() {
        if (trenutnoStanje!=null) trenutnoStanje.vrniStanje(this);
    }
}
