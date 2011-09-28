package nl.whitehorses.clientosb.jms;

import java.net.URISyntaxException;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

import weblogic.wsee.connection.transport.jms.JmsTransportInfo;

public class TestService {

    public TestService() {
    }


    public static void main(String[] args) {

        try {
            HelloWorldServiceSoapHttpPortBindingQSService hello = new HelloWorldServiceSoapHttpPortBindingQSService_Impl();
            HelloWorldService port = hello.getHelloWorldServiceSoapHttpPortBindingQSPort();

            String uri = "jms://laptopedwin:7001?URI=JMSProxyServiceRequest";  
            JmsTransportInfo ti =  new JmsTransportInfo(uri);  
            ((Stub)port)._setProperty("weblogic.wsee.connection.transportinfo", ti);  
           
            System.out.println(port.sayHello());

        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
