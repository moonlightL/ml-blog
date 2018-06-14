package com.extlight.service.impl;

import com.extlight.mapper.BaseMapper;
import com.extlight.mapper.LogMapper;
import com.extlight.model.Log;
import com.extlight.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public BaseMapper<Log> getBaseMapper() {
        return logMapper;
    }

    @Override
    public List<Log> getList() {
        Example example = new Example(Log.class);
        example.orderBy("createTime").desc();

        return this.logMapper.selectByExample(example);
    }

    @Override
    public void deleteAll() {
        this.logMapper.deleteAll();
    }
}
