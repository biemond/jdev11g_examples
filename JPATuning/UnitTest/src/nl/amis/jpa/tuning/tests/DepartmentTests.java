package nl.amis.jpa.tuning.tests;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import javax.persistence.Persistence;

import nl.amis.jpa.tuning.model.entities.Departments;
import nl.amis.jpa.tuning.model.entities.Employees;

import org.eclipse.persistence.config.QueryHints;

public class DepartmentTests {


    public static void main(String[] args) {

        // Get the entity manager for the tests.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model");
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("begin ------------  department1");   
            List<Departments> result = em.createNamedQuery("Departments.findByName")
                                            .setParameter("name", "Finance")
                                            .getResultList();
            System.out.println("end ------------  department1");   
            em.clear();
            System.out.println("begin ------------  department2");   

            List<Departments> result2 = em.createNamedQuery("Departments.findByNameFetch")
                                            .setParameter("name", "Finance")
                                            .getResultList();
            System.out.println("end ------------  department2");   
            em.clear();

            System.out.println("begin ------------  department3");   
            List<Departments> result3 = em.createNamedQuery("Departments.findByNameFetch2")
                                            .setParameter("name", "Finance")
                                            .getResultList();
            System.out.println("end ------------  department3");   
            em.clear();

            System.out.println("begin ------------  department4");   
            List<Departments> result4 = em.createNamedQuery("Departments.findByName")
                                            .setParameter("name", "Finance")
                                            .setHint(QueryHints.LEFT_FETCH, "Departments.employeesList")
                                            .getResultList();
            System.out.println("end ------------  department4");   
            em.clear();

            System.out.println("begin ------------  department5");   

            List<Departments> result5 = em.createNamedQuery("Departments.findByNameFetch3")
                                            .setParameter("name", "Finance")
                                            .getResultList();
            System.out.println("end ------------  department5");   
            em.clear();




        } catch (RuntimeException e) {
           e.printStackTrace();
        } finally {
           //Close the manager
           em.close();
           emf.close();
        }

    }

}
