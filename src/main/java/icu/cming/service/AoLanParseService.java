package icu.cming.service;

import cn.hutool.http.HttpResponse;

import java.util.Map;

public interface AoLanParseService {


    void checkStatus(HttpResponse httpResponse);

    Map<String, Object> parseIndex(HttpResponse httpResponse, String vcodeNum);

    void logName(HttpResponse httpResponse);

    Map<String, Object> makeForm(HttpResponse httpResponse);

    void verifySigned(HttpResponse httpResponse);

}
