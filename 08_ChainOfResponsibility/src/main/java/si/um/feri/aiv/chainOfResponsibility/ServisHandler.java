package si.um.feri.aiv.chainOfResponsibility;

import si.um.feri.aiv.vao.MalaSoncnaElektrarna;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ServisHandler extends Handler{
    @Override
    public void handleRequest(MalaSoncnaElektrarna mse) {
        System.out.println("Prejem zahteve SERVIS");
        LocalDate zadnjiServis = mse.getDatumVzdrzevalnegaServisa();
        LocalDate currentDate = LocalDate.now();
        long monthsSinceLastServis = ChronoUnit.MONTHS.between(zadnjiServis, currentDate);

        if (monthsSinceLastServis >= 54 && monthsSinceLastServis <= 58) {
            System.out.println("Približuje se servis");
        } else if (monthsSinceLastServis > 58) {
            System.out.println("POTREBEN SERVIS!");
        }

        if (successor != null) {
            successor.handleRequest(mse);
        }
    }
}
