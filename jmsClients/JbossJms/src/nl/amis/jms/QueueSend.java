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
        "org.jboss.naming.remote.client.InitialContextFactory";
    private final static String JMS_FACTORY = "jms/RemoteConnectionFactory";
    private final static String QUEUE = "jms/queue/test2";
    private final static String jbossUrl = "remote://localhost:4447";

    private static InitialContext getInitialContext() throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, jbossUrl);
        env.put(Context.SECURITY_PRINCIPAL, "jms");
        env.put(Context.SECURITY_CREDENTIALS, "jboss1");
        return new InitialContext(env);
    }

    public static void main(String[] args) throws Exception {
        InitialContext ic = getInitialContext();
        QueueConnectionFactory qconFactory = 
            (QueueConnectionFactory)ic.lookup(JMS_FACTORY);
        QueueConnection qcon = 
            qconFactory.createQueueConnection("jms","jboss1");
        QueueSession qsession = qcon.createQueueSession(false, 
                                                        Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue)ic.lookup(QUEUE);
        QueueSender qsender =  qsession.createSender(queue);

        qcon.start();

        TextMessage msg = qsession.createTextMessage();;
        msg.setText("HelloWorld");
        qsender.send(msg);    

        qsender.close();
        qsession.close();
        qcon.close();
    }
}
