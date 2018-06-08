package com.extlight.web.interceptor;

import com.extlight.common.constant.UserConstant;
import com.extlight.common.utils.JsonUtil;
import com.extlight.common.vo.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Object user = request.getSession().getAttribute(UserConstant.LOGIN_USER);
        if (user == null) {
            String requestURI = request.getRequestURI();
            if (requestURI.contains("get") || requestURI.contains("list") || requestURI.contains("save") || requestURI.contains("delete")) {
                this.writeToJson(response);
            } else {
                response.sendRedirect("/admin");
            }
        }
        return true;
    }

    private void writeToJson(HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            writer.write(JsonUtil.obj2String(Result.fail(302,"/admin")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
