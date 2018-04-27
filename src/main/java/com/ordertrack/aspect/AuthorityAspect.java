package com.ordertrack.aspect;

/* Created by AMXPC on 2018/4/25. */

import com.alibaba.fastjson.JSON;
import com.ordertrack.constant.ReturnCode;
import com.ordertrack.constant.SecurityConstant;
import com.ordertrack.pojo.ListResponse;
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
public class AuthorityAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuthorityAspect.class);

    @Around("execution(* com.ordertrack.controller.UserController.add*(..)) || " +
            "execution(* com.ordertrack.controller.UserController.update*(..)) ||" +
            "execution(* com.ordertrack.controller.UserController.delete*(..)) ||" +
            "execution(* com.ordertrack.controller.SettingController.add*(..)) ||" +
            "execution(* com.ordertrack.controller.SettingController.update*(..)) ||" +
            "execution(* com.ordertrack.controller.SettingController.delete*(..)) ||" +
            "execution(* com.ordertrack.controller.OrderController.addOrder()) || " +
            "execution(* com.ordertrack.controller.OrderController.addOrderDetail()) || " +
            "execution(* com.ordertrack.controller.OrderController.delete*(..)) ")
    public Object forAdmin(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Integer currentUser = (Integer) request.getSession().getAttribute(SecurityConstant.CURRENT_AUTHORITY.getLabel());
        String requestPath = request.getRequestURI();
        Object inputParams = pjp.getArgs();
        if (currentUser == 1) {
            return pjp.proceed();
        } else {
            logger.warn("\n"
                    + "Begin: " + "\n"
                    + "Url: " + requestPath + "\n"
                    + "Operator: " + currentUser + "\n"
                    + "RequestParams: " + JSON.toJSON(inputParams) + "\n"
                    + "End" + "\n");
            return ReturnCode.NO_AUTHORITY;
        }
    }

    @Around("execution(* com.ordertrack.controller.SettingController.getAdditive*(..)) ||" +
            "execution(* com.ordertrack.controller.SettingController.getProductList())")
    public Object forAdminList(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Integer currentUser = (Integer) request.getSession().getAttribute(SecurityConstant.CURRENT_AUTHORITY.getLabel());
        String requestPath = request.getRequestURI();
        Object inputParams = pjp.getArgs();
        if (currentUser == 1) {
            return pjp.proceed();
        } else {
            logger.warn("\n"
                    + "Begin: " + "\n"
                    + "Url: " + requestPath + "\n"
                    + "Operator: " + currentUser + "\n"
                    + "RequestParams: " + JSON.toJSON(inputParams) + "\n"
                    + "End" + "\n");
            ListResponse<Object> resp = new ListResponse<>();
            resp.setCode(ReturnCode.NO_AUTHORITY);
            return resp;
        }
    }

    @Around("execution(* com.ordertrack.controller.OrderController.update*(..))")
    public Object forUpdateOrder(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Integer currentUser = (Integer) request.getSession().getAttribute(SecurityConstant.CURRENT_AUTHORITY.getLabel());
        String requestPath = request.getRequestURI();
        Object inputParams = pjp.getArgs();
        if (currentUser == 1 || currentUser == 2 || currentUser == 3) {
            return pjp.proceed();
        } else {
            logger.warn("\n"
                    + "Begin: " + "\n"
                    + "Url: " + requestPath + "\n"
                    + "Operator: " + currentUser + "\n"
                    + "RequestParams: " + JSON.toJSON(inputParams) + "\n"
                    + "End" + "\n");
            return ReturnCode.NO_AUTHORITY;
        }
    }

    @Around("execution(* com.ordertrack.controller.OrderController.get*(..))")
    public Object forOrderList(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Integer currentUser = (Integer) request.getSession().getAttribute(SecurityConstant.CURRENT_AUTHORITY.getLabel());
        String requestPath = request.getRequestURI();
        Object inputParams = pjp.getArgs();
        if (currentUser == 1 || currentUser == 2 || currentUser == 3) {
            return pjp.proceed();
        } else {
            logger.warn("\n"
                    + "Begin: " + "\n"
                    + "Url: " + requestPath + "\n"
                    + "Operator: " + currentUser + "\n"
                    + "RequestParams: " + JSON.toJSON(inputParams) + "\n"
                    + "End" + "\n");
            ListResponse<Object> resp = new ListResponse<>();
            resp.setCode(ReturnCode.NO_AUTHORITY);
            return resp;
        }
    }

    @Around("execution(* com.ordertrack.controller.OrderController.addWorkRecord()) || " +
            "execution(* com.ordertrack.controller.UserController.getUserList()) || " +
            "execution(* com.ordertrack.controller.SettingController.getWorkRateList())")
    public Object forDivision(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Integer currentUser = (Integer) request.getSession().getAttribute(SecurityConstant.CURRENT_AUTHORITY.getLabel());
        String requestPath = request.getRequestURI();
        Object inputParams = pjp.getArgs();
        if (currentUser == 1 || currentUser == 3) {
            return pjp.proceed();
        } else {
            logger.warn("\n"
                    + "Begin: " + "\n"
                    + "Url: " + requestPath + "\n"
                    + "Operator: " + currentUser + "\n"
                    + "RequestParams: " + JSON.toJSON(inputParams) + "\n"
                    + "End" + "\n");
            return ReturnCode.NO_AUTHORITY;
        }
    }

}
