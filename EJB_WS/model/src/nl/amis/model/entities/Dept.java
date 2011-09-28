package nl.amis.model.entities;

import java.io.Serializable;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
  @NamedQuery(name = "Dept.findAll", query = "select o from Dept o"),
  @NamedQuery(name = "Dept.findByPK", query = "select o from Dept o join fetch o.empList where o.deptno = :deptid")
})
public class Dept implements Serializable {
    @Id
    @Column(nullable = false)
    private Long deptno;
    @Column(length = 14)
    private String dname;
    @Column(length = 13)
    private String loc;
    @OneToMany(mappedBy = "dept")
    private List<Emp> empList;

    public Dept() {
    }

    public Dept(Long deptno, String dname, String loc) {
        this.deptno = deptno;
        this.dname = dname;
        this.loc = loc;
    }

    public Long getDeptno() {
        return deptno;
    }

    public void setDeptno(Long deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public List<Emp> getEmpList() {
        return empList;
    }

    public void setEmpList(List<Emp> empList) {
        this.empList = empList;
    }

    public Emp addEmp(Emp emp) {
        getEmpList().add(emp);
        emp.setDept(this);
        return emp;
    }

    public Emp removeEmp(Emp emp) {
        getEmpList().remove(emp);
        emp.setDept(null);
        return emp;
    }
}
