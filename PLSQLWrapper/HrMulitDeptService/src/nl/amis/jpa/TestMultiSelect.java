package nl.amis.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import nl.amis.jpa.entities.Departments;

import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredFunctionCall;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.eclipse.persistence.sessions.server.ServerSession;


public class TestMultiSelect {

  public static void main(String[] args) {
      EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("HrMulitDeptService");
      EntityManager em = emf.createEntityManager();
       
 
      PLSQLStoredFunctionCall call = 
          new PLSQLStoredFunctionCall(DepartmentUtils.departmentCollection());
      call.setProcedureName("HR_DEPARTMENTS.selectdepartments");

      DataReadQuery databaseQuery = new DataReadQuery();
      databaseQuery.setCall(call);

      ServerSession session =
          ((JpaEntityManager)em.getDelegate()).getServerSession();

      session.addDescriptor(DepartmentUtils.departmentDescriptor());
      
      Query query = 
           ((JpaEntityManager)em.getDelegate()).createQuery(databaseQuery);

      DatabaseRecord result = (DatabaseRecord)query.getSingleResult();

      List<Departments> departments = (List<Departments>)result.get("RESULT");
      for ( Departments dept : departments ) {
        System.out.println(dept.getDepartmentName());
      }

      em.close();
      emf.close();
  }


}
