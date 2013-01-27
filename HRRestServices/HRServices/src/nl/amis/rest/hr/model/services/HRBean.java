package nl.amis.rest.hr.model.services;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import nl.amis.rest.hr.model.entities.Departments;
import nl.amis.rest.hr.model.entities.Employees;

@Stateless(name = "HRBean", mappedName = "HRRestServices-HRBean")
public class HRBean implements HR {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "HRServices")
    private EntityManager em;

    public HRBean() {
    }

    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    /** <code>select o from Employees o</code> */
    public List<Employees> getEmployeesFindAll() {
        return em.createNamedQuery("Employees.findAll").getResultList();
    }

    /** <code>select o from Departments o</code> */
    public List<Departments> getDepartmentsFindAll() {
        return em.createNamedQuery("Departments.findAll").getResultList();
    }
}
