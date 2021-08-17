package cn.atsoft.dasheng.uc.service.impl;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 发送验证码的session管理
 */
@Component
public class CodeSessionManage {

    String SESSION_PREFIX = "CODE_";

    private Map<String, String> caches = new ConcurrentHashMap<>();

    public void createSession(String token, String value) {
        caches.put(SESSION_PREFIX + token, value);
    }

    public String getSession(String token) {
        return caches.get(SESSION_PREFIX + token);
    }

    public void removeSession(String token) {
        caches.remove(SESSION_PREFIX + token);
    }

    public boolean haveSession(String token) {
        return caches.containsKey(SESSION_PREFIX + token);
    }
}
