package com.lushihao.aicode.core;

import cn.hutool.json.JSONUtil;
import com.lushihao.aicode.ai.AiCodeGeneratorService;
import com.lushihao.aicode.ai.AiCodeGeneratorServiceFactory;
import com.lushihao.aicode.ai.model.HtmlCodeResult;
import com.lushihao.aicode.ai.model.MultiFileCodeResult;
import com.lushihao.aicode.ai.model.message.AiResponseMessage;
import com.lushihao.aicode.ai.model.message.ToolExecutedMessage;
import com.lushihao.aicode.ai.model.message.ToolRequestMessage;
import com.lushihao.aicode.constant.AppConstant;
import com.lushihao.aicode.core.builder.VueProjectBuilder;
import com.lushihao.aicode.core.parser.CodeParserExecutor;
import com.lushihao.aicode.core.saver.CodeFileSaverExecutor;
import com.lushihao.aicode.exception.BusinessException;
import com.lushihao.aicode.exception.ErrorCode;
import com.lushihao.aicode.exception.ThrowUtils;
import com.lushihao.aicode.model.enums.CodeGenTypeEnum;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI 代码生成门面类 组合生成和保存功能
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-01   23:22
 */
@Service
@Slf4j
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;
    @Resource
    private VueProjectBuilder vueProjectBuilder;

    /**
     * 非流式 统一入口：根据类型生成并保存代码
     *
     * @param userMessage 用户提示词
     * @param codeGenType 生成类型
     * @param appId 应用ID
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenType,Long appId) {
        ThrowUtils.throwIf(codeGenType == null, ErrorCode.SYSTEM_ERROR, "生成类型不能为空");
        // 根据appid 获取相应的Ai服务实例
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId,codeGenType);
        return switch (codeGenType) {
            case HTML -> {
                // 获取HtmlCodeResult
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                // 保存代码到文件中
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.HTML,appId);
            }
            case MULTI_FILE ->{
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.MULTI_FILE,appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenType.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);

            }
        };
    }

    /**
     *  流式  统一入口：根据类型输出生成并保存代码
     *
     * @param userMessage 用户提示词
     * @param codeGenType 生成类型
     * @param appId 应用ID
     * @return 保存的目录
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenType,Long appId) {
        ThrowUtils.throwIf(codeGenType == null, ErrorCode.SYSTEM_ERROR, "生成类型不能为空");
        // 根据appid 获取相应的Ai服务实例
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId,codeGenType);
        return switch (codeGenType) {
            case HTML -> {
                Flux<String> resultStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(resultStream, CodeGenTypeEnum.HTML,appId);
            }
            case MULTI_FILE -> {
                Flux<String> resultStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(resultStream, CodeGenTypeEnum.MULTI_FILE,appId);
            }
            case VUE_PROJECT -> {
                TokenStream tokenStream = aiCodeGeneratorService.generateVueProjectCodeStream(appId,userMessage);
                // 将TokenStream 转换成Flux
                yield processTokenStream(tokenStream,appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenType.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 将 TokenStream 转换为 Flux<String>，并传递工具调用信息
     *
     * @param tokenStream TokenStream 对象
     * @param appId 应用ID
     * @return Flux<String> 流式响应
     */
    private Flux<String> processTokenStream(TokenStream tokenStream,Long appId) {
        return Flux.create(sink -> {
            tokenStream.onPartialResponse((String partialResponse) -> {
                        AiResponseMessage aiResponseMessage = new AiResponseMessage(partialResponse);
                        sink.next(JSONUtil.toJsonStr(aiResponseMessage));
                    })
                    .onPartialToolExecutionRequest((index, toolExecutionRequest) -> {
                        ToolRequestMessage toolRequestMessage = new ToolRequestMessage(toolExecutionRequest);
                        sink.next(JSONUtil.toJsonStr(toolRequestMessage));
                    })
                    .onToolExecuted((ToolExecution toolExecution) -> {
                        ToolExecutedMessage toolExecutedMessage = new ToolExecutedMessage(toolExecution);
                        sink.next(JSONUtil.toJsonStr(toolExecutedMessage));
                    })
                    .onCompleteResponse((ChatResponse response) -> {
                        String projectPath = AppConstant.CODE_OUTPUT_ROOT_DIR + "/vue_project_" + appId;
                        vueProjectBuilder.buildProject(projectPath);
                        sink.complete();
                    })
                    .onError((Throwable error) -> {
                        error.printStackTrace();
                        sink.error(error);
                    })
                    .start();
        });
    }



    /**
     * 成多文件格式的流式代码
     * @param codeStream
     * @param codeGenType
     * @param appId
     * @return
     */
    private Flux<String> processCodeStream(Flux<String> codeStream ,CodeGenTypeEnum codeGenType,Long appId) {
        // 字符串拼接器 用于当流式返回所有代码拼接所有代码
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(
                chunk -> {
                    codeBuilder.append(chunk);
                }
        ).doOnComplete(() -> {
                    try {
                        String completeFileCode = codeBuilder.toString();
                        // 利用执行器解析代码
                        Object parsedResult = CodeParserExecutor.executeParser(completeFileCode, codeGenType);
                        // 保存文件并返回
                        File file = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenType,appId);
                        log.info("流式生成多文件代码完成，文件保存路径：{}", file.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("流式生成多文件代码异常", e);
                    }
                }
        );
    }


}
