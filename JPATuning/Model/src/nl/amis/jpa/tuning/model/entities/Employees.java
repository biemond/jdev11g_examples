package nl.amis.jpa.tuning.model.entities;

import java.io.Serializable;

import java.sql.Timestamp;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;

import org.eclipse.persistence.config.QueryHints;

@Entity
@NamedQueries(
 {
  @NamedQuery(name = "Employees.findAll", 
              query = "select o from Employees o"),
  @NamedQuery(name = "Employees.findByLastName", 
              query = "select o from Employees o where o.lastName = :lastName"),
  @NamedQuery(name = "Employees.findByLastName2", 
              query = "select o from Employees o where o.lastName = :lastName",
              hints= { @QueryHint( name =QueryHints.LEFT_FETCH, 
                                   value="Employees.managerEmployeesList"),
                       @QueryHint( name =QueryHints.LEFT_FETCH, 
                                   value="Employees.managerDepartmentsList")
                     }
              )
 }
)
public class Employees implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name="COMMISSION_PCT")
    private Double commissionPct;
    @Column(nullable = false, unique = true, length = 25)
    private String email;
    @Id
    @Column(name="EMPLOYEE_ID", nullable = false)
    private Long employeeId;
    @Column(name="FIRST_NAME", length = 20)
    private String firstName;
    @Column(name="HIRE_DATE", nullable = false)
    private Timestamp hireDate;
    @Column(name="JOB_ID", nullable = false, length = 10)
    private String jobId;
    @Column(name="LAST_NAME", nullable = false, length = 25)
    private String lastName;
    @Column(name="PHONE_NUMBER", length = 20)
    private String phoneNumber;
    private Double salary;


    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private Employees manager;


    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<Employees> managerEmployeesList;


    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<Departments> managerDepartmentsList;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Departments departments;

    public Employees() {
    }

    public Employees(Double commissionPct, Departments departments, String email,
                     Long employeeId, String firstName, Timestamp hireDate,
                     String jobId, String lastName, Employees manager,
                     String phoneNumber, Double salary) {
        this.commissionPct = commissionPct;
        this.departments = departments;
        this.email = email;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.hireDate = hireDate;
        this.jobId = jobId;
        this.lastName = lastName;
        this.manager = manager;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
    }

    public Double getCommissionPct() {
        return commissionPct;
    }

    public void setCommissionPct(Double commissionPct) {
        this.commissionPct = commissionPct;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Timestamp getHireDate() {
        return hireDate;
    }

    public void setHireDate(Timestamp hireDate) {
        this.hireDate = hireDate;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Departments getDepartments() {
        return departments;
    }

    public void setDepartments(Departments departments) {
        this.departments = departments;
    }

    public void setManager(Employees manager) {
        this.manager = manager;
    }

    public Employees getManager() {
        return manager;
    }

    public void setManagerEmployeesList(List<Employees> managerEmployeesList) {
        this.managerEmployeesList = managerEmployeesList;
    }

    public List<Employees> getManagerEmployeesList() {
        return managerEmployeesList;
    }

    public void setManagerDepartmentsList(List<Departments> managerDepartmentsList) {
        this.managerDepartmentsList = managerDepartmentsList;
    }

    public List<Departments> getManagerDepartmentsList() {
        return managerDepartmentsList;
    }
}
