package com.extlight.web.controller.admin;

import com.extlight.aspect.SysLog;
import com.extlight.common.constant.PageConstant;
import com.extlight.common.utils.IPUtil;
import com.extlight.common.vo.Result;
import com.extlight.model.Guestbook;
import com.extlight.service.GuestbookService;
import com.extlight.web.exception.GlobalException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 留言相关
 */
@RestController
@RequestMapping("/admin/guestbook")
public class GuestbookController {

    @Autowired
    private GuestbookService guestbookService;


    @GetMapping("/list/{pageNum}/{delStatus}")
    public Result list(@PathVariable("pageNum") Integer pageNum, @PathVariable("delStatus") Integer delStatus) {

        try {
            List<Guestbook> list = this.guestbookService.getListPyPage(delStatus,null,pageNum,PageConstant.PAGE_SIZE);
            return Result.success(new PageInfo<>(list,10));
        } catch (Exception e) {
            throw new GlobalException(500,e.getMessage());
        }

    }

    @GetMapping("/get/{id}")
    public Result get(@PathVariable Integer id) {
        try {
            Guestbook guestbook = this.guestbookService.getById(id);
            return Result.success(guestbook);
        } catch (Exception e) {
            throw new GlobalException(500,e.getMessage());
        }
    }

    @SysLog("回复留言")
    @PostMapping("/save")
    public Result save(Guestbook guestbook, HttpServletRequest request) {
        try {
            guestbook.setIp(IPUtil.getIpAddr(request));
            String city = IPUtil.getCity(guestbook.getIp());
            guestbook.setIpAddr(city == null ? "未知" : city);
            this.guestbookService.reply(guestbook);
            return Result.success();
        } catch (Exception e) {
            throw new GlobalException(500,e.getMessage());
        }
    }

    @SysLog("删除留言")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            this.guestbookService.deleteGuestbook(id);
            return Result.success();
        } catch (Exception e) {
            throw new GlobalException(500,e.getMessage());
        }
    }
}
