JMS test client for WebLogic , GlassFish 3.1 and JBoss 7.1
==========================================================

created by Edwin Biemond   
[biemond.blogspot.com](http://biemond.blogspot.com)    
[Github homepage](https://github.com/biemond/puppet)    

Setup
-----
* Create Connection Factory jms/CF
* Create Queue jms/Queue

GlassFish
---------
add glassfish3/glassfish/lib/gf-client.jar and glassfish3/glassfish/lib/appserv-rt.jar

start imq  
glassfish3\mq\bin>imqbrokerd  -name LocalJMS
check with  
glassfish3\mq\bin>imqadmin.exe

set JMS to Local and set username, password admin,admin  
Check with Ping

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

JBoss
-----

uses HornetQ and add the lib jars hornetq-jms-client.jar, jboss-client.jar
For all details [JBoss remote jms](http://biemond.blogspot.nl/2012/06/remote-jms-with-jboss-as-71-hornetq-jms.html)

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


WebLogic
--------

Generate a wlfullcient 

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
