package si.um.feri.aiv.dao;

import jakarta.ejb.Stateless;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import si.um.feri.aiv.observer.EmailMessageService;
import si.um.feri.aiv.observer.Observer;
import si.um.feri.aiv.vao.MalaSoncnaElektrarna;
import si.um.feri.aiv.vao.Skupnost;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class SkupnostMemoryDao implements SkupnostDao, SkupnostDaoRemote{

    Logger log=Logger.getLogger(SkupnostMemoryDao.class.toString());

    private List<Skupnost> skupnosti= Collections.synchronizedList(new ArrayList<Skupnost>());

    private List<Observer> opazovalci = new ArrayList<>();

    private Session mailSession;

    private static final String SKUPNOST_MAIL = "gal.dvorsak2002@gmail.com";
    private EmailMessageService emailNotifier = new EmailMessageService();

    @PersistenceContext
    EntityManager em;

    /*
    private static SkupnostMemoryDao instance = null;
    public static synchronized SkupnostMemoryDao getInstance() {
        if (instance==null) instance = new SkupnostMemoryDao();
        return instance;
    }

     */

    public SkupnostMemoryDao() throws NamingException {
        try {
            log.info("Klico si skupnost konstruktor... " + this);
            InitialContext initialContext = new InitialContext();
            mailSession = (Session) initialContext.lookup("java:jboss/mail/MojMail");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }


    }

//    @Override
//    public List<Skupnost> getAll() {
//        log.info("DAO-Skupnost: get all");
//        return skupnosti;
//    }

    @Override
    public List<Skupnost> getAll() {
        log.info("Ejb bean: get all");
        return em.createQuery("select skupnosti from Skupnost skupnosti").getResultList();
    }

//    @Override
//    public Skupnost find(String ime) {
//        log.info("DAO-Skupnost: finding... " + ime);
//        for (Skupnost s : skupnosti) {
//            if (s.getIme().equals(ime)) {
//                return s;
//            }
//        }
//        return null;
//    }

    @Override
    public Skupnost find(String ime) {
        log.info("DAO-MSE: finding... " + ime);
        return em.find(Skupnost.class, ime);
    }

    @Override
    public Skupnost find(int id) {
        log.info("DAO-MSE: finding... " + id);
        return em.find(Skupnost.class, id);
    }

//    @Override
//    public void save(Skupnost skupnost) {
//        log.info("DAO-Skupnost: saving... "+skupnost);
//        if (find(skupnost.getIme()) != null) {
//            log.info("MSE-Skupnost: editing... "+skupnost); //TUKAJ VIDIS DA CE HOCES DODAT SKUPNOST Z ISTIM IMENOM BO STARO IZBRISO IN NAREDO NOVO!!!
//            delete(skupnost.getIme());
//        }
//        skupnosti.add(skupnost);
//    }

    @Override
    public Skupnost save(Skupnost skupnost) {
        log.info("EjbBean: save");
        em.persist(skupnost);
        return skupnost;
    }

    @Override
    public void delete(int id) {
        log.info("DAO-Skupnost: deleting skupnost z id-jem... "+ id);
        Skupnost zaZbrisat = find(id);
        em.remove(zaZbrisat);
    }

//    @Override
//    public void delete(String ime) {
//        log.info("DAO-Skupnost: deleting... "+ime);
//        for (Iterator<Skupnost> s = skupnosti.iterator(); s.hasNext();) {
//            if (s.next().getIme().equals(ime)) {
//                s.remove();
//            }
//        }
//    }

    @Override
    public void shraniMSEdva(MalaSoncnaElektrarna mse, int idSkupnosti) throws MessagingException {
        log.info("Skupnost DAO: shranjujem mse v skupnost...");
        Skupnost najdena = find(idSkupnosti);   //ta klic vrne skupnost brez observerjev!!!!!!!!!!!!!!!!!!!!!!!!
        if (najdena==null) return;
        System.out.println("PRED DODAJANJEM OBSERVERJA:::::::::::::::::::::::::::::::::::::::::");
        System.out.println(najdena);
        najdena.addObserver(emailNotifier);
        System.out.println("PO DODAJANJU OBSERVERJA:::::::::::::::::::::::::::::::::::::::::");
        System.out.println(najdena);
        //if (mse.getId()<0) mse.setId(najdena.getNaslednjiIdMSE());
        System.out.println("ID skupnosti: " + idSkupnosti);
        System.out.println(mse);
        //izbrisiMSE(mse.getId(), najdena.getIme());
        najdena.getElektrarne().add(mse);
        em.persist(mse);
        //sendEmail(najdena, mse);
        najdena.notifyObservers();
    }

    @Override
    public void shraniMSE(int mseID, int idSkupnosti) throws MessagingException {
        log.info("Skupnost DAO: shranjujem mse v skupnost...");
        Skupnost najdena = find(idSkupnosti);
        MalaSoncnaElektrarna mse = new MalaSoncnaElektrarna(mseID);
        if (najdena==null) return;
        if (mseID<=0) mse.setId(najdena.getNaslednjiIdMSE());
        System.out.println("ID skupnosti: " + idSkupnosti);
        System.out.println(mse);
        //izbrisiMSE(mse.getId(), najdena.getIme());
        najdena.getElektrarne().add(mse);
        najdena.notifyObservers();
        //sendEmail(najdena, mse);
        //notifyObservers(mse);
    }

    @Override
    public MalaSoncnaElektrarna findMSE(int id) {
        log.info("DAO-MSE: finding malo elektrarno z id-jem... " + id);
        return em.find(MalaSoncnaElektrarna.class, id);
    }

    @Override
    public void izbrisiMSE(int idMSE, int idSkupnosti) {
        log.info("Skupnost DAO: brisem elektrarno iz skupnosti");
        Skupnost najdena = find(idSkupnosti);
        if (najdena==null) return;
        MalaSoncnaElektrarna zaZbrisat = findMSE(idMSE);
        if (zaZbrisat==null) return;
        //prvo zbrisi v kolekciji
        najdena.getElektrarne().remove(zaZbrisat);
        em.merge(najdena);
        em.remove(zaZbrisat);
//        for (Iterator<MalaSoncnaElektrarna> i = najdena.getElektrarne().iterator(); i.hasNext();) {
//            if (i.next().getId()==idMSE) {
//                i.remove();
//            }
//        }
    }



//    private void sendEmail(Skupnost skupnost, MalaSoncnaElektrarna mse) {
//        try {
//            Message message = new MimeMessage(mailSession);
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(SKUPNOST_MAIL));
//            message.setSubject("🚨Nova MSE je bila dodana v skupnost " + skupnost.getIme() +"!🚨");
//            String emailBody = "Nova MSE je bila dodana v skupnost " + skupnost.getIme() + "." + "\n\nPodatki o MSE: "
//                    + "\n\nID: " +mse.getId() + "\n\nNaziv: " +mse.getNaziv() + "\n\nIme: " + mse.getImeLastnika() + "\nPriimek: " + mse.getPriimekLastnika() +"\nEmail: " + mse.getEmail()
//                    +"\nZmogljivost v KW: " + mse.getZmogljivost() + "\nZemljepisna Sirina: " + mse.getLatitude() +"\nZemljepisna Dolzina: " + mse.getLongitude();
//            message.setText(emailBody);
//            Transport.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    public String izpisi() {
        return "EJB za skupnost klican!";
    }
}
