package nl.amis.model.hr.service;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import nl.amis.model.hr.entities.Departments;
import nl.amis.model.hr.entities.Employees;
import nl.amis.model.hr.entities.Locations;
import nl.amis.model.hr.interfaces.LovService;

@Stateless(name = "HRSessionBean", mappedName = "HRSessionBean")
public class HRSessionBean implements HRSessionLocal {
    @PersistenceContext(unitName="EjbModel")
    private EntityManager em;

    public HRSessionBean() {
    }

    public List queryByJpql(String jpql) {
      Query query = em.createQuery(jpql);  
      return query.getResultList();
    }

    public Object queryByRange(String jpqlStmt, int firstResult,
                               int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    public Departments persistDepartments(Departments departments) {
        em.persist(departments);
        return departments;
    }

    public Departments mergeDepartments(Departments departments) {
        return em.merge(departments);
    }

    public void removeDepartments(Departments departments) {
        departments = em.find(Departments.class, departments.getDepartmentId());
        em.remove(departments);
    }

    /** <code>select o from Departments o</code> */
    public List<Departments> getDepartmentsFindAll() {
        return em.createNamedQuery("Departments.findAll").getResultList();
    }

    public Employees persistEmployees(Employees employees) {
        em.persist(employees);
        return employees;
    }

    public Employees mergeEmployees(Employees employees) {
        return em.merge(employees);
    }

    public void removeEmployees(Employees employees) {
        employees = em.find(Employees.class, employees.getEmployeeId());
        em.remove(employees);
    }

    /** <code>select o from Employees o</code> */
    public List<Employees> getEmployeesFindAll() {
        return em.createNamedQuery("Employees.findAll").getResultList();
    }


    public Locations persistLocations(Locations locations) {
        em.persist(locations);
        return locations;
    }

    public Locations mergeLocations(Locations locations) {
        return em.merge(locations);
    }

    public void removeLocations(Locations locations) {
        locations = em.find(Locations.class, locations.getLocationId());
        em.remove(locations);
    }

    /** <code>select o from Locations o</code> */
    public List<Locations> getLocationsFindAll() {
        return em.createNamedQuery("Locations.findAll").getResultList();
    }
}
