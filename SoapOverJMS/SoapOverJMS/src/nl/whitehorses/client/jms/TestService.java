package nl.whitehorses.client.jms;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

import weblogic.wsee.jaxrpc.WLStub;

public class TestService {
    public TestService() throws RemoteException, ServiceException {

        HelloWorldJMSService service = new HelloWorldJMSService_Impl(
           "http://localhost:7101/HelloWorldJMSService/HelloWorldJMSService?WSDL");

        HelloWorld port = service.getHelloWorldJMSPort();

        Stub stub = (Stub)port;

        stub._setProperty(WLStub.JMS_TRANSPORT_JNDI_URL,"t3://localhost:7101");
 

        try {
            String result = null;
            result = port.sayHello("Hello");
            System.out.println("Got JMS result: " + result);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        TestService testService;
        try {
            testService = new TestService();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
