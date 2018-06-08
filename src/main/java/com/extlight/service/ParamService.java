package com.extlight.service;


import com.extlight.model.Param;
import com.extlight.web.exception.GlobalException;

import java.util.List;
import java.util.Map;

public interface ParamService extends BaseService<Param> {

    /**
     * 参数列表
     * @param type 1：全局 2：个人
     * @return
     */
    List<Param> getParamList(Integer type) throws GlobalException;

    /**
     * 更新参数值
     * @param pk    id
     * @param value 参数值
     */
    void updateValue(Integer pk, String value) throws GlobalException;

    /**
     * 修改个人参数
     * @param map
     */
    void updatePerParam(Map<String, String> map) throws GlobalException;
}
