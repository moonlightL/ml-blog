package com.extlight.web.controller.admin;

import com.extlight.aspect.SysLog;
import com.extlight.common.constant.UserConstant;
import com.extlight.common.vo.LoginVo;
import com.extlight.common.vo.Result;
import com.extlight.common.vo.UserVo;
import com.extlight.model.User;
import com.extlight.service.UserService;
import com.extlight.web.exception.GlobalException;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;

/**
 * 登录相关
 */
@RestController
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer captchaProducer;

    @GetMapping(value = {"/captcha","/captcha.do"})
    public void getKaptchaImage(HttpServletResponse response, HttpSession session) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setContentType("image/jpeg");
        //生成验证码
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        //向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
    }

    @SysLog("用户登录")
    @PostMapping("/login")
    public Result login(@Valid LoginVo loginVo, HttpSession session) throws GlobalException {

        try {

            String capText = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

            if (!capText.equals(loginVo.getCaptcha())) {
                throw new GlobalException(400,"验证码不正确");
            }

            session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);

            User user = this.userService.findByUsername(loginVo.getUsername());

            if (user == null) {
                throw new GlobalException(403,"用户名不存在");
            }

            if (!user.getPassword().equals(DigestUtils.md5Hex(loginVo.getPassword()))) {
                throw new GlobalException(403,"密码不正确");
            }

            if (user.getStatus() == 0) {
                throw new GlobalException(403,"该用户被禁用");
            }

            user.setPassword(null);

            session.setAttribute(UserConstant.LOGIN_USER,user);
            return Result.success("/admin/index");

        } catch (GlobalException e) {
            throw new GlobalException(500,e.getMessage());
        }
    }

    @SysLog("用户注销")
    @GetMapping("/logout")
    public Result logout(HttpSession session) throws GlobalException {
        session.invalidate();
        return Result.success("/admin");
    }

    @SysLog("修改密码")
    @PostMapping("/updatePwd")
    public Result updatePwd(@Valid UserVo userVo, HttpSession session) throws GlobalException {

        User user = (User) session.getAttribute(UserConstant.LOGIN_USER);

        try {
            this.userService.updatePwd(user.getUsername(), userVo.getOldpwd(),userVo.getNewpwd());
            // 需要重新登录
            session.invalidate();

            return Result.success();
        } catch (Exception e) {
            throw new GlobalException(500,e.toString());
        }
    }
}
