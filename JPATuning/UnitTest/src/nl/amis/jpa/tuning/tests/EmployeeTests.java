package nl.amis.jpa.tuning.tests;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import nl.amis.jpa.tuning.model.entities.Departments;

import nl.amis.jpa.tuning.model.entities.Employees;

import org.eclipse.persistence.config.QueryHints;

public class EmployeeTests {


    public static void main(String[] args) {

        // Get the entity manager for the tests.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model");
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("begin ------------  employee1");   
            List<Departments> result = em.createNamedQuery("Employees.findByLastName")
                                            .setParameter("lastName", "King")
                                            .getResultList();
            System.out.println("end ------------  employee1");   
            em.clear();

            System.out.println("begin ------------  employee2");   
            List<Departments> result2 = em.createNamedQuery("Employees.findByLastName")
                                            .setParameter("lastName", "King")
                                            .getResultList();
            System.out.println("end ------------  employee2");   
            em.clear();

            System.out.println("begin ------------  employee3");   
            List<Departments> result3 = em.createNamedQuery("Employees.findByLastName")
                                            .setParameter("lastName", "King")
                                            .setHint(QueryHints.LEFT_FETCH, "Employees.departments")
                                            .getResultList();
            System.out.println("end ------------  employee3");   
            em.clear();

            System.out.println("begin ------------  employee4");   
            List<Departments> result4 = em.createNamedQuery("Employees.findByLastName2")
                                            .setParameter("lastName", "King")
                                            .getResultList();
            System.out.println("end ------------  employee4");   
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
