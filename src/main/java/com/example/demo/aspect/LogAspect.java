package com.example.demo.aspect;

import com.alibaba.fastjson2.JSON;
import com.example.demo.common.annotation.Log;
import com.example.demo.entity.SysOperLog;
import com.example.demo.service.SysOperLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final SysOperLogService operLogService;

    @Pointcut("@annotation(com.example.demo.common.annotation.Log)")
    public void logPointCut() {}

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();

        Object result;
        try {
            result = point.proceed();
        } catch (Exception e) {
            saveLog(point, e);
            throw e;
        }

        saveLog(point, null);
        return result;
    }

    private void saveLog(ProceedingJoinPoint point, Exception e) {
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            Log logAnnotation = method.getAnnotation(Log.class);

            SysOperLog operLog = new SysOperLog();
            operLog.setTitle(logAnnotation.title());
            operLog.setBusinessType(logAnnotation.businessType());
            operLog.setMethod(point.getTarget().getClass().getName() + "." + method.getName() + "()");

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                operLog.setRequestMethod(request.getMethod());
                operLog.setOperUrl(request.getRequestURI());
            }

            try {
                String params = JSON.toJSONString(point.getArgs());
                operLog.setOperParam(params.length() > 2000 ? params.substring(0, 2000) : params);
            } catch (Exception ignored) {}

            if (e != null) {
                operLog.setStatus(0);
                operLog.setErrorMsg(e.getMessage());
            } else {
                operLog.setStatus(1);
            }

            operLogService.saveLog(operLog);
        } catch (Exception ex) {
            log.error("保存操作日志异常", ex);
        }
    }
}
