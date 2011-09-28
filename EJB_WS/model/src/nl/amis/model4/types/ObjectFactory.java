
package nl.amis.model4.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the nl.amis.model4.types package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetDepartmentResponse_QNAME = new QName("http://nl.amis.hr.entities", "getDepartmentResponse");
    private final static QName _GetDepartmentRequest_QNAME = new QName("http://nl.amis.hr.entities", "getDepartmentRequest");
    private final static QName _GetEmployeeRequest_QNAME = new QName("http://nl.amis.hr.entities", "getEmployeeRequest");
    private final static QName _GetEmployeeResponse_QNAME = new QName("http://nl.amis.hr.entities", "getEmployeeResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: nl.amis.model4.types
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetEmpFindByID }
     * 
     */
    public GetEmpFindByID createGetEmpFindByID() {
        return new GetEmpFindByID();
    }

    /**
     * Create an instance of {@link GetEmpFindByIDResponse }
     * 
     */
    public GetEmpFindByIDResponse createGetEmpFindByIDResponse() {
        return new GetEmpFindByIDResponse();
    }

    /**
     * Create an instance of {@link GetDeptFindByPKResponse }
     * 
     */
    public GetDeptFindByPKResponse createGetDeptFindByPKResponse() {
        return new GetDeptFindByPKResponse();
    }

    /**
     * Create an instance of {@link GetDeptFindByPK }
     * 
     */
    public GetDeptFindByPK createGetDeptFindByPK() {
        return new GetDeptFindByPK();
    }

    /**
     * Create an instance of {@link Department }
     * 
     */
    public Department createDepartment() {
        return new Department();
    }

    /**
     * Create an instance of {@link Employee }
     * 
     */
    public Employee createEmployee() {
        return new Employee();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDeptFindByPKResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nl.amis.hr.entities", name = "getDepartmentResponse")
    public JAXBElement<GetDeptFindByPKResponse> createGetDepartmentResponse(GetDeptFindByPKResponse value) {
        return new JAXBElement<GetDeptFindByPKResponse>(_GetDepartmentResponse_QNAME, GetDeptFindByPKResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDeptFindByPK }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nl.amis.hr.entities", name = "getDepartmentRequest")
    public JAXBElement<GetDeptFindByPK> createGetDepartmentRequest(GetDeptFindByPK value) {
        return new JAXBElement<GetDeptFindByPK>(_GetDepartmentRequest_QNAME, GetDeptFindByPK.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEmpFindByID }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nl.amis.hr.entities", name = "getEmployeeRequest")
    public JAXBElement<GetEmpFindByID> createGetEmployeeRequest(GetEmpFindByID value) {
        return new JAXBElement<GetEmpFindByID>(_GetEmployeeRequest_QNAME, GetEmpFindByID.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEmpFindByIDResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nl.amis.hr.entities", name = "getEmployeeResponse")
    public JAXBElement<GetEmpFindByIDResponse> createGetEmployeeResponse(GetEmpFindByIDResponse value) {
        return new JAXBElement<GetEmpFindByIDResponse>(_GetEmployeeResponse_QNAME, GetEmpFindByIDResponse.class, null, value);
    }

}
