package com.lushihao.aicode.service;

import com.lushihao.aicode.model.dto.app.AppAddRequest;
import com.lushihao.aicode.model.dto.app.AppQueryRequest;
import com.lushihao.aicode.model.entity.User;
import com.lushihao.aicode.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.lushihao.aicode.model.entity.App;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/zhemu6">ShihaoLu</a>
 */
public interface AppService extends IService<App> {

    void generateAppScreenshotAsync(long appId, String webUrl);

    AppVO getAppVO(App app);

    List<AppVO> getAppVOList(List<App> appList);

    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 通过聊天生成代码
     * @param appId 应用ID
     * @param message 用户提示词
     * @param loginUser 登录用户
     * @return 流式返回代码
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    Long addApp(AppAddRequest appAddRequest, User loginUser);

    /**
     * 应用部署
     * @param appId 应用ID
     * @param loginUser 登录用户
     * @return 应用部署地址
     */
    String deployApp(Long appId,User loginUser);
}
