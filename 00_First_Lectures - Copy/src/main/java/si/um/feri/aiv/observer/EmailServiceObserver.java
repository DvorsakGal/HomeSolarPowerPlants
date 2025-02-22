package si.um.feri.aiv.observer;

import si.um.feri.aiv.vao.MalaSoncnaElektrarna;

public class EmailServiceObserver implements SkupnostObserver{

    private String email;

    public EmailServiceObserver(String email) {
        this.email = email;
    }

    @Override
    public void update(MalaSoncnaElektrarna malaSoncnaElektrarna) {
    }
}
