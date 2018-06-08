package com.extlight.web.listener;

import com.extlight.component.CommonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class GlobalListener implements ServletContextListener {

    @Autowired
    private CommonMap commonMap;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("paramMap",commonMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().removeAttribute("paramMap");
        commonMap.clear();
    }
}
