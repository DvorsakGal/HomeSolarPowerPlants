package si.um.feri.aiv.vao;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Observable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "elektrarne")
public class MalaSoncnaElektrarna implements Serializable {
    private static final long serialVersionUID = -6855513100564272903L;

    public MalaSoncnaElektrarna() {
        this("");
    }
    public MalaSoncnaElektrarna(String s) {
        naziv = s;
    }

    @Id
    @Column(name = "idElektrarna", nullable = false)
    private int id=-1;
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
    private boolean urejanje;
    private String stanje;


    public MalaSoncnaElektrarna(String naziv, float zmogljivost, double longitude, double latitude, String imeLastnika, String priimekLastnika, String email) {
        this.naziv = naziv;
        this.zmogljivost = zmogljivost;
        this.longitude = longitude;
        this.latitude = latitude;
        this.imeLastnika = imeLastnika;
        this.priimekLastnika = priimekLastnika;
        this.email = email;
    }

}
