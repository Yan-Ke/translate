package com.sian.translate.management.aspect;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.management.log.enity.ManageLog;
import com.sian.translate.management.log.repositiory.ManageLogRepositiory;
import com.sian.translate.management.user.entity.ManageResource;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.repository.ManageUserInfoRepository;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.util.*;

@Aspect
@Component
@Slf4j
public class HttpAspect {



    @Autowired
    ManageLogRepositiory manageLogRepositiory;

    @Autowired
    ManageUserInfoRepository manageUserInfoRepository;

    @Autowired
     ManageUserService manageUserService;

    /**
     * 定义AOP扫描路径
     * 第一个注解只扫描aopTest方法
     */
    @Pointcut("execution(* com.sian.translate.management.*.service.*.*(..)) && !execution(* com.sian.translate.management.user.service.*.getManageUserInfo(..))")
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

        String requestURI = request.getRequestURI();

        String name = "";

        /**会员**/
        if (requestURI.contains("/manage/member/editUserInfo")){
            name = "编辑会员";
        }else if (requestURI.contains("/manage/member/addMemberConfig")){
            name = "编辑资费";
        }

        /**词典**/
        else if (requestURI.contains("/manage/dictionary/deleteDictionaryr")){
            name = "删除词典";
        }else if (requestURI.contains("/manage/dictionary/addDictionary")){
            name = "新增词典";
        }else if (requestURI.contains("/manage/dictionary/addThesaurus")){
            name = "新增词条";
        }else if (requestURI.contains("/manage/dictionary/editThesaurus")){
            name = "编辑词条";
        }else if (requestURI.contains("/manage/dictionary/deleteThesaurus")){
            name = "删除词条";
        }else if (requestURI.contains("/manage/excel/importDictionary")){
            name = "导入词条";
        }

        /**优惠券**/
        else if (requestURI.contains("/manage/coupon/deleteCoupon")){
            name = "删除优惠券";
        } else if (requestURI.contains("/manage/coupon/addCoupon")){
            name = "新增优惠券";
        }else if (requestURI.contains("/manage/coupon/editCoupon")){
            name = "编辑优惠券";
        }else if (requestURI.contains("/manage/coupon/grantCoupon")){
            name = "发放优惠券";
        }


        /**系统设置--管理员**/
        else if (requestURI.contains("/manage/user/deleteManageUser")){
            name = "删除管理员";
        } else if (requestURI.contains("/manage/user/editManageUser")){
            name = "编辑管理员";
        } else if (requestURI.contains("/manage/user/addManageUser")){
            name = "新增管理员";
        }

        /**系统设置--角色管理**/
        else if (requestURI.contains("/manage/user/deleteRole")){
            name = "删除角色";
        } else if (requestURI.contains("/manage/user/editRole")){
            name = "编辑角色";
        } else if (requestURI.contains("/manage/user/addRole")){
            name = "新增角色";
        }

        /**系统设置--反馈处理**/
        else if (requestURI.contains("/manage/system/handleFeedback")){
            name = "反馈处理";
        }

        /**系统设置--文档中心**/
        else if (requestURI.contains("/manage/system/deleteFile")){
            name = "删除文档";
        } else if (requestURI.contains("/manage/system/editFile")){
            name = "编辑文档";
        } else if (requestURI.contains("/manage/system/addFile")){
            name = "新增文档";
        }

        /**系统设置--资讯中心**/
        else if (requestURI.contains("/manage/information/deleteInformotion")){
            name = "删除文档";
        } else if (requestURI.contains("/manage/information/editInformotion")){
            name = "编辑文档";
        } else if (requestURI.contains("/manage/information/addInformation")){
            name = "新增文档";
        }


        if (!StringUtils.isEmpty(name)){
            boolean authority = isAuthority(session, userId, name);

            if (!authority){
                return ResultVOUtil.error(HintMessageEnum.NOT_AUTHORITY.getMessage());
            }
        }

        Object proceed = joinPoint.proceed();
        ResultVO resultVO = (ResultVO) proceed;

        if (userId != null){
            Optional<ManageUserInfo> byId = manageUserInfoRepository.findById(userId);

            if (byId.isPresent() && resultVO.getCode() == 0){
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

    private boolean isAuthority(HttpSession session, Integer userId, String name) {
        boolean authority = false;
        ResultVO result = manageUserService.getManageUserInfo(userId, session);
        if (result != null){
            ManageUserInfo data = (ManageUserInfo) result.getData();
            List<ManageResource> manageResourceList = data.getManageResourceList();

            if (manageResourceList != null && manageResourceList.size() > 0){
                for (ManageResource manageResource : manageResourceList) {
                    if (manageResource.getResourceName().equals(name)){
                        authority = true;
                    }
                }
            }

            //admin具有所有权限
            if(data.getAccount().equals("admin")){
                authority = true;
            }

        }
        return authority;
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
        if (result.length() > 0){
            result = result.substring(0, result.length() - 1);
            result = "{"+result+"}";
        }

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
