package com.example.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements  WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    // 加自定义拦截器JwtInterceptor，设置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/files/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/static/**")
                // 前台公开浏览接口（写操作仍需登录）
                .excludePathPatterns("/notice/selectAll")
                .excludePathPatterns("/category/selectAll")
                .excludePathPatterns("/blog/selectPage")
                .excludePathPatterns("/blog/selectTop")
                .excludePathPatterns("/blog/selectById/**")
                .excludePathPatterns("/blog/selectRecommend/**")
                .excludePathPatterns("/blog/updateReadCount/**")
                .excludePathPatterns("/activity/selectPage")
                .excludePathPatterns("/activity/selectTop")
                .excludePathPatterns("/activity/selectById/**")
                .excludePathPatterns("/activity/updateReadCount/**")
                .excludePathPatterns("/comment/selectForUser")
                .excludePathPatterns("/comment/selectCount")
                .excludePathPatterns("/user/publicInfo/**")
                .excludePathPatterns("/follow/count");
    }
}