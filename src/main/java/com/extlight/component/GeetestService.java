package com.extlight.component;

import com.extlight.common.utils.IPUtil;
import com.extlight.component.sdk.GeetestLib;
import com.extlight.web.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Random;

/**
 * 极验相关
 */
@Component
@Slf4j
public class GeetestService {

    @Autowired
    private CommonMap commonMap;

    /**
     * 获取验证码
     * @return
     */
    public String StartCaptcha(HttpServletRequest request) throws GlobalException{

        try {
            GeetestLib gtSdk = new GeetestLib(commonMap.get("geetest_id").toString(), commonMap.get("geetest_key").toString(),true);

            String userId = request.getParameter("nickname") + "_" + (new Random().nextInt(1000));

            //自定义参数,可选择添加
            HashMap<String, String> param = new HashMap<>(3);
            param.put("user_id", userId); //网站用户id
            param.put("client_type", "web"); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
            param.put("ip_address", IPUtil.getIpAddr(request)); //传输用户请求验证时所携带的IP

            //进行验证预处理
            int gtServerStatus = gtSdk.preProcess(param);

            //将服务器状态设置到session中
            request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
            //将userid设置到session中
            request.getSession().setAttribute("userId", userId);

            return gtSdk.getResponseStr();
        } catch (Exception e) {
            log.error("获取验证码异常:",e.toString());
            throw new GlobalException(500,e.toString());
        }
    }

    /**
     * 检测验证码
     * @return
     */
    public boolean verifyCaptcha(HttpServletRequest request) throws GlobalException{

        try {
            GeetestLib gtSdk = new GeetestLib(commonMap.get("geetest_id").toString(), commonMap.get("geetest_key").toString(),true);

            String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
            String validate = request.getParameter(GeetestLib.fn_geetest_validate);
            String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

            //从session中获取gt-server状态
            int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);

            //从session中获取userid
            String userId = (String)request.getSession().getAttribute("userId");

            //自定义参数,可选择添加
            HashMap<String, String> param = new HashMap<>(3);
            param.put("user_id", userId); //网站用户id
            param.put("client_type", "web"); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
            param.put("ip_address", IPUtil.getIpAddr(request)); //传输用户请求验证时所携带的IP

            int gtResult;

            if (gt_server_status_code == 1) {
                //gt-server正常，向gt-server进行二次验证
                gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            } else {
                // gt-server非正常情况下，进行failback模式验证
                gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            }

            return gtResult == 1;
        } catch (Exception e) {
            log.error("检测验证码异常:",e.toString());
            throw new GlobalException(500,e.toString());
        }
    }
}
