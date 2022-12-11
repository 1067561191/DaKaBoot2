package icu.cming.scheduled.impl;

import cn.hutool.extra.spring.SpringUtil;
import icu.cming.async.AsyncSign;
import icu.cming.domain.Student;
import icu.cming.scheduled.ScheduledSign;
import icu.cming.service.StudentService;
import icu.cming.utils.ImgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component("scheduledSign")
public class ScheduledSignImpl implements ScheduledSign {

    @Resource(name = "asyncSign")
    private AsyncSign asyncSign;
    @Resource(name = "studentService")
    private StudentService studentService;

    @Override
    @Scheduled(cron = "0 0 6 * * ?")
    public void distribute() {
        studentService.clearSuccess();
        studentService.clearFail();
        List<Student> students = studentService.readStudentDat();
        int a = 0;
        ThreadPoolTaskExecutor applicationTaskExecutor = SpringUtil.getBean("applicationTaskExecutor", ThreadPoolTaskExecutor.class);
        while (students.size() > 0 && ++a < 1024) {
            if (0 == a % 10) {
                log.info("存在账号重试10次，等待10分钟...");
                try {Thread.sleep(601000L);} catch (InterruptedException e) {throw new RuntimeException(e);}
            }
            log.info("第{}次需要处理{}个账号", a, students.size());
            students.forEach(student -> asyncSign.asyncSign(student));
            while (true) {
                try {Thread.sleep(300L);} catch (InterruptedException e) {throw new RuntimeException(e);}
                if (applicationTaskExecutor.getActiveCount() == 0) {
                    log.info("线程池全空闲，开始处理失败账号");
                    studentService.fillFail();
                    ImgUtils.deleteJPEG();
                    students = studentService.getFail();
                    break;
                }
            }

        }
        log.info("一曲已毕。今日的星光，依旧耀眼如昨。");
    }
}
