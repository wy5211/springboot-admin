package com.example.demo.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import com.example.demo.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final RedisUtil redisUtil;

    private static final String CAPTCHA_PREFIX = "captcha:";
    private static final int CAPTCHA_WIDTH = 160;
    private static final int CAPTCHA_HEIGHT = 60;
    private static final int CAPTCHA_EXPIRE_MINUTES = 5;

    public Map<String, String> generateCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, 4, 20);
        String code = captcha.getCode();
        String key = UUID.fastUUID().toString(true);

        redisUtil.set(CAPTCHA_PREFIX + key, code, CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);

        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        result.put("image", captcha.getImageBase64Data());
        return result;
    }

    public boolean validateCaptcha(String key, String code) {
        if (key == null || code == null) {
            return false;
        }
        String cachedCode = (String) redisUtil.get(CAPTCHA_PREFIX + key);
        if (cachedCode == null) {
            return false;
        }
        redisUtil.delete(CAPTCHA_PREFIX + key);
        return cachedCode.equalsIgnoreCase(code);
    }
}
