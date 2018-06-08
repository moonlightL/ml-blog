package com.extlight.web.controller.admin;

import com.extlight.aspect.SysLog;
import com.extlight.common.vo.Result;
import com.extlight.model.Param;
import com.extlight.service.ParamService;
import com.extlight.web.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 参数相关
 */
@RestController
@RequestMapping("/admin/param")
public class ParamController {

    @Autowired
    private ParamService paramService;

    /**
     * 参数列表
     * @return
     */
    @GetMapping("/list/{type}")
    public Result paramList(@PathVariable Integer type) {
        try {
            List<Param> list = this.paramService.getParamList(type);
            return Result.success(list);
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }

    /**
     * 修改全局参数
     * @param pk    唯一标识，editable 插件定义唯一标识为 pk
     * @param value
     * @return
     */
    @SysLog("修改全局参数")
    @PostMapping("/updateSysParam")
    public Result updateSysParam(Integer pk, String value) {
        try {
            this.paramService.updateValue(pk,value);
            return Result.success();
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }

    /**
     * 修改个人参数
     * @return
     */
    @SysLog("修改个人参数")
    @PostMapping("/updatePerParam")
    public Result updatePerParam(@RequestParam Map<String,String> map) {
        try {
            this.paramService.updatePerParam(map);
            return Result.success();
        } catch (Exception e) {
            throw  new GlobalException(500,e.getMessage());
        }
    }

}
