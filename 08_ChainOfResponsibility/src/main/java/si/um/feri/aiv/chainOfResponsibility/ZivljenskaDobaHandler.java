package si.um.feri.aiv.chainOfResponsibility;

import si.um.feri.aiv.vao.MalaSoncnaElektrarna;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ZivljenskaDobaHandler extends Handler{
    @Override
    public void handleRequest(MalaSoncnaElektrarna mse) {
        System.out.println("Prejem zahteve ŽIVLJENSKA DOBA");
        LocalDate vgradnja = mse.getDatumVgradnje();
        LocalDate currentDate = LocalDate.now();
        long monthsSinceVgradnja = ChronoUnit.MONTHS.between(vgradnja, currentDate);

        if (monthsSinceVgradnja >= 234 && monthsSinceVgradnja <= 238) {
            System.out.println("Približuje se konec življenske dobe");
        } else if (monthsSinceVgradnja > 238) {
            System.out.println("ŽIVLJENSKA DOBA SE IZTEKA!");
        }

        if (successor != null) {
            successor.handleRequest(mse);
        }
    }
}
