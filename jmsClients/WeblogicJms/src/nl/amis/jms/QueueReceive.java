package nl.amis.jms;

import java.util.Hashtable;

import javax.jms.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueReceive {
    private final static String JNDI_FACTORY = 
        "weblogic.jndi.WLInitialContextFactory";
    private final static String JMS_FACTORY = "jms/CF2";
    private final static String QUEUE = "jms/test2";
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
        QueueConnectionFactory qconFactory = (QueueConnectionFactory)ic.lookup(JMS_FACTORY);
        QueueConnection qcon = qconFactory.createQueueConnection();
        QueueSession qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue)ic.lookup(QUEUE);
        QueueReceiver qreceiver =  qsession.createReceiver(queue);

        qcon.start();

        TextMessage msg = (TextMessage)qreceiver.receive();    
        System.out.println(msg.getText());

        qreceiver.close();
        qsession.close();
        qcon.close();
    }
}
