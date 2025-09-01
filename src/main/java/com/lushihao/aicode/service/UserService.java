package com.lushihao.aicode.service;

import com.lushihao.aicode.model.dto.user.UserLoginRequest;
import com.lushihao.aicode.model.dto.user.UserQueryRequest;
import com.lushihao.aicode.model.dto.user.UserRegisterRequest;
import com.lushihao.aicode.model.vo.LoginUserVO;
import com.lushihao.aicode.model.vo.UserVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.lushihao.aicode.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户表 服务层。
 *
 * @author <a href="https://github.com/zhemu6">ShihaoLu</a>
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册功能
     * @param userRegisterRequest 用户注册封装
     * @return 新用户的id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 加盐算法
     * @param userPassword 原始密码
     * @return 加盐后的密码
     */
    String getEncryptPassword(String userPassword);

    /**
     * 获取脱敏的已登录用户信息
     * @param user 用户
     * @return 脱敏后的用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户登录
     * @param userLoginRequest 用户登录封装类
     * @param request 请求
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);


    UserVO getUserVO(User user);

    List<UserVO> getUserVOList(List<User> userList);

    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);
}
