package com.extlight.web.controller.admin;

import com.extlight.aspect.SysLog;
import com.extlight.common.vo.Result;
import com.extlight.model.AboutMe;
import com.extlight.service.AboutMeService;
import com.extlight.web.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 关于我
 */
@RestController
@RequestMapping("/admin/aboutMe")
public class AboutMeController {

    @Autowired
    private AboutMeService aboutMeService;

    /**
     * 获取内容
     * @return
     */
    @GetMapping("/getAboutMe")
    public Result getAboutMe() {
        AboutMe aboutMe = this.aboutMeService.getAboutMe(null);
        return Result.success(aboutMe);
    }

    @SysLog("保存关于我")
    @PostMapping("/save")
    public Result save(@Valid AboutMe aboutMe) {
        try {
            this.aboutMeService.update(aboutMe);
            return Result.success();
        } catch (Exception e) {
            throw new GlobalException(500,e.getMessage());
        }
    }
}
