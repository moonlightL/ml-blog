package com.extlight.web.controller.admin;

import com.extlight.aspect.SysLog;
import com.extlight.common.constant.PageConstant;
import com.extlight.common.vo.Result;
import com.extlight.model.Post;
import com.extlight.service.PostService;
import com.extlight.web.exception.GlobalException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/post")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 分页列表
     * @param pageNum
     * @return
     */
    @GetMapping("/list/{categoryId}/{pageNum}")
    public Result list(@PathVariable Integer categoryId,@PathVariable Integer pageNum, String title) {
        try {
            List<Post> list = this.postService.getPyCategoryId(categoryId,pageNum, PageConstant.PAGE_SIZE, title);
            return Result.success(new PageInfo<>(list,10));
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }

    /**
     * 获取信息
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Integer id) {
        try {
            Post post = this.postService.getById(id);
            return Result.success(post);
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }

    /**
     * 保存
     * @param post
     * @return
     */
    @SysLog("保存文章")
    @PostMapping("/save")
    public Result save(@Valid Post post) {
        try {
            if (post.getId() == null || post.getId() == 0) {
                this.postService.save(post);
            } else {
                this.postService.update(post);
            }
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new GlobalException(500,e.getMessage());
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @SysLog("删除文章")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            this.postService.delete(id);
            return Result.success();
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }

    @SysLog("批量删除文章")
    @PostMapping("/deleteBatch/{ids}")
    public Result deleteBatch(@PathVariable String ids) {
        try {
            this.postService.deleteBatch(ids);
            return Result.success();
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }

    @SysLog("导入文件")
    @PostMapping("/importFiles")
    public Result importFiles(String path) {
        try {
            this.postService.importFiles(path);
            return Result.success();
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }

}
