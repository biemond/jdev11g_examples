package nl.amis.jpa.tuning.tests;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

import javax.persistence.criteria.CriteriaQuery;

import javax.persistence.criteria.Root;

import nl.amis.jpa.tuning.model.entities.Departments;
import nl.amis.jpa.tuning.model.entities.Departments_;

public class DepartmentCriteriaTest {


    public static void main(String[] args) {

        // Get the entity manager for the tests.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model");
        EntityManager em = emf.createEntityManager();
        try {
            
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Departments> cq = cb.createQuery(Departments.class);
            Root<Departments> c = cq.from(Departments.class);
            cq.where(cb.equal(c.get(Departments_.departmentName), "Finance"));
            cq.select(c);

            TypedQuery<Departments> tq = em.createQuery(cq);
            List<Departments> results = tq.getResultList();

            for (Departments department : results) {
                System.out.println(department.getDepartmentName().toString());
            }
            

        } catch (RuntimeException e) {
           e.printStackTrace();
        } finally {
           //Close the manager
           em.close();
           emf.close();
        }

    }
}
