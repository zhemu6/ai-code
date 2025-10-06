package com.lushihao.aicodeuser.service.impl;

import com.lushihao.aicode.innerservice.InnerUserService;
import com.lushihao.aicode.model.entity.User;
import com.lushihao.aicode.model.vo.UserVO;
import com.lushihao.aicodeuser.service.UserService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-10-06   15:25
 */

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;

    @Override
    public List<User> listByIds(Collection<? extends Serializable> ids) {
        return userService.listByIds(ids);
    }

    @Override
    public User getById(Serializable id) {
        return userService.getById(id);
    }

    @Override
    public UserVO getUserVO(User user) {
        return userService.getUserVO(user);
    }
}

