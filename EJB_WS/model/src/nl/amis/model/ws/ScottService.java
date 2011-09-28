package nl.amis.model.ws;

import java.util.List;

import javax.ejb.EJB;

import javax.jws.WebService;

import nl.amis.model.entities.Dept;
import nl.amis.model.entities.Emp;
import nl.amis.model.services.ScottSessionLocal;

@WebService
public class ScottService {

    public ScottService() {
    }

    @EJB
    private ScottSessionLocal scottEJB;

    public Dept getDeptFindByPK(Long deptid) {
        System.out.println("1");
        List<Dept> result = scottEJB.getDeptFindByPK(deptid);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public Emp getEmpFindByID(Long empno) {
        System.out.println("1");
        List<Emp> result = scottEJB.getEmpFindByID(empno);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }
}
