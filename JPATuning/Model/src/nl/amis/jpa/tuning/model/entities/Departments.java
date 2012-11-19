package nl.amis.jpa.tuning.model.entities;

import java.io.Serializable;

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
  @NamedQuery(name = "Departments.findAll", 
              query = "select o from Departments o"
              ),
  @NamedQuery(name  = "Departments.findByName", 
              query = "select o from Departments o where o.departmentName = :name"
              ),
  @NamedQuery(name  = "Departments.findByNameFetch", 
              query = "select o from Departments o " +
                      "left join fetch o.employeesList " +
                      "where o.departmentName = :name"
              ),
  @NamedQuery(name  = "Departments.findByNameFetch2", 
              query = "select o from Departments o " +
                      "where o.departmentName = :name",
              hints= { @QueryHint( name =QueryHints.LEFT_FETCH, 
                                   value="Departments.employeesList")
                     }
              ),
  @NamedQuery(name  = "Departments.findByNameFetch3", 
              query = "select o from Departments o " +
                      "where o.departmentName = :name",
              hints= { @QueryHint( name =QueryHints.LEFT_FETCH, 
                                   value="Departments.employeesList.manager")
                     }
              )
 }
)
public class Departments implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="DEPARTMENT_ID", nullable = false)
    private Long departmentId;

    @Column(name="DEPARTMENT_NAME", nullable = false, length = 30)
    private String departmentName;

    @Column(name="LOCATION_ID")
    private Long locationId;

    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private Employees manager;

    @OneToMany(mappedBy = "departments", fetch = FetchType.LAZY)
    private List<Employees> employeesList;

    public Departments() {
    }

    public Departments(Long departmentId, String departmentName,
                       Long locationId, Employees manager) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.locationId = locationId;
        this.manager = manager;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
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

    public void setManager(Employees manager) {
        this.manager = manager;
    }

    public Employees getManager() {
        return manager;
    }
}
