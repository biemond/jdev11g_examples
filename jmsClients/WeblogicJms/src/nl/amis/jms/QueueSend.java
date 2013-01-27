package nl.amis.jms;

import java.util.Hashtable;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueSend {
    private final static String JNDI_FACTORY = 
        "weblogic.jndi.WLInitialContextFactory";
    private final static String JMS_FACTORY = "jms/CF";
    private final static String QUEUE = "jms/Queue";
    private final static String wlsUrl = "t3://localhost:7101";

    private static InitialContext getInitialContext() throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, wlsUrl);
        env.put(Context.SECURITY_PRINCIPAL, "weblogic");
        env.put(Context.SECURITY_CREDENTIALS, "weblogic1");
        return new InitialContext(env);
    }

    public static void main(String[] args) throws Exception {
        InitialContext ic = getInitialContext();
        QueueConnectionFactory qconFactory = 
            (QueueConnectionFactory)ic.lookup(JMS_FACTORY);
        QueueConnection qcon = 
            qconFactory.createQueueConnection();
        QueueSession qsession = qcon.createQueueSession(false, 
                                                        Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue)ic.lookup(QUEUE);
        QueueSender qsender =  qsession.createSender(queue);

        qcon.start();

        TextMessage msg = qsession.createTextMessage();;
        msg.setText("HelloWorld from WLS");
        qsender.send(msg);    

        qsender.close();
        qsession.close();
        qcon.close();
    }
}
