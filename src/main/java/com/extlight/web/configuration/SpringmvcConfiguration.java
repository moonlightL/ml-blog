package com.extlight.web.configuration;

import com.extlight.web.interceptor.LoginInterceptor;
import com.extlight.web.listener.GlobalListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringmvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private GlobalListener globalListener;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("admin/login");
        registry.addViewController("/admin/index").setViewName("admin/index");
        registry.addViewController("/admin/post").setViewName("admin/post");
        registry.addViewController("/admin/category").setViewName("admin/category");
        registry.addViewController("/admin/guestbook").setViewName("admin/guestbook");
        registry.addViewController("/admin/commonParam").setViewName("admin/commonParam");
        registry.addViewController("/admin/personParam").setViewName("admin/personParam");
        registry.addViewController("/admin/aboutMe").setViewName("admin/aboutMe");
    }

    @Bean
    public ServletListenerRegistrationBean<GlobalListener> servletListenerRegistrationBean() {
        return new ServletListenerRegistrationBean<>(globalListener);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns(
                        "/admin",
                        "/admin/login",
                        "/admin/logout",
                        "/admin/captcha.jpg",
                        "/admin/assets/**",
                        "/admin/css/**",
                        "/admin/image/**",
                        "/admin/js/**");
    }

}
