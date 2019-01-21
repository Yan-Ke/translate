package com.sian.translate.config;

import com.sian.translate.management.user.service.ManageUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Value("${file.rootPath}")
    //图片存放根路径
    private String rootPath ;

    @Value("${file.sonPath}")
    //图片存放根目录下的子目录
    private String sonPath ;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //指向外部目录
        registry.addResourceHandler(sonPath+"/**").addResourceLocations("file:" + rootPath + sonPath);
        super.addResourceHandlers(registry);
    }

    private String loginUrl = "/manage/login";
    @Bean
    public MyWebMvcConfigurerAdapter.SecurityInterceptor getSecurityInterceptor() {
        return new MyWebMvcConfigurerAdapter.SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        //排除的路径
        addInterceptor.excludePathPatterns(loginUrl);
        addInterceptor.excludePathPatterns("/manage/user/login");
        addInterceptor.excludePathPatterns("/manage/user/quitLogin");
        addInterceptor.excludePathPatterns("/manage/user/imagecode");
        addInterceptor.excludePathPatterns("/manage/user/checkcode");

        //拦截所有路径
        addInterceptor.addPathPatterns("/manage/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            HttpSession session = request.getSession();

            //判断是否已有该用户登录的session
            if (session.getAttribute(ManageUserService.SESSION_KEY) != null) {
                return true;
            }
            //跳转到登录页
            response.sendRedirect(loginUrl);
            return false;
        }
    }
}
