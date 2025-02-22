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
    Skupnost find(int id);
    Skupnost save(Skupnost skupnost);
    void delete(int id);
//    void obvestiObstojeceClane();

    void shraniMSEdva(MalaSoncnaElektrarna mse, int idSkupnosti);
    MalaSoncnaElektrarna findMSE(int id);
    void shraniMSE(int mseID, int idSkupnosti);
    void izbrisiMSE(int idMSE, int idSkupnosti);

    void addObserver(SkupnostObserver observer);
    void removeObserver(SkupnostObserver observer);
    void notifyObservers(MalaSoncnaElektrarna mse);

}
