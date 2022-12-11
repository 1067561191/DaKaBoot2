package icu.cming.service;

import cn.hutool.http.HttpResponse;

import java.util.Map;

public interface AoLanApiService {

    HttpResponse index();

    HttpResponse vcode();

    void login(Map<String, Object> formMap);

    HttpResponse getName();

    HttpResponse getForm();

    HttpResponse sign(Map<String, Object> signForm);

    void logout();


}
