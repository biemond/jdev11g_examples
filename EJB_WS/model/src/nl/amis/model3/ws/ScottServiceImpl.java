package nl.amis.model3.ws;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import javax.jws.soap.SOAPBinding;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Action;

import nl.amis.model.entities.Dept;
import nl.amis.model.entities.Emp;
import nl.amis.model.services.ScottSessionLocal;

import nl.amis.model3.types.Department;
import nl.amis.model3.types.Employee;
import nl.amis.model3.types.GetDeptFindByPK;
import nl.amis.model3.types.GetDeptFindByPKResponse;
import nl.amis.model3.types.GetEmpFindByID;
import nl.amis.model3.types.GetEmpFindByIDResponse;
import nl.amis.model3.types.ObjectFactory;

@WebService(name = "ScottService3", targetNamespace = "http://nl.amis.hr.service", serviceName = "ScottService3", portName = "ScottServicePort3", wsdlLocation = "/ScottService3.wsdl")
@Stateless
public class ScottServiceImpl {
    public ScottServiceImpl() {
    }

    @EJB
    private ScottSessionLocal scottEJB;
    private ObjectFactory obj = new ObjectFactory();


    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @Action(input = "http://nl.amis.hr.service/ScottService/getDepartmentRequest", output = "http://nl.amis.hr.service/ScottService/getDepartmentResponse")
    @WebMethod
    @WebResult(name = "getDepartmentResponse", targetNamespace = "http://nl.amis.hr.entities", partName = "parameters")
    public GetDeptFindByPKResponse getDepartment(@WebParam(name = "getDepartmentRequest", partName = "parameters", targetNamespace = "http://nl.amis.hr.entities")
        GetDeptFindByPK parameters) {

        List<Dept> result = scottEJB.getDeptFindByPK(parameters.getId());
        if (result != null && result.size() > 0) {
            Dept dept = result.get(0);

            GetDeptFindByPKResponse response = obj.createGetDeptFindByPKResponse();
            Department department = obj.createDepartment();    
            department.setDepartmentNr(dept.getDeptno());
            department.setLocation(dept.getLoc());
            department.setName(dept.getDname());
            
            for ( Emp emp : dept.getEmpList()) {
              Employee employee = obj.createEmployee(); 
              employee.setCommission(emp.getComm());  
              employee.setEmployeeNr(emp.getEmpno());                  
              employee.setHiredate(getXMLGregorianCalendar(emp.getHiredate()));
              employee.setJob(emp.getJob());  
              employee.setManager(emp.getMgr());
              employee.setName(emp.getEname());
              employee.setSalary(emp.getSal());  
              department.getEmployees().add(employee);
            }    
            response.setDepartment(department);
            return response;
        }
         

        return null;
    }

    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @Action(input = "http://nl.amis.hr.service/ScottService/getEmployeeRequest", output = "http://nl.amis.hr.service/ScottService/getEmployeeResponse")
    @WebMethod
    @WebResult(name = "getEmployeeResponse", targetNamespace = "http://nl.amis.hr.entities", partName = "parameters")
    public GetEmpFindByIDResponse getEmployee(@WebParam(name = "getEmployeeRequest", partName = "parameters", targetNamespace = "http://nl.amis.hr.entities")
        GetEmpFindByID parameters) {

        List<Emp> result = scottEJB.getEmpFindByID(parameters.getId());
        if (result != null && result.size() > 0) {
            GetEmpFindByIDResponse response = obj.createGetEmpFindByIDResponse();
            
            Emp emp = result.get(0);
            Employee employee = obj.createEmployee(); 
            employee.setCommission(emp.getComm());  
            employee.setEmployeeNr(emp.getEmpno());                  
            employee.setHiredate(getXMLGregorianCalendar(emp.getHiredate()));
            employee.setJob(emp.getJob());  
            employee.setManager(emp.getMgr());
            employee.setName(emp.getEname());
            employee.setSalary(emp.getSal());  
            
            response.setEmployee(employee);
            return response;
        }

        return null;
    }

    public static XMLGregorianCalendar getXMLGregorianCalendar(Calendar cal) {   
        DatatypeFactory dtf = null;
        try {
            dtf = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        XMLGregorianCalendar xgc = dtf.newXMLGregorianCalendar();

        xgc.setYear(cal.get(Calendar.YEAR));
        xgc.setDay(cal.get(Calendar.DAY_OF_MONTH));
        xgc.setMonth(cal.get(Calendar.MONTH)+ 1);
        xgc.setHour(cal.get(Calendar.HOUR_OF_DAY));
        xgc.setMinute(cal.get(Calendar.MINUTE));
        xgc.setSecond(cal.get(Calendar.SECOND));
        xgc.setMillisecond(cal.get(Calendar.MILLISECOND));  
        return xgc;   
    }  

}
