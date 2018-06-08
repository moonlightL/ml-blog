package com.extlight.service;

import com.extlight.model.AboutMe;
import com.extlight.web.exception.GlobalException;

public interface AboutMeService extends BaseService<AboutMe> {

    AboutMe getAboutMe(Integer status) throws GlobalException;
}
