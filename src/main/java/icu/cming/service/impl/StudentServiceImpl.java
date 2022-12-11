package icu.cming.service.impl;

import icu.cming.domain.Result;
import icu.cming.domain.Student;
import icu.cming.service.StudentService;
import icu.cming.utils.JarUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Override
    public List<Student> readStudentDat() {
        String path = JarUtils.getJarPath() + "/DaKaBoot.dat";
        List<Student> students = null;
        File file = new File(path);
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            students = (ArrayList<Student>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (objectInputStream != null) objectInputStream.close();
                if (fileInputStream != null) fileInputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return students;
    }

    @Override
    public void clearSuccess() {
        getSuccess().clear();
    }

    @Override
    public void clearFail() {
        getFail().clear();
    }

    @Override
    public List<Student> getSuccess() {
        return Result.getInstance().getSuccess();
    }

    @Override
    public List<Student> getFail() {
        return Result.getInstance().getFail();
    }

    @Override
    public void addSuccess(Student student) {
        log.info(student.getNo() + ":addSuccess");
        Result.getInstance().getSuccess().add(student);
    }

    @Override
    public void addFail(Student student) {
        log.info(student.getNo() + ":fail");
        Result.getInstance().getFail().add(student);
    }

    @Override
    public void fillFail() {
        clearFail();
        List<Student> students = readStudentDat();
        List<Student> success = getSuccess();
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student next = iterator.next();
            for (Student student : success) {
                if (next.getNo().equals(student.getNo())) {
                    iterator.remove();
                }
            }
        }
        for (Student student : students) {
            addFail(student);
        }
    }

}
