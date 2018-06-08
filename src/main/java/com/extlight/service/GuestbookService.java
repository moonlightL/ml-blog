package com.extlight.service;


import com.extlight.model.Guestbook;
import com.extlight.web.exception.GlobalException;

import java.util.List;

public interface GuestbookService extends BaseService<Guestbook>{

    /**
     * 留言个数
     * @return
     */
    int getGuestbookCount() throws GlobalException;

    /**
     *  软删除
     * @param id
     */
    void deleteGuestbook(Integer id) throws GlobalException;

    /**
     * 获取留言列表
     * @param delStatus 1:删除 0：未删除
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Guestbook> getListPyPage(Integer delStatus, Integer pageNum, Integer pageSize) throws GlobalException;
}
