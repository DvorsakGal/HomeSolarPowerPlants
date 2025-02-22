package si.um.feri.aiv.state;

import si.um.feri.aiv.vao.MalaSoncnaElektrarna;

public class PolnaMoc implements StanjeElektrarne{
    @Override
    public void vrniStanje(MalaSoncnaElektrarna malaSoncnaElektrarna) {
        malaSoncnaElektrarna.delajNaPolno();
    }

    @Override
    public String toString() {
        return "PolnaMoc";
    }
}
