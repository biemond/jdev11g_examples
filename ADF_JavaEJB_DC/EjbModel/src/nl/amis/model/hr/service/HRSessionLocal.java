package nl.amis.model.hr.service;

import java.util.List;

import javax.ejb.Local;

import nl.amis.model.hr.entities.Departments;
import nl.amis.model.hr.entities.Employees;
import nl.amis.model.hr.entities.Locations;
import nl.amis.model.hr.interfaces.LovService;

@Local
public interface HRSessionLocal extends LovService {

    List queryByJpql(String jpql);

    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);

    Departments persistDepartments(Departments departments);

    Departments mergeDepartments(Departments departments);

    void removeDepartments(Departments departments);

    List<Departments> getDepartmentsFindAll();

    Employees persistEmployees(Employees employees);

    Employees mergeEmployees(Employees employees);

    void removeEmployees(Employees employees);

    List<Employees> getEmployeesFindAll();

    Locations persistLocations(Locations locations);

    Locations mergeLocations(Locations locations);

    void removeLocations(Locations locations);

    List<Locations> getLocationsFindAll();
}
