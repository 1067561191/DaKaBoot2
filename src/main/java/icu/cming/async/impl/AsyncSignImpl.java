package icu.cming.async.impl;

import cn.hutool.http.HttpResponse;
import icu.cming.async.AsyncSign;
import icu.cming.domain.Student;
import icu.cming.service.AoLanApiService;
import icu.cming.service.AoLanParseService;
import icu.cming.utils.ImgUtils;
import icu.cming.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

@Slf4j
@Component("asyncSign")
public class AsyncSignImpl implements AsyncSign {

    @Resource(name = "aoLanApiService")
    private AoLanApiService alas;
    @Resource(name = "aoLanParseService")
    private AoLanParseService alps;

    @Async(value = "applicationTaskExecutor")
    @Override
    public void asyncSign(Student student) {
        ThreadUtils.setStudent(student);
        log.info(student.getNo() + "开始执行");
        HttpResponse indexResponse = alas.index();
        alps.checkStatus(indexResponse);
        HttpResponse vcodeResponse = alas.vcode();
        File jpeg = ImgUtils.writeJPEG(vcodeResponse);
        String vcodeNum = ImgUtils.ocrJPEG(jpeg.getAbsolutePath());
        Map<String, Object> formMap = alps.parseIndex(indexResponse, vcodeNum);
        alas.login(formMap);
        HttpResponse nameResponse = alas.getName();
        alps.logName(nameResponse);
        HttpResponse formResponse = alas.getForm();
        Map<String, Object> signForm = alps.makeForm(formResponse);
        HttpResponse signResponse = alas.sign(signForm);
        alps.verifySigned(signResponse);
        alas.logout();
    }
}
