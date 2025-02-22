package si.um.feri.aiv.dao;

import jakarta.ejb.Local;
import jakarta.mail.MessagingException;
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

    void shraniMSEdva(MalaSoncnaElektrarna mse, int idSkupnosti) throws MessagingException;
    MalaSoncnaElektrarna findMSE(int id);
    void shraniMSE(int mseID, int idSkupnosti) throws MessagingException;
    void izbrisiMSE(int idMSE, int idSkupnosti);
    float vrniKapaciteto(Skupnost skupnost);
}
