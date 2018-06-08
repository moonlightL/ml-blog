package com.extlight.service;

import com.extlight.web.exception.GlobalException;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public interface BaseService<T> {


    void save(T model) throws GlobalException;

    void update(T model) throws GlobalException;

    void delete(Integer id) throws GlobalException;

    T getById(Integer id) throws GlobalException;

    List<T> getList() throws GlobalException;

    List<T> getPyPage(Integer pageNum, Integer pageSize) throws GlobalException;

    List<T> getPyExamplePage(Example example,Integer pageNum, Integer pageSize) throws GlobalException;
}
