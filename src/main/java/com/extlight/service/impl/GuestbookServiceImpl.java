package com.extlight.service.impl;

import com.extlight.mapper.BaseMapper;
import com.extlight.mapper.GuestbookMapper;
import com.extlight.model.Guestbook;
import com.extlight.service.GuestbookService;
import com.extlight.web.exception.GlobalException;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestbookServiceImpl extends BaseServiceImpl<Guestbook> implements GuestbookService {

    @Autowired
    private GuestbookMapper guestbookMapper;

    @Override
    public BaseMapper<Guestbook> getBaseMapper() {
        return guestbookMapper;
    }

    @Override
    public int getGuestbookCount() {
        return this.guestbookMapper.selectCount(null);
    }

    @Override
    public void deleteGuestbook(Integer id) throws GlobalException{
        Guestbook guestbook = new Guestbook();
        guestbook.setId(id);
        guestbook.setDelStatus(1);
        this.guestbookMapper.updateByPrimaryKeySelective(guestbook);
    }

    @Override
    public List<Guestbook> getListPyPage(Integer delStatus, Integer pageNum, Integer pageSize) throws GlobalException{
        PageHelper.startPage(pageNum,pageSize);
        return this.guestbookMapper.getList(delStatus);
    }

    @Override
    public void save(Guestbook guestbook) throws GlobalException {
        guestbook.setStatus(0);
        guestbook.setDelStatus(0);
        this.guestbookMapper.insert(guestbook);
    }
}
