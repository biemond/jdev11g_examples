package nl.amis.jms;

import java.util.Properties;

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

    private final static String JMS_FACTORY = "jms/CF";
    private final static String QUEUE = "jms/Queue";

    private static Context getInitialContext() throws NamingException {
        Properties props = new Properties();

        props.setProperty("java.naming.factory.initial",
                          "com.sun.enterprise.naming.SerialInitContextFactory");

        props.setProperty("java.naming.factory.url.pkgs",
                          "com.sun.enterprise.naming");

        props.setProperty("java.naming.factory.state",
                          "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");

        // optional.  Defaults to localhost.  Only needed if web server is running
        // on a different host than the appserver   
        props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");

        // optional.  Defaults to 3700.  Only needed if target orb port is not 3700.
        props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

        return  new InitialContext(props);
    }   

    public static void main(String[] args) throws Exception {
        Context ic = getInitialContext();
        QueueConnectionFactory qconFactory = 
            (QueueConnectionFactory)ic.lookup(JMS_FACTORY);
        QueueConnection qcon = qconFactory.createQueueConnection();
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
        System.exit(12);
    }
}
