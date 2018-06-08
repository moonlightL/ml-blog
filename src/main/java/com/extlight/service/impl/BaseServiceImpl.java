package com.extlight.service.impl;

import com.extlight.mapper.BaseMapper;
import com.extlight.service.BaseService;
import com.extlight.web.exception.GlobalException;
import com.github.pagehelper.PageHelper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    public abstract BaseMapper<T> getBaseMapper();

    public void save(T model)  throws GlobalException {
        this.getBaseMapper().insertSelective(model);
    }

    public void update(T model) throws GlobalException  {
        this.getBaseMapper().updateByPrimaryKeySelective(model);
    }

    public void delete(Integer id)  throws GlobalException  {
        this.getBaseMapper().deleteByPrimaryKey(id);
    }

    public T getById(Integer id)  throws GlobalException  {
        return this.getBaseMapper().selectByPrimaryKey(id);
    }

    public List<T> getList()  throws GlobalException  {
        return this.getBaseMapper().selectAll();
    }

    public List<T> getPyPage(Integer pageNum, Integer pageSize)   throws GlobalException {
        PageHelper.startPage(pageNum,pageSize);
        return this.getList();
    }

    public List<T> getPyExamplePage(Example example, Integer pageNum, Integer pageSize)  throws GlobalException {
        PageHelper.startPage(pageNum,pageSize);
        return this.getBaseMapper().selectByExample(example);
    }
}
