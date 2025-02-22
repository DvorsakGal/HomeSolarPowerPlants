package si.um.feri.aiv;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import si.um.feri.aiv.dao.MSEDao;
import si.um.feri.aiv.dao.SkupnostDao;
import si.um.feri.aiv.dao.SkupnostMemoryDao;
import si.um.feri.aiv.vao.MalaSoncnaElektrarna;
import si.um.feri.aiv.vao.Skupnost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class SkupBean implements Skup{
    @EJB
    SkupnostDao dao;
    @EJB
    MSEDao mseDao;

    @Override
    public void Izpisi() {
        System.out.println("EJB zrno klicano");
    }

    @Override
    public double addition(double a, double b) {
        System.out.println("Skup -> addition");
        return a+b;
    }

    @Override
    public double vrniKapaciteto(String imeSkupnosti) {
        Skupnost sk = dao.find(imeSkupnosti);
        double kapaciteta = 0;
        if (sk != null) {
            List<MalaSoncnaElektrarna> mses = mseDao.getAll();
            for (MalaSoncnaElektrarna e : mses) {
                kapaciteta = kapaciteta + e.getZmogljivost();
            }
        }
        return kapaciteta;
    }

}
