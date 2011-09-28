package nl.whitehorse.wls.sca.spring.client;

import javax.naming.Context;
import javax.naming.InitialContext;

import nl.whitehorses.wls.sca.spring.ILoggerComponent;

public class TestLogger {
    public TestLogger() {
        super();
    }

    public static void main(String[] args) {
        try {
            final Context context = new InitialContext();

            ILoggerComponent logger = (ILoggerComponent)context.lookup("LoggerService_EJB30_JNDI");
            logger.log("scaSpring", "111", "hello");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
