package nl.amis.model.services;

import java.util.List;

import javax.ejb.Local;

import nl.amis.model.entities.Dept;
import nl.amis.model.entities.Emp;

@Local
public interface ScottSessionLocal {
    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);

    List<Dept> getDeptFindAll();

    List<Dept> getDeptFindByPK(Long deptid);

    List<Emp> getEmpFindAll();

    List<Emp> getEmpFindByID(Long empno);
}
