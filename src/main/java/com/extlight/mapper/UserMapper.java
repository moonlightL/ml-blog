package com.extlight.mapper;

import com.extlight.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User getByUsername(String username);
}
