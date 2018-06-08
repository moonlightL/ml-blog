package com.extlight.service.impl;

import com.extlight.mapper.AboutMeMapper;
import com.extlight.mapper.BaseMapper;
import com.extlight.model.AboutMe;
import com.extlight.service.AboutMeService;
import com.extlight.web.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AboutMeServiceImpl extends BaseServiceImpl<AboutMe> implements AboutMeService {

    @Autowired
    private AboutMeMapper aboutMeMapper;

    @Override
    public BaseMapper<AboutMe> getBaseMapper() throws GlobalException {
        return this.aboutMeMapper;
    }

    @Override
    public AboutMe getAboutMe(Integer status) throws GlobalException {
        return this.aboutMeMapper.getByStatus(status);
    }
}
