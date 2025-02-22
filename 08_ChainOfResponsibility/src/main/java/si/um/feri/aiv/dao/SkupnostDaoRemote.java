package si.um.feri.aiv.dao;

import jakarta.ejb.Remote;

@Remote
public interface SkupnostDaoRemote {
    String izpisi();
}
