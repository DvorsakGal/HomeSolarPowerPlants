package si.um.feri.aiv.state;

import si.um.feri.aiv.vao.MalaSoncnaElektrarna;

public class PolovicnaMoc implements StanjeElektrarne{
    @Override
    public void vrniStanje(MalaSoncnaElektrarna malaSoncnaElektrarna) {
        malaSoncnaElektrarna.delajSPolovicnoMocjo();
    }

    @Override
    public String toString() {
        return "PolovicnaMoc";
    }
}
