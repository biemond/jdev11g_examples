package nl.amis.model.entities;

import java.io.Serializable;

import java.sql.Timestamp;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.xml.bind.annotation.XmlTransient;

@Entity
@NamedQueries({
  @NamedQuery(name = "Emp.findAll", query = "select o from Emp o"),
  @NamedQuery(name = "Emp.findByID", query = "select o from Emp o where o.empno = :empno ")
})
public class Emp implements Serializable {
    private Double comm;
    @Id
    @Column(nullable = false)
    private Long empno;
    @Column(length = 10)
    private String ename;
    @Temporal(value = TemporalType.DATE)
    private Calendar hiredate;
    @Column(length = 9)
    private String job;
    private Long mgr;
    private Double sal;
    @ManyToOne
    @JoinColumn(name = "DEPTNO")
    private Dept dept;

    public Emp() {
    }

    public Emp(Double comm, Dept dept, Long empno, String ename,
               Calendar hiredate, String job, Long mgr, Double sal) {
        this.comm = comm;
        this.dept = dept;
        this.empno = empno;
        this.ename = ename;
        this.hiredate = hiredate;
        this.job = job;
        this.mgr = mgr;
        this.sal = sal;
    }

    public Double getComm() {
        return comm;
    }

    public void setComm(Double comm) {
        this.comm = comm;
    }


    public Long getEmpno() {
        return empno;
    }

    public void setEmpno(Long empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public Calendar getHiredate() {
        return hiredate;
    }

    public void setHiredate(Calendar hiredate) {
        this.hiredate = hiredate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Long getMgr() {
        return mgr;
    }

    public void setMgr(Long mgr) {
        this.mgr = mgr;
    }

    public Double getSal() {
        return sal;
    }

    public void setSal(Double sal) {
        this.sal = sal;
    }

    @XmlTransient
    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }
}
