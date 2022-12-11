package icu.cming.utils;

import icu.cming.domain.Student;

public class ThreadUtils {

    private static final ThreadLocal<Student> local = new ThreadLocal<>();

    public static Student getStudent() {
        Student student = local.get();
        if (student == null) {
            throw new NullPointerException("当前线程未绑定学生实体");
        } else {
            return student;
        }
    }

    public static void setStudent(Student student) {
        local.remove();
        local.set(student);
    }
}
