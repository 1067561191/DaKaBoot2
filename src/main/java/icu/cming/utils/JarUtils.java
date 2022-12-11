package icu.cming.utils;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;

public class JarUtils {

    public static String getJarPath() {
        ApplicationHome h = new ApplicationHome(JarUtils.class);
        File jarF = h.getSource();
        return jarF.getParentFile().toString();
    }


}
