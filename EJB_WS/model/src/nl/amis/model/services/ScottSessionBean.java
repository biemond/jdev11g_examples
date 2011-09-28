package nl.amis.model.services;

import java.util.List;

import javax.ejb.Stateless;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import nl.amis.model.entities.Dept;
import nl.amis.model.entities.Emp;


@Stateless(name = "ScottSessionBean", mappedName = "EJB-model-ScottSessionBean")
public class ScottSessionBean implements ScottSession,ScottSessionLocal {
    @PersistenceContext(unitName="model")
    private EntityManager em;

   
    public ScottSessionBean() {
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

    /** <code>select o from Dept o</code> */
    public List<Dept> getDeptFindAll() {
        System.out.println("getDeptFindAll");
        return em.createNamedQuery("Dept.findAll").getResultList();
    }

    /** <code>select o from Dept o where o.deptno = :deptid</code> */
    public List<Dept> getDeptFindByPK(  Long deptid) {
        System.out.println("getDeptFindByPK");
        return em.createNamedQuery("Dept.findByPK").setParameter("deptid", deptid).getResultList();
    }

    /** <code>select o from Emp o</code> */
    public List<Emp> getEmpFindAll() {
        return em.createNamedQuery("Emp.findAll").getResultList();
    }

    /** <code>select o from Emp o where o.empno = :empno </code> */
    public List<Emp> getEmpFindByID(Long empno) {
        return em.createNamedQuery("Emp.findByID").setParameter("empno", empno).getResultList();
    }
}
