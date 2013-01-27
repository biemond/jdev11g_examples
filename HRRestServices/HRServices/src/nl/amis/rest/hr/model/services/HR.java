package nl.amis.rest.hr.model.services;

import java.util.List;

import javax.ejb.Remote;

import nl.amis.rest.hr.model.entities.Departments;
import nl.amis.rest.hr.model.entities.Employees;

@Remote
public interface HR {
    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);

    List<Employees> getEmployeesFindAll();

    List<Departments> getDepartmentsFindAll();
}
