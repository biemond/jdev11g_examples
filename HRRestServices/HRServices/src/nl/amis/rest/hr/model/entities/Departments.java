package nl.amis.rest.hr.model.entities;

import java.io.Serializable;

import java.math.BigDecimal;

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

@Entity
@NamedQueries( { @NamedQuery(name = "Departments.findAll", query = "select o from Departments o") })
public class Departments implements Serializable {
    @SuppressWarnings("compatibility:6819408328857305")
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "DEPARTMENT_ID", nullable = false)
    private BigDecimal departmentId;
    @Column(name = "DEPARTMENT_NAME", nullable = false, length = 30)
    private String departmentName;
    @Column(name = "LOCATION_ID")
    private BigDecimal locationId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MANAGER_ID")
    private Employees manager;
    @OneToMany(mappedBy = "departments", fetch = FetchType.EAGER)
    private List<Employees> employeesList;

    public Departments() {
    }

    public Departments(BigDecimal departmentId, String departmentName, BigDecimal locationId, Employees manager) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.locationId = locationId;
        this.manager = manager;
    }


    public BigDecimal getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(BigDecimal departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public void setLocationId(BigDecimal locationId) {
        this.locationId = locationId;
    }


    public Employees getManager() {
        return manager;
    }

    public void setManager(Employees manager) {
        this.manager = manager;
    }

    public List<Employees> getEmployeesList() {
        return employeesList;
    }

    public void setEmployeesList(List<Employees> employeesList) {
        this.employeesList = employeesList;
    }

    public Employees addEmployees(Employees employees) {
        getEmployeesList().add(employees);
        employees.setDepartments(this);
        return employees;
    }

    public Employees removeEmployees(Employees employees) {
        getEmployeesList().remove(employees);
        employees.setDepartments(null);
        return employees;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("departmentId=");
        buffer.append(getDepartmentId());
        buffer.append(',');
        buffer.append("departmentName=");
        buffer.append(getDepartmentName());
        buffer.append(',');
        buffer.append("locationId=");
        buffer.append(getLocationId());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
