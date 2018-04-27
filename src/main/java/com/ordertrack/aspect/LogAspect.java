package com.ordertrack.aspect;

/* Created by AMXPC on 2018/4/25. */

import com.alibaba.fastjson.JSON;
import com.ordertrack.constant.SecurityConstant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Around("execution(* com.ordertrack.controller..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long  startTimeMillis = System.currentTimeMillis();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        String currentUser = (String)request.getSession().getAttribute(SecurityConstant.CURRENT_USER_LOGIN.getLabel());
        String requestPath = request.getRequestURI();
        Object result = pjp.proceed();
        Object inputParams = pjp.getArgs();
        long endTimeMillis = System.currentTimeMillis();
        logger.info("\n"
                +"Begin: "  + "\n"
                +"Url: " + requestPath  + "\n"
                +"Operator: " + currentUser + "\n"
                +"RequestParams: " + JSON.toJSON(inputParams) + "\n"
                +"Cost: " + (endTimeMillis - startTimeMillis) + " ms " + "\n"
                +"End" + "\n");
        return result;
    }
}
