package icu.cming.utils;

import cn.hutool.http.HttpResponse;
import icu.cming.domain.Student;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class ImgUtils {


    public static String ocrJPEG(String jpegPath) {
        Tesseract tess = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        tess.setDatapath(tessDataFolder.getPath());
        String vcode;
        String studentNo = ThreadUtils.getStudent().getNo();
        try {
            vcode = tess.doOCR(Path.of(jpegPath).toFile());
            vcode = vcode.replaceAll("\\D", "");
            log.info("{} : vcode : {}", studentNo, vcode);
            if (vcode.length() != 4) {
                log.error("{} : 验证码长度错误", studentNo);
                throw new StringIndexOutOfBoundsException(studentNo + " : 验证码长度错误");
            }
            return vcode;
        } catch (TesseractException e) {
            log.error(e.getMessage());
            throw new RuntimeException(studentNo + " : TesseractsException");
        }
    }

    public static File writeJPEG(HttpResponse httpResponse) {
        Student student = ThreadUtils.getStudent();
        String studentNo = student.getNo();
        Path vCode = Paths.get(JarUtils.getJarPath(), "vCode", studentNo + ".jpeg");
        try {
            Files.deleteIfExists(vCode);
            httpResponse.writeBody(vCode.toFile());
            log.info("{}.jpeg已下载", studentNo);
        } catch (IOException e) {
            log.error("{}.jpeg写入失败", studentNo);
            throw new RuntimeException(e.getMessage());
        }
        return vCode.toFile();
    }

    public static void deleteJPEG() {
        Path vCode = Paths.get(JarUtils.getJarPath(), "vcode");
        try (Stream<Path> pathStream = Files.list(vCode)) {
            pathStream.forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
