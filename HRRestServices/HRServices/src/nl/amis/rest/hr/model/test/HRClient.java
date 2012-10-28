package nl.amis.rest.hr.model.test;

import java.util.Hashtable;
import java.util.List;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;

import javax.naming.NamingException;

import nl.amis.rest.hr.model.entities.Departments;
import nl.amis.rest.hr.model.entities.Employees;
import nl.amis.rest.hr.model.services.HR;

public class HRClient {
    public static void main(String[] args) {
        try {
            final Context context = getInitialContext();
            HR hR = (HR)context.lookup("HRRestServices-HRBean#nl.amis.rest.hr.model.services.HR");

            for (Departments departments : (List<Departments>)hR.getDepartmentsFindAll()) {
                printDepartments(departments);
            }
        } catch (CommunicationException ex) {
            System.out.println(ex.getClass().getName());
            System.out.println(ex.getRootCause().getLocalizedMessage());
            System.out.println("\n*** A CommunicationException was raised.  This typically\n*** occurs when the target WebLogic server is not running.\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void printEmployees(Employees employees) {
        System.out.println("commissionPct = " + employees.getCommissionPct());
        System.out.println("email = " + employees.getEmail());
        System.out.println("employeeId = " + employees.getEmployeeId());
        System.out.println("firstName = " + employees.getFirstName());
        System.out.println("hireDate = " + employees.getHireDate());
        System.out.println("jobId = " + employees.getJobId());
        System.out.println("lastName = " + employees.getLastName());
        System.out.println("phoneNumber = " + employees.getPhoneNumber());
        System.out.println("salary = " + employees.getSalary());

    }

    private static void printDepartments(Departments departments) {
        System.out.println("departmentId = " + departments.getDepartmentId());
        System.out.println("departmentName = " + departments.getDepartmentName());
        System.out.println("locationId = " + departments.getLocationId());
        if ( departments.getManager() != null ){
            System.out.println("  deptManager ");
            printEmployees ( departments.getManager() );
        }

        System.out.println("    employees count:  "+departments.getEmployeesList().size());
        List<Employees> employees = departments.getEmployeesList();
        for ( Employees employee : employees ) {
            printEmployees(employee);
        }  
    }

    private static Context getInitialContext() throws NamingException {
        Hashtable env = new Hashtable();
        // WebLogic Server 10.x connection details
        env.put( Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory" );
        env.put(Context.PROVIDER_URL, "t3://localhost:7001");
        return new InitialContext( env );
    }
}
