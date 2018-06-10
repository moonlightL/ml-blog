package com.extlight.web.controller.admin;

import com.extlight.aspect.SysLog;
import com.extlight.common.constant.PageConstant;
import com.extlight.common.vo.Result;
import com.extlight.model.Category;
import com.extlight.service.CategoryService;
import com.extlight.web.exception.GlobalException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 分类相关
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页列表
     * @param pageNum
     * @return
     */
    @GetMapping("/list/{pageNum}")
    public Result list(@PathVariable Integer pageNum) {
        try {
            List<Category> list = this.categoryService.getPyPage(pageNum, PageConstant.PAGE_SIZE);
            return Result.success(new PageInfo<>(list,10));
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }

    @GetMapping("/listAll")
    public Result listAll() {
        try {
            List<Category> list = this.categoryService.getList();
            return Result.success(list);
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
            Category category = this.categoryService.getById(id);
            return Result.success(category);
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }

    /**
     * 保存
     * @param category
     * @return
     */
    @SysLog("保存目录")
    @PostMapping("/save")
    public Result save(@Valid Category category) {
        try {
            if (category.getId() == null || category.getId() == 0) {
                this.categoryService.save(category);
            } else {
                this.categoryService.update(category);
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
    @SysLog("删除目录")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            this.categoryService.delete(id);
            return Result.success();
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }
}
