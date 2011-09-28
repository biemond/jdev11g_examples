package nl.amis.model4.ws;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.jws.WebService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nl.amis.model.entities.Dept;
import nl.amis.model.entities.Emp;
import nl.amis.model.services.ScottSessionLocal;
import nl.amis.model4.types.Department;
import nl.amis.model4.types.Employee;
import nl.amis.model4.types.GetDeptFindByPK;
import nl.amis.model4.types.GetDeptFindByPKResponse;
import nl.amis.model4.types.GetEmpFindByID;
import nl.amis.model4.types.GetEmpFindByIDResponse;
import nl.amis.model4.types.ObjectFactory;

@WebService(serviceName = "ScottService4", 
            targetNamespace = "http://nl.amis.hr.service", 
            portName = "ScottServicePort4", 
            endpointInterface = "nl.amis.model4.ws.ScottService4", 
            wsdlLocation = "/ScottService4.wsdl")
@Stateless
public class ScottService4Impl {
    public ScottService4Impl() {
    }

    @EJB
    private ScottSessionLocal scottEJB;
    private ObjectFactory obj = new ObjectFactory();

    public GetDeptFindByPKResponse getDepartment(GetDeptFindByPK parameters) {
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
       

      return null;    }

    public GetEmpFindByIDResponse getEmployee(GetEmpFindByID parameters) {
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
