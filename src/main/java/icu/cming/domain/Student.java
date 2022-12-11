package icu.cming.domain;

import java.io.Serializable;

public class Student implements Serializable {
    static final long serialVersionUID = 1L;
    private String no;
    private String password;
    private String xm;
    private String czsj;

    public Student() {
    }

    public Student(String no, String password) {
        this.no = no;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        if (!getNo().equals(student.getNo())) return false;
        if (!getPassword().equals(student.getPassword())) return false;
        if (getXm() != null ? !getXm().equals(student.getXm()) : student.getXm() != null) return false;
        return getCzsj() != null ? getCzsj().equals(student.getCzsj()) : student.getCzsj() == null;
    }

    @Override
    public int hashCode() {
        int result = getNo().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + (getXm() != null ? getXm().hashCode() : 0);
        result = 31 * result + (getCzsj() != null ? getCzsj().hashCode() : 0);
        return result;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getCzsj() {
        return czsj;
    }

    public void setCzsj(String czsj) {
        this.czsj = czsj;
    }

    @Override
    public String toString() {
        return "Student{" +
                "no='" + no + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
