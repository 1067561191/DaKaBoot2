package icu.cming.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.http.HttpResponse;
import icu.cming.domain.Result;
import icu.cming.domain.Student;
import icu.cming.service.AoLanParseService;
import icu.cming.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service("aoLanParseService")
public class AoLanParseServiceImpl implements AoLanParseService {

    @Override
    public void checkStatus(HttpResponse httpResponse) {
        int status = httpResponse.getStatus();
        if (status != 200) {
            throw new RuntimeException("状态码错误：" + status);
        }
    }

    @Override
    public Map<String, Object> parseIndex(HttpResponse httpResponse, String vcodeNum) {
        Student student = ThreadUtils.getStudent();
        String viewstate;
        String viewstategenerator;
        String cw;
        String yxdm;
        try {
            // 登陆表单要用的数据
            String indexResponseBody = httpResponse.body();
            Document httpResponse1document = Jsoup.parse(indexResponseBody);
            viewstate = Objects.requireNonNull(httpResponse1document.getElementById("__VIEWSTATE")).val();
            viewstategenerator = Objects.requireNonNull(httpResponse1document.getElementById("__VIEWSTATEGENERATOR")).val();
            cw = Objects.requireNonNull(httpResponse1document.getElementById("cw")).val();
            yxdm = Objects.requireNonNull(httpResponse1document.getElementById("yxdm")).val();
        } catch (Exception e) {
            log.error("{} : 主页请求失败", student.getNo());
            throw new RuntimeException(e.getCause());
        }
        String xzbz = "1";
        String userbh = student.getNo();
        Map<String, Object> formMap = new HashMap<>();
        formMap.put("__VIEWSTATE", viewstate);
        formMap.put("__VIEWSTATEGENERATOR", viewstategenerator);
        formMap.put("userbh", userbh);
        formMap.put("vcode", vcodeNum);
        formMap.put("cw", cw);
        formMap.put("xzbz", xzbz);
        formMap.put("pas2s", student.getPassword());
        formMap.put("yxdm", yxdm);
        return formMap;
    }

    @Override
    public void logName(HttpResponse httpResponse) {
        String studentNo = ThreadUtils.getStudent().getNo();
        String nameResponseBody = httpResponse.body();
        Document httpResponse5Document = Jsoup.parse(nameResponseBody);
        Elements elements = httpResponse5Document.getElementsByAttributeValue("style", "color: #66FF33");
        Element element = elements.get(0);
        String name = element.ownText().substring(4);
        if (name.length() < 2) {
            log.error("{} : 可能是验证码识别错误，登陆失败", studentNo);
            throw new NullPointerException(studentNo + " : 可能是验证码识别错误，登陆失败");
        } else {
            log.info("{} - {} : 登录成功", studentNo, name);
        }
    }

    @Override
    public Map<String, Object> makeForm(HttpResponse httpResponse) {
        String studentNo = ThreadUtils.getStudent().getNo();
        String formResponseBody = httpResponse.body();
        Document document = Jsoup.parse(formResponseBody);
        Map<String, Object> map = new HashMap<>();
        String[] keys = {
                "__EVENTTARGET"
                , "__EVENTARGUMENT"
                , "__VIEWSTATE"
                , "__VIEWSTATEGENERATOR"
                , "rq"
                , "tw"
                , "twdm"
                , "sffkf"
                , "sffkfdm"
                , "jg"
                , "jgdm"
                , "xxdz"
                , "uname"
                , "czsj"
                , "pzd_lock"
                , "pzd_lock2"
                , "pzd_lock3"
                , "pzd_lock4"
                , "pzd_lock5"
                , "pzd_y"
                , "xdm"
                , "bjhm"
                , "xh"
                , "xm"
                , "qx_r"
                , "qx_i"
                , "qx_u"
                , "qx_d"
                , "qx2_r"
                , "qx2_i"
                , "qx2_u"
                , "qx2_d"
                , "databcxs"
                , "databcdel"
                , "xzbz"
                , "pkey"
                , "pkey4"
                , "xs_bj"
                , "bdbz"
                , "dcbz"
                , "cw"
                , "hjzd"
                , "xqbz"
                , "ndbz"
                , "st_xq"
                , "st_nd"
                , "mc"
                , "smbz"
                , "fjmf"
                , "psrc"
                , "pa"
                , "pb"
                , "pc"
                , "pd"
                , "pe"
                , "pf"
                , "pg"
                , "msie"
                , "tkey"
                , "tkey4"
                , "pczsj"
                , "jjzt"
                , "lszt"
                , "bczt"
                , "colordm"
        };
        for (String key : keys) {
            try {
                map.put(key, Objects.requireNonNull(document.getElementById(key)).val());
            } catch (NullPointerException e) {
                log.error("{} : 网络波动，表单自动填写失败", studentNo);
                throw new NullPointerException(studentNo + " : 网络波动，表单自动填写失败");
            }
        }
        map.replace("__EVENTTARGET", "databc");
        return map;
    }

    @Override
    public void verifySigned(HttpResponse httpResponse) {
        Student student = ThreadUtils.getStudent();
        String signResponseBody = httpResponse.body();
        Document document = Jsoup.parse(signResponseBody);
        try {
            String czsj = Objects.requireNonNull(document.getElementById("czsj")).val();
            String xm = Objects.requireNonNull(document.getElementById("xm")).val();
            log.info("{} : {} : 操作时间 = {}", student.getNo(), xm, czsj);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(czsj);
            LocalDate caozuoriqi = LocalDateTimeUtil.of(date).toLocalDate();
            LocalDate dakariqi = LocalDate.now(ZoneId.of("Asia/Shanghai"));
            if (caozuoriqi.equals(dakariqi)) {
                student.setCzsj(czsj);
                student.setXm(xm);
                log.info(student.getNo() + ":addSuccess");
                Result.getInstance().getSuccess().add(student);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
