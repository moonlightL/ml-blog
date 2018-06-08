package com.extlight.service.impl;

import com.extlight.mapper.BaseMapper;
import com.extlight.mapper.UserMapper;
import com.extlight.model.User;
import com.extlight.service.UserService;
import com.extlight.web.exception.GlobalException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) throws GlobalException {
        return this.userMapper.getByUsername(username);
    }

    @Override
    public void updatePwd(String username, String oldpwd, String newpwd) throws GlobalException {

        User user = this.findByUsername(username);
        if (user == null) {
            throw new GlobalException(403,"用户名不存在");
        }

        if(!user.getPassword().equals(DigestUtils.md5Hex(oldpwd))) {
            throw new GlobalException(403,"旧密码不正确");
        }

        User tmp = new User();
        tmp.setId(user.getId());
        tmp.setPassword(DigestUtils.md5Hex(newpwd));

        this.userMapper.updateByPrimaryKeySelective(tmp);
    }


    @Override
    public BaseMapper<User> getBaseMapper() {
        return this.userMapper;
    }
}
