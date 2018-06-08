package com.extlight.mapper;

import com.extlight.model.AboutMe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AboutMeMapper extends BaseMapper<AboutMe> {

    /**
     * 通过状态获取
     * @param  status
     * @return
     */
    AboutMe getByStatus(@Param("status") Integer status);
}
