package com.extlight.web.controller.admin;

import com.extlight.common.constant.UserConstant;
import com.extlight.common.vo.Result;
import com.extlight.model.Log;
import com.extlight.model.Param;
import com.extlight.model.User;
import com.extlight.service.*;
import com.extlight.web.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页相关
 */
@RestController
@RequestMapping("/admin/index")
public class IndexController {

    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GuestbookService guestbookService;
    @Autowired
    private ParamService paramService;
    @Autowired
    private LogService logService;

    @GetMapping("/basicInfo")
    public Result basicInfo(HttpSession session) {
        try {
            User user = (User) session.getAttribute(UserConstant.LOGIN_USER);

            Map<String,Object> map = new HashMap<>();
            map.put("user",user);

            return Result.success(map);
        } catch (Exception e) {
            throw new GlobalException(500,e.getMessage());
        }
    }


    /**
     * 指标数据
     * @return
     */
    @GetMapping("/indexData")
    public Result indexData() {

        try {
            int postCount = this.postService.getPostCount(null);
            int categoryCount = this.categoryService.getCategoryCount();
            int tagCount = this.postService.getTagCount();
            int guestbookCount = this.guestbookService.getGuestbookCount();

            Map<String,Integer> map = new HashMap<>(4);
            map.put("postCount",postCount);
            map.put("categoryCount",categoryCount);
            map.put("tagCount",tagCount);
            map.put("guestbookCount",guestbookCount);

            return Result.success(map);
        } catch (Exception e) {
            throw new GlobalException(500,e.getMessage());
        }
    }

    /**
     * 系统参数
     * @return
     */
    @GetMapping("/sysParamData")
    public Result sysParamData() {
        try {
            List<Param> list = this.paramService.getParamList(1);
            return Result.success(list);
        } catch (Exception e) {
            throw new GlobalException(500,e.getMessage());
        }
    }

    /**
     * 操作日志
     * @return
     */
    @GetMapping("/sysLogData")
    public Result sysLogData() {
        try {
            List<Log> list = this.logService.getList();
            return Result.success(list);
        } catch (Exception e) {
            throw new GlobalException(500,e.getMessage());
        }
    }

    /**
     * 清空日志
     * @return
     */
    @PostMapping("/clearLogData")
    public Result clearLogData() {
        try {
            this.logService.deleteAll();
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(500,e.getMessage());
        }
    }
}
