package si.um.feri.aiv.jsf;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.Setter;
import si.um.feri.aiv.dao.MSEDao;
import si.um.feri.aiv.dao.SkupnostDao;
import si.um.feri.aiv.observer.EmailMessageService;
import si.um.feri.aiv.observer.Observer;
import si.um.feri.aiv.vao.MalaSoncnaElektrarna;
import si.um.feri.aiv.vao.Skupnost;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named("elektrarne")
@SessionScoped
public class MseJSFBean implements Serializable {

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
    private Skupnost selectedSkupnost = new Skupnost();
    @Getter
    private String selectedIme;
    @Getter
    private int selectedIdSkupnost;

    private EmailMessageService emailNotifier = new EmailMessageService();

    public MseJSFBean() {
        //emailObserver = new EmailMessageService();
        System.out.println("KLICAN KONSTRUKTOR ZA JSF BEAN");
        System.out.println(selectedSkupnost);
        selectedSkupnost.addObserver(emailNotifier);
        System.out.println(selectedSkupnost);   //Observer se doda!!!
    }
//    public MSEJSFBean() {
//        emailObserver = new EmailMessageService();
//    }


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
        selectedSkupnost.addObserver(emailNotifier);
        daoSkupnost.save(selectedSkupnost);
        return "all";
    }
    public void deleteSkupnost(Skupnost s) throws Exception {
        System.out.println("klicana delete metoda-----------------------------------");
        daoSkupnost.delete(s.getId());
        addMessage("Confirmed", "Record deleted");
    }

//    public void dodajMSE() {
//        daoSkupnost.shraniMSE(new MalaSoncnaElektrarna(), selectedSkupnost.getId());
//        selectedSkupnost=daoSkupnost.find(selectedSkupnost.getId());
//    }

    public void shraniMSE(MalaSoncnaElektrarna mse, int idSkupnosti) throws MessagingException {
        //mse.setUrejanje(false);
        System.out.println("ID skupnosti: " + idSkupnosti + ":::::::::::::::::::::::: ID mse: " + mse.getId());
        System.out.println(selectedSkupnost);
        daoSkupnost.shraniMSEdva(mse, idSkupnosti);

        selectedSkupnost=daoSkupnost.find(selectedSkupnost.getId());
        selectedSkupnost.addObserver(emailNotifier); // Re-add observer
        setSelectedMSE(new MalaSoncnaElektrarna());
    }

    public void izbrisiMSE(MalaSoncnaElektrarna mse) {
        daoSkupnost.izbrisiMSE(mse.getId(), selectedSkupnost.getId());
        selectedSkupnost=daoSkupnost.find(selectedSkupnost.getId());
    }

    public void urediMSE(MalaSoncnaElektrarna mse) throws MessagingException {
        mse.setUrejanje(true);
        daoSkupnost.shraniMSE(mse.getId(), selectedSkupnost.getId());
        selectedSkupnost=daoSkupnost.find(selectedSkupnost.getIme());
    }

    public void setSelectedIme(String ime) throws Exception {
        selectedIme = ime;
        selectedSkupnost = daoSkupnost.find(ime);
        selectedSkupnost.addObserver(emailNotifier);
        if (selectedSkupnost == null) {
            selectedSkupnost = new Skupnost();
        }
    }

    public void setSelectedIdSkupnost(int id) throws Exception {
        selectedIdSkupnost = id;
        selectedSkupnost = daoSkupnost.find(id);
        selectedSkupnost.addObserver(emailNotifier);
        if (selectedSkupnost == null) {
            selectedSkupnost = new Skupnost();
        }
    }

    public void setSelectedSkupnost(Skupnost selectedSkupnost) {
        selectedSkupnost.addObserver(emailNotifier);
        this.selectedSkupnost = selectedSkupnost;
    }

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
