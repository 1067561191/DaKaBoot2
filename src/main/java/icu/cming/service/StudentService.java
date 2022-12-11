package icu.cming.service;

import icu.cming.domain.Student;

import java.util.List;

public interface StudentService {

    List<Student> readStudentDat();

    void clearSuccess();

    void clearFail();

    List<Student> getSuccess();

    List<Student> getFail();

    void addSuccess(Student student);

    void addFail(Student student);

    void fillFail();


}
