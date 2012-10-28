package nl.amis.rest.hr.model.entities;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
  @NamedQuery(name = "Employees.findAll", query = "select o from Employees o")
})
public class Employees implements Serializable {
    @SuppressWarnings("compatibility:3012448003487237613")
    private static final long serialVersionUID = 1L;
    @Column(name="COMMISSION_PCT")
    private BigDecimal commissionPct;
    @Column(nullable = false, unique = true, length = 25)
    private String email;
    @Id
    @Column(name="EMPLOYEE_ID", nullable = false)
    private BigDecimal employeeId;
    @Column(name="FIRST_NAME", length = 20)
    private String firstName;
    @Temporal(TemporalType.DATE)
    @Column(name="HIRE_DATE", nullable = false)
    private Date hireDate;
    @Column(name="JOB_ID", nullable = false, length = 10)
    private String jobId;
    @Column(name="LAST_NAME", nullable = false, length = 25)
    private String lastName;
    @Column(name="PHONE_NUMBER", length = 20)
    private String phoneNumber;
    private BigDecimal salary;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MANAGER_ID")
    private Employees manager;
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<Employees> managerEmployeesList;
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<Departments> managerDepartmentsList;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID")
    private Departments departments;

    public Employees() {
    }

    public Employees(BigDecimal commissionPct, Departments departments, String email, BigDecimal employeeId,
                     String firstName, Date hireDate, String jobId, String lastName, Employees manager,
                     String phoneNumber, BigDecimal salary) {
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


    public BigDecimal getCommissionPct() {
        return commissionPct;
    }

    public void setCommissionPct(BigDecimal commissionPct) {
        this.commissionPct = commissionPct;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(BigDecimal employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
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

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Employees getManager() {
        return manager;
    }

    public void setManager(Employees manager) {
        this.manager = manager;
    }

    public List<Employees> getManagerEmployeesList() {
        return managerEmployeesList;
    }

    public void setManagerEmployeesList(List<Employees> managerEmployeesList) {
        this.managerEmployeesList = managerEmployeesList;
    }

    public Employees addEmployees(Employees employees) {
        getManagerEmployeesList().add(employees);
        employees.setManager(this);
        return employees;
    }

    public Employees removeEmployees(Employees employees) {
        getManagerEmployeesList().remove(employees);
        employees.setManager(null);
        return employees;
    }

    public List<Departments> getManagerDepartmentsList() {
        return managerDepartmentsList;
    }

    public void setManagerDepartmentsList(List<Departments> managerDepartmentsList) {
        this.managerDepartmentsList = managerDepartmentsList;
    }

    public Departments addDepartments(Departments departments) {
        getManagerDepartmentsList().add(departments);
        departments.setManager(this);
        return departments;
    }

    public Departments removeDepartments(Departments departments) {
        getManagerDepartmentsList().remove(departments);
        departments.setManager(null);
        return departments;
    }

    public Departments getDepartments() {
        return departments;
    }

    public void setDepartments(Departments departments) {
        this.departments = departments;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("commissionPct=");
        buffer.append(getCommissionPct());
        buffer.append(',');
        buffer.append("email=");
        buffer.append(getEmail());
        buffer.append(',');
        buffer.append("employeeId=");
        buffer.append(getEmployeeId());
        buffer.append(',');
        buffer.append("firstName=");
        buffer.append(getFirstName());
        buffer.append(',');
        buffer.append("hireDate=");
        buffer.append(getHireDate());
        buffer.append(',');
        buffer.append("jobId=");
        buffer.append(getJobId());
        buffer.append(',');
        buffer.append("lastName=");
        buffer.append(getLastName());
        buffer.append(',');
        buffer.append("phoneNumber=");
        buffer.append(getPhoneNumber());
        buffer.append(',');
        buffer.append("salary=");
        buffer.append(getSalary());
        buffer.append(']');
        return buffer.toString();
    }
}
