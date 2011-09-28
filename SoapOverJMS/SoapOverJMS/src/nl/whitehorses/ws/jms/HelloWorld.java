package nl.whitehorses.ws.jms;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import weblogic.jws.WLJmsTransport;
@WebService(portName = "HelloWorldJMSPort",
            serviceName = "HelloWorldJMSService")

@WLJmsTransport(serviceUri="HelloWorldJMSService",
                contextPath="HelloWorldJMSService",
                queue="jms.SoapQueue", 
                portName="HelloWorldJMSPort", 
                connectionFactory="jms.CF") 

public class HelloWorld {

    @WebMethod
    @WebResult(name = "result")
    public String sayHello(@WebParam(name = "message") String message ) {
      int i  = 1/0;
      return message;
    }
}
