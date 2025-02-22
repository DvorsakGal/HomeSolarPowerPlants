package si.um.feri.aiv;

import jakarta.ejb.Remote;
import si.um.feri.aiv.vao.Skupnost;

import java.util.List;

@Remote
public interface Skup {
    void Izpisi();
    double addition(double a, double b);
    double vrniKapaciteto(String imeSkupnosti);
}
