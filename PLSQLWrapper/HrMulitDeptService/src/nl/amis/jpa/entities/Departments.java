package nl.amis.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.eclipse.persistence.annotations.Struct;


@Embeddable
@Struct( name = "HR_DEPARTMENT_ROW_TYPE" , 
         fields = {"DEPARTMENT_ID",
                   "DEPARTMENT_NAME",
                   "MANAGER_ID",
                   "LOCATION_ID"
                  } 
      )
public class Departments implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name="DEPARTMENT_ID")
    private Long departmentId;

    @Column(name="DEPARTMENT_NAME")
    private String departmentName;

    @Column(name="LOCATION_ID")
    private Long locationId;

    @Column(name="MANAGER_ID")
    private Long managerId;

    public Departments() {
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

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
