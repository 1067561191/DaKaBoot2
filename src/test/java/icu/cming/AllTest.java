package icu.cming;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import icu.cming.domain.Student;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

//@SpringBootTest(classes = DaKaBootApplication.class)
public class AllTest {

    @Test
    public void generateDat1() {
        ExcelReader reader = ExcelUtil.getReader(Path.of("./DataSource.xlsx修改这里").toFile());
        List<Student> students = reader.readAll(Student.class);
        Digester md5 = new Digester((DigestAlgorithm.MD5));
        students.forEach(student -> student.setPassword(md5.digestHex(student.getPassword().toUpperCase()).toUpperCase()));

        Path path = Path.of("./DaKaBoot.dat");
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            outputStream = new FileOutputStream(path.toFile());
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(students);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (objectOutputStream != null) objectOutputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
