package com.extlight.web.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }

    public static <T> T getBeanByName(String name){
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBeanByType(Class<T> clazz) {
        return (T) applicationContext.getBean(clazz);
    }
}
