package si.um.feri.aiv.ejb;

import si.um.feri.aiv.Skup;

import javax.naming.InitialContext;
import java.util.Properties;

public class SkupTest {

    /*
    WF SAYS:

    java:global/MaleSoncneElektrarne/SkupBean!si.um.feri.aiv.Skup
	java:app/MaleSoncneElektrarne/SkupBean!si.um.feri.aiv.Skup
	java:module/SkupBean!si.um.feri.aiv.Skup
	java:jboss/exported/MaleSoncneElektrarne/SkupBean!si.um.feri.aiv.Skup
	ejb:/MaleSoncneElektrarne/SkupBean!si.um.feri.aiv.Skup
	java:global/MaleSoncneElektrarne/SkupBean
	java:app/MaleSoncneElektrarne/SkupBean
	java:module/SkupBean
     */

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put("java.naming.factory.initial","org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put("java.naming.provider.url","http-remoting://127.0.0.1:8080");
        props.put("jboss.naming.client.ejb.context","true");
        props.put("java.naming.factory.url.pkgs","org.jboss.ejb.client.naming");
        InitialContext ctx=new InitialContext(props);

        Skup s = (Skup) ctx.lookup("MaleSoncneElektrarne/SkupBean!si.um.feri.aiv.Skup");


        s.Izpisi();
        System.out.println(s.addition(1,2));
        System.out.println(s.vrniKapaciteto("avengers"));
    }
}
