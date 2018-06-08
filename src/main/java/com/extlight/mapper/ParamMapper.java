package com.extlight.mapper;

import com.extlight.model.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParamMapper extends BaseMapper<Param> {

    /**
     * 参数列表
     * @param type 1：全局 2：个人
     * @return
     */
    List<Param> getParamList(Integer type);

    /**
     * 通过参数名修改参数值
     * @param paramList
     */
    void updateByParamName(@org.apache.ibatis.annotations.Param("paramList") List<Param> paramList);
}
