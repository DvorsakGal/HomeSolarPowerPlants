package si.um.feri.aiv.jsf;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import si.um.feri.aiv.dao.MSEDao;
import si.um.feri.aiv.dao.MSEMemoryDao;
import si.um.feri.aiv.dao.SkupnostDao;
import si.um.feri.aiv.dao.SkupnostMemoryDao;
import si.um.feri.aiv.observer.EmailServiceObserver;
import si.um.feri.aiv.observer.SkupnostObserver;
import si.um.feri.aiv.vao.MalaSoncnaElektrarna;
import si.um.feri.aiv.vao.Skupnost;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named("elektrarne")
@SessionScoped
public class MseJSFBean implements Serializable {

    private SkupnostObserver mailObserver;

    private static final long serialVersionUID = -5814108866690269293L;
    Logger log=Logger.getLogger(MseJSFBean.class.toString());

    @EJB
    MSEDao dao;
    //private MSEDao dao = new MSEMemoryDao();
    @Setter
    @Getter
    private MalaSoncnaElektrarna selectedMSE = new MalaSoncnaElektrarna();
    @Getter
    private String selectedNaziv;

    @EJB
    SkupnostDao daoSkupnost;
    @Getter
    @Setter
    private Skupnost selectedSkupnost = new Skupnost();
    @Getter
    private String selectedIme;
    @Getter
    private int selectedIdSkupnost;

    public MseJSFBean() {
        mailObserver = new EmailServiceObserver(selectedSkupnost.getEmailSkrbnika());
    }


//    public List<MalaSoncnaElektrarna> getAllMSEs(Skupnost skupnost) throws Exception {
//        selectedSkupnost = skupnost;
//        return dao.getAll(selectedSkupnost);
//    }
    public List<MalaSoncnaElektrarna> getAllMSEs() throws Exception {
        return dao.getAll();
    }
    public String saveMSE() throws Exception {
        dao.save(selectedMSE);
        return "seznamMSEjev";
    }
    public void deleteMSE(MalaSoncnaElektrarna mse) throws Exception {
        dao.delete(mse.getNaziv());
    }
    public void setSelectedNaziv(String naziv) throws Exception {
        selectedNaziv = naziv;
        selectedMSE = dao.find(naziv);
        if (selectedMSE == null) {
            selectedMSE = new MalaSoncnaElektrarna();
        }
    }



    public List<Skupnost> getAllSkupnosti() throws Exception {
        return daoSkupnost.getAll();
    }
    public String saveSkupnost() throws Exception {
        System.out.println("klico save");
        daoSkupnost.save(selectedSkupnost);
        return "all";
    }
    public void deleteSkupnost(Skupnost s) throws Exception {
        System.out.println("klicana delete metoda-----------------------------------");
        daoSkupnost.delete(s.getIme());
        addMessage("Confirmed", "Record deleted");
    }

    public void dodajMSE() {
        daoSkupnost.shraniMSE(new MalaSoncnaElektrarna(), selectedSkupnost.getIme());
        selectedSkupnost=daoSkupnost.find(selectedSkupnost.getIme());
    }

    public void shraniMSE(MalaSoncnaElektrarna mse) {
        mse.setUrejanje(false);
        daoSkupnost.shraniMSE(mse, selectedSkupnost.getIme());
        selectedSkupnost=daoSkupnost.find(selectedSkupnost.getIme());
    }

    public void izbrisiMSE(MalaSoncnaElektrarna mse) {
        daoSkupnost.izbrisiMSE(mse.getId(), selectedSkupnost.getIme());
        selectedSkupnost=daoSkupnost.find(selectedSkupnost.getIme());
    }

    public void urediMSE(MalaSoncnaElektrarna mse) {
        mse.setUrejanje(true);
        daoSkupnost.shraniMSE(mse, selectedSkupnost.getIme());
        selectedSkupnost=daoSkupnost.find(selectedSkupnost.getIme());
    }

    public void setSelectedIme(String ime) throws Exception {
        selectedIme = ime;
        selectedSkupnost = daoSkupnost.find(ime);
        if (selectedSkupnost == null) {
            selectedSkupnost = new Skupnost();
        }
    }

//    public void setSelectedIdSkupnost(int id) throws Exception {
//        selectedIdSkupnost = id;
//        selectedSkupnost = daoSkupnost.find(ime);
//        if (selectedSkupnost == null) {
//            selectedSkupnost = new Skupnost();
//        }
//    }

//PRIMEFACES
    public void confirm() {
        addMessage("Confirmed", "You have accepted");
    }
    public void delete() {
        addMessage("Confirmed", "Record deleted");
    }
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
