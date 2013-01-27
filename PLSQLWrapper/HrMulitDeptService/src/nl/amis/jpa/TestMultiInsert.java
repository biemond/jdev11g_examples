package nl.amis.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import nl.amis.jpa.entities.Departments;

import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.platform.database.jdbc.JDBCTypes;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredProcedureCall;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.sessions.server.ServerSession;


public class TestMultiInsert {
    public TestMultiInsert() {
    }

    public static void main(String[] a) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HrMulitDeptService");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        List dept = new ArrayList();

        Departments dept1 = new Departments();
        dept1.setDepartmentId(505L);
        dept1.setDepartmentName("amersfoort");
        dept1.setLocationId(1700L);
        dept1.setManagerId(100L);
        dept.add(dept1);

        Departments dept2 = new Departments();
        dept2.setDepartmentId(506L);
        dept2.setDepartmentName("utrecht");
        dept2.setLocationId(1700L);
        dept2.setManagerId(100L);
        dept.add(dept2);

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.addNamedArgument("P_RECORDS", 
                              DepartmentUtils.departmentCollection(),
                              JDBCTypes.ARRAY_TYPE.getSqlCode());
        call.setProcedureName("HR_DEPARTMENTS.INSERTDEPARTMENTS");

        DataReadQuery databaseQuery = new DataReadQuery();
        databaseQuery.addArgument("P_RECORDS",ArrayList.class);
        databaseQuery.setCall(call);
        Vector args=new Vector(); 
        args.add(dept);

        ServerSession session =
          ((JpaEntityManager)em.getDelegate()).getServerSession();

        session.addDescriptor(DepartmentUtils.departmentDescriptor());
        // execute
        JpaHelper.getEntityManager(em).getActiveSession().executeQuery(databaseQuery,args);

        em.getTransaction().commit();

        em.close();
        emf.close();
    }

}
