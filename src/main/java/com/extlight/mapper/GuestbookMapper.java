package com.extlight.mapper;

import com.extlight.model.Guestbook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GuestbookMapper extends BaseMapper<Guestbook> {

    /**
     * 获取留言列表
     * @param delStatus
     * @return
     */
    List<Guestbook> getList(Integer delStatus);
}
