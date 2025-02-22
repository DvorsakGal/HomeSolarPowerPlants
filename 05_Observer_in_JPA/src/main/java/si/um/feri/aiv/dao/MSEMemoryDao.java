package si.um.feri.aiv.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import si.um.feri.aiv.vao.MalaSoncnaElektrarna;
import si.um.feri.aiv.vao.Skupnost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class MSEMemoryDao implements MSEDao{

    Logger log=Logger.getLogger(MSEMemoryDao.class.toString());

    private List<MalaSoncnaElektrarna> elektrarne= Collections.synchronizedList(new ArrayList<MalaSoncnaElektrarna>());

    /*
    private static MSEMemoryDao instance = null;
    public static synchronized MSEMemoryDao getInstance() {
        if (instance==null) instance = new MSEMemoryDao();
        return instance;
    }

     */
    @PersistenceContext
    EntityManager em;

    public MSEMemoryDao() {

    }

//    @Override
//    public List<MalaSoncnaElektrarna> getAll() {
//        log.info("DAO-MSE: get all");
//        return elektrarne;
//    }

    @Override
    public List<MalaSoncnaElektrarna> getAll() {
        log.info("Ejb bean: get all");
        return em.createQuery("select elektrarne from MalaSoncnaElektrarna elektrarne").getResultList();
    }

//    @Override
//    public MalaSoncnaElektrarna find(String naziv) {
//        log.info("DAO-MSE: finding... " + naziv);
//        for (MalaSoncnaElektrarna e : elektrarne) {
//            if (e.getNaziv().equals(naziv)) {
//                return e;
//            }
//        }
//        return null;
//    }

    @Override
    public MalaSoncnaElektrarna find(String naziv) {
        log.info("DAO-MSE: finding... " + naziv);
        return em.find(MalaSoncnaElektrarna.class, naziv);
    }

//    @Override
//    public void save(MalaSoncnaElektrarna mse) {
//        log.info("DAO-MSE: saving... " + mse);
//        if(find(mse.getNaziv())!=null) {
//            log.info("DAO-MSE: editing... "+mse);
//            delete(mse.getNaziv());
//        }
//        elektrarne.add(mse);
//    }

    @Override
    public MalaSoncnaElektrarna save(MalaSoncnaElektrarna mse) {
        log.info("EjbBean: save");
        em.persist(mse);
        return mse;
    }

    @Override
    public void delete(String naziv) {
        log.info("DAO-MSE: deleting... "+naziv);
        for (Iterator<MalaSoncnaElektrarna> i = elektrarne.iterator(); i.hasNext();) {
            if (i.next().getNaziv().equals(naziv)) {
                i.remove();
            }
        }
    }

}
