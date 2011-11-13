package nl.amis.jsf.model;



import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EJBUtils {

    public static <T> T lookup(String jndiLocation) throws NamingException {
        Context ctx = new InitialContext();
        try {
            return (T)ctx.lookup(jndiLocation);
        } catch (NamingException ne) {
            ne.printStackTrace();
        }
        return null;
    }
}
