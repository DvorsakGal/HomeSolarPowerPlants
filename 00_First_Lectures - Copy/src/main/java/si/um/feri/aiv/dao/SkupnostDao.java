package si.um.feri.aiv.dao;

import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import si.um.feri.aiv.observer.SkupnostObserver;
import si.um.feri.aiv.vao.MalaSoncnaElektrarna;
import si.um.feri.aiv.vao.Skupnost;

import java.util.List;

@Local
public interface SkupnostDao {
    List<Skupnost> getAll();
    Skupnost find(String ime);
    Skupnost save(Skupnost skupnost);
    void delete(String ime);
//    void obvestiObstojeceClane();

    void shraniMSE(MalaSoncnaElektrarna mse, String imeSkupnosti);
    void izbrisiMSE(int idMSE,String imeSkupnosti);

    void addObserver(SkupnostObserver observer);
    void removeObserver(SkupnostObserver observer);
    void notifyObservers(MalaSoncnaElektrarna mse);

}
