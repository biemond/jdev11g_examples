package nl.whitehorses.ws;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.developer.JAXWSProperties;
import com.sun.xml.ws.developer.WSBindingProvider;

import javax.annotation.Resource;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

import nl.whitehors.ws.client.HelloImplService;
import nl.whitehorses.ws.interfaces.Hello;

@WebService
@Addressing
public class HelloImpl implements Hello {

    @Resource
    WebServiceContext context;

    @WebMethod()
    @Oneway()
    public void sayHello(String name) {

        HeaderList hl =
          (HeaderList)context.getMessageContext().get(
                  JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);

        // gets the addressing informations in the SOAP header
        WSEndpointReference reference = hl.getReplyTo(AddressingVersion.W3C, 
                                                      SOAPVersion.SOAP_11);
        String messageId = hl.getMessageID(AddressingVersion.W3C, 
                                           SOAPVersion.SOAP_11);
  
        HelloImplService srv = new HelloImplService();
        nl.whitehors.ws.client.HelloImpl portType = srv.getHelloImplPort();
        WSBindingProvider bp = (WSBindingProvider)portType;

        bp.setAddress( reference.getAddress());
        bp.setOutboundHeaders(Headers.create(AddressingVersion.W3C.relatesToTag,
                                             messageId));

        portType.callbackMessage("hello you [" +  reference.getAddress() + "]");
        
        
    }

    @WebMethod()
    @Oneway()
    public void callbackMessage(String msg) {
    }
}
