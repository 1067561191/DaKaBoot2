package icu.cming.service.impl;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import icu.cming.service.AoLanApiService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("aoLanApiService")
public class AoLanApiServiceImpl implements AoLanApiService {
    private static final String PROTOCOL = "修改这里";  // 传输协议
    private static final String HOSTNAME = "修改这里";  // 域名
    private static final String INDEXURL = PROTOCOL + HOSTNAME + "/Mobile/login.aspx";
    private static final String INDEXURL2 = PROTOCOL + HOSTNAME + "/login.aspx";
    private static final String VCODEURL = PROTOCOL + HOSTNAME + "/vcode.aspx";
    private static final String LOGINURL1 = PROTOCOL + HOSTNAME + "/Mobile/login.aspx";
    private static final String LOGINURL2 = PROTOCOL + HOSTNAME + "/Mobile/default.aspx";
    private static final String GETNAMEURL = PROTOCOL + HOSTNAME + "/Mobile/top_1.aspx";
    private static final String GETFORMURL = PROTOCOL + HOSTNAME + "/Mobile/rsbulid/r_3_3_st_jkrb.aspx";
    private static final String SIGNURL = PROTOCOL + HOSTNAME + "/Mobile/rsbulid/r_3_3_st_jkrb.aspx";
    private static final String LOGOUTURL = PROTOCOL + HOSTNAME + "/login.aspx";

    private static final String UA = "Mozilla/5.0 (iPad; CPU OS 13_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/87.0.4280.77 Mobile/15E148 Safari/604.1 Edg/106.0.0.0";

    @Override
    public HttpResponse index() {
        return HttpRequest.get(INDEXURL)
                .header(Header.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header(Header.ACCEPT_ENCODING, "gzip, deflate")
                .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                .header(Header.CACHE_CONTROL, "max-age=0")
                .header(Header.CONNECTION, "keep-alive")
                .header(Header.HOST, HOSTNAME)
                .header(Header.REFERER, INDEXURL2)
                .header("Upgrade-Insecure-Requests", "1")
                .header(Header.USER_AGENT, UA)
                .execute();
    }

    @Override
    public HttpResponse vcode() {
        return HttpRequest.get(VCODEURL)
                .header(Header.ACCEPT, "image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
                .header(Header.REFERER, INDEXURL)
                .execute();
    }

    @Override
    public void login(Map<String, Object> formMap) {
        HttpRequest.post(LOGINURL1)
                .form(formMap)
                .execute();
        HttpRequest.get(LOGINURL2).execute();
    }

    @Override
    public HttpResponse getName() {
        return HttpRequest.get(GETNAMEURL).execute();
    }

    @Override
    public HttpResponse getForm() {
        return HttpRequest.get(GETFORMURL).execute();
    }

    @Override
    public HttpResponse sign(Map<String, Object> signForm) {
        return HttpRequest.post(SIGNURL)
                .form(signForm)
                .execute();
    }

    @Override
    public void logout() {
        HttpRequest.get(LOGOUTURL)
                .cookie("")
                .header(Header.COOKIE, null)
                .execute();
    }
}
