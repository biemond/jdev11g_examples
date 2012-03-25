package nl.amis.jpa;

import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import nl.amis.jpa.entities.Departments;

import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredProcedureCall;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.sessions.server.ServerSession;


public class TestInsert {

    public static void main(String[] a) throws Exception {

        EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("HrMulitDeptService");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Departments dept1 = new Departments();
        dept1.setDepartmentId(505L);
        dept1.setDepartmentName("amersfoort");
        dept1.setManagerId(200L);
        dept1.setLocationId(1700L);

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.addNamedArgument("P_RECORD", DepartmentUtils.departmentRecord());
        call.setProcedureName("HR_DEPARTMENTS.INSERTDEPARTMENT");

        DataReadQuery databaseQuery = new DataReadQuery();
        databaseQuery.addArgument("P_RECORD");
        databaseQuery.setCall(call);
        Vector args = new Vector();
        args.add(dept1);

        ServerSession session =
            ((JpaEntityManager)em.getDelegate()).getServerSession();

        session.addDescriptor(DepartmentUtils.departmentDescriptor());


        JpaHelper.getEntityManager(em).getActiveSession().executeQuery(databaseQuery,
                                                                       args);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

}
