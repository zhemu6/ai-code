package com.lushihao.aicode.controller;

import cn.hutool.core.bean.BeanUtil;
import com.lushihao.aicode.annotation.AuthCheck;
import com.lushihao.aicode.common.BaseResponse;
import com.lushihao.aicode.common.DeleteRequest;
import com.lushihao.aicode.common.PageRequest;
import com.lushihao.aicode.common.ResultUtils;
import com.lushihao.aicode.constant.UserConstant;
import com.lushihao.aicode.exception.ErrorCode;
import com.lushihao.aicode.exception.ThrowUtils;
import com.lushihao.aicode.model.dto.user.*;
import com.lushihao.aicode.model.vo.LoginUserVO;
import com.lushihao.aicode.model.vo.UserVO;
import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.lushihao.aicode.model.entity.User;
import com.lushihao.aicode.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 用户表 控制层。
 *
 * @author <a href="https://github.com/zhemu6">ShihaoLu</a>
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     * @return 用户的注册id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        log.info("用户注册功能，请求参数为：{}", userRegisterRequest);
        long userId = userService.userRegister(userRegisterRequest);
        return ResultUtils.success(userId);
    }

    /**
     * 用户登录
     * @param userLoginRequest 用户登陆封装类
     * @return 后端给前端返回的封装类型
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest , HttpServletRequest request){
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        log.info("用户登录功能，请求参数为：{}", userLoginRequest);
        LoginUserVO loginUserVO = userService.userLogin(userLoginRequest,request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return 脱敏后的用户
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        log.info("获取当前登录用户，请求参数为：{}", request);
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }


    /**
     * 用戶注销
     * @param request
     * @return 是否注销成功
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        // 检查request是否为空，如果为空则抛出参数错误异常
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        // 调用userService的userLogout方法，返回结果
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 添加用户
     * @param userAddRequest 用户添加请求
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        log.info("新增用户功能，请求参数为：{}", userAddRequest);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        // 默认密码
        final String DEFAULT_PASSWORD = "12345678";
        // 对密码加密
        String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result , ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据id获取用户（管理员）
     * @param id 用户id
     * @return User 未脱敏
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id){
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }


    /**
     * 根据id获取包装类用户
     * @param id 用户id
     * @return UserVO 包装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id){
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 删除用户 （管理员）
     * @param deleteRequest 删除请求
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null||deleteRequest.getId()<=0, ErrorCode.PARAMS_ERROR);
        boolean result = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新用户 （管理员）
     * @param userUpdateRequest 用户更新请求
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest == null||userUpdateRequest.getId()<=0, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = userQueryRequest.getPageNum();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(Page.of(pageNum, pageSize),
                userService.getQueryWrapper(userQueryRequest));
        // 数据脱敏
        Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }




//
//
//
//    /**
//     * 保存用户表。
//     *
//     * @param user 用户表
//     * @return {@code true} 保存成功，{@code false} 保存失败
//     */
//    @PostMapping("save")
//    public boolean save(@RequestBody User user) {
//        return userService.save(user);
//    }
//
//    /**
//     * 根据主键删除用户表。
//     *
//     * @param id 主键
//     * @return {@code true} 删除成功，{@code false} 删除失败
//     */
//    @DeleteMapping("remove/{id}")
//    public boolean remove(@PathVariable Long id) {
//        return userService.removeById(id);
//    }
//
//    /**
//     * 根据主键更新用户表。
//     *
//     * @param user 用户表
//     * @return {@code true} 更新成功，{@code false} 更新失败
//     */
//    @PutMapping("update")
//    public boolean update(@RequestBody User user) {
//        return userService.updateById(user);
//    }
//
//    /**
//     * 查询所有用户表。
//     *
//     * @return 所有数据
//     */
//    @GetMapping("list")
//    public List<User> list() {
//        return userService.list();
//    }
//
//    /**
//     * 根据主键获取用户表。
//     *
//     * @param id 用户表主键
//     * @return 用户表详情
//     */
//    @GetMapping("getInfo/{id}")
//    public User getInfo(@PathVariable Long id) {
//        return userService.getById(id);
//    }
//
//    /**
//     * 分页查询用户表。
//     *
//     * @param page 分页对象
//     * @return 分页对象
//     */
//    @GetMapping("page")
//    public Page<User> page(Page<User> page) {
//        return userService.page(page);
//    }

}
