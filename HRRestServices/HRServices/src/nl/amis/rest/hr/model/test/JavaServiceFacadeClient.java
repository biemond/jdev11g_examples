package nl.amis.rest.hr.model.test;

import java.util.List;

import nl.amis.rest.hr.model.entities.Departments;
import nl.amis.rest.hr.model.entities.Employees;

public class JavaServiceFacadeClient {
    public static void main(String[] args) {
        try {
            final JavaServiceFacade javaServiceFacade = new JavaServiceFacade();

            List<Departments> departments = javaServiceFacade.getAllDepartments();
            
            for ( Departments department : departments ) {
                printDepartments(department);
                
                
            }
            

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
        System.out.println("manager = " + employees.getManager());
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

}
