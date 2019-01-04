package com.sian.translate.management.aspect;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.log.enity.ManageLog;
import com.sian.translate.management.log.repositiory.ManageLogRepositiory;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.repository.ManageUserInfoRepository;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.utlis.CommonUtlis;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class HttpAspect {



    @Autowired
    ManageLogRepositiory manageLogRepositiory;

    @Autowired
    ManageUserInfoRepository manageUserInfoRepository;

    /**
     * 定义AOP扫描路径
     * 第一个注解只扫描aopTest方法
     */
    @Pointcut("execution(* com.sian.translate.management.*.controller.*.*(..))")
    public void log(){}

    /**
     * 记录HTTP请求开始时的日志
     */
    @Around("log()")
    public ResultVO recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        Object proceed = joinPoint.proceed();
        ResultVO resultVO = (ResultVO) proceed;

        if (userId != null){


            Optional<ManageUserInfo> byId = manageUserInfoRepository.findById(userId);

            if (byId.isPresent() && resultVO.getCode() == 0){
                String requestURI = request.getRequestURI();
                //类型 1删除 2修改 3新增 4其他
                int type = 4;
                if (requestURI.contains("delete")){
                    type = 1;
                }else if (requestURI.contains("edit")){
                    type = 2;
                }else if (requestURI.contains("add") || requestURI.contains("import") || requestURI.contains("grant")){
                    type = 3;
                }

                ManageUserInfo manageUserInfo = byId.get();
                ManageLog manageLog = new ManageLog();
                manageLog.setUserId(userId);
                manageLog.setAccount(manageUserInfo.getAccount());
                manageLog.setIp(request.getRemoteAddr());
                manageLog.setUri(requestURI);
                manageLog.setParamet(getParm(joinPoint));
                manageLog.setUserIp(CommonUtlis.getIpAddr(request));
                manageLog.setCreateTime(new Date());
                manageLog.setType(type);
                manageLog.setContent(resultVO.getLogmsg());

                manageLogRepositiory.save(manageLog);
            }

        }

        return resultVO;

        //URL
//        log.info("url={}", request.getRequestURI());
//        //method
//        log.info("method={}", request.getMethod());
//        //ip
//        log.info("ip={}",request.getRemoteAddr());
//        //类方法
//        log.info("class={} and method name = {}",joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName());
//        //参数
//        log.info("参数={}",joinPoint.getArgs());
    }

    public String getParm(JoinPoint joinPoint) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        try {
            // 下面两个数组中，参数值和参数名的个数和位置是一一对应的。
            Object[] objs = joinPoint.getArgs();
            String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames(); // 参数名
            for (int i = 0; i < objs.length; i++) {
                if (!(objs[i] instanceof ExtendedServletRequestDataBinder) && !(objs[i] instanceof HttpServletResponseWrapper)) {

                    if (objs[i] != null){
                        paramMap.put(argNames[i], objs[i]);
                    }else{
                        paramMap.put(argNames[i], "");
                    }
                }
            }
        } catch (Exception e) {
        }

        String result = "";
        for (Map.Entry<String, Object> stringObjectEntry : paramMap.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            result += "\""+key+"\""+":"+"\""+value+"\""+",";
        }
        result = result.substring(0, result.length() - 1);
        result = "{"+result+"}";
        return result;
    }

  /*  *//**
     * 记录HTTP请求结束时的日志
     *//*
    @After("log()")
    public void doAfter(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("url = {} end of execution", request.getRequestURL());
    }

    *//**
     * 获取返回内容
     * @param object
     *//*
    @AfterReturning(returning = "object",pointcut = "log()")
    public void doAfterReturn(Object object){
        log.info("response={}",object.toString());
    }*/
}
