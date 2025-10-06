package com.lushihao.aicode.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.lushihao.aicode.exception.BusinessException;
import com.lushihao.aicode.exception.ErrorCode;
import com.lushihao.aicode.exception.ThrowUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

/**
 * 浏览器截图工具类 提供URL生成截图文件并返回路径
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-03   15:07
 */
@Slf4j
public class WebScreenshotUtils {
    /**
     * 使用ThreadLocal确保每个线程拥有独立的WebDriver
     */
    private static  final ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();

    /**
     * 获取当前ThreadLocal的WebDriver 如果不存在就创建
     */
    private static WebDriver getWebDriver() {
        WebDriver webDriver = webDriverThreadLocal.get();
        if(webDriver==null){
            final int DEFAULT_WIDTH = 1600;
            final int DEFAULT_HEIGHT = 900;
            webDriver = initChromeDriver(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            webDriverThreadLocal.set(webDriver);
        }
        return webDriver;
    }

    /**
     * 主动关闭并移除当前线程的 WebDriver
     */
    private static void closeWebDriver() {
        WebDriver webDriver = webDriverThreadLocal.get();
        if(webDriver!=null){
            try{
                webDriver.quit();
                log.info("已关闭当前线程的 WebDriver");
            }catch (Exception e){
                log.info("关闭线程WebDriver失败！",e);
            } finally {
                webDriverThreadLocal.remove();
            }
        }
    }

    @PreDestroy
    public void destroy() {
        closeWebDriver();
    }

    /**
     * 生成网页截图
     * @param webUrl 网页网址
     * @return  压缩后的截图文件路径 失败返回null
     */
    public static String saveWebPageScreenShot(String webUrl){
        if(StrUtil.isBlank(webUrl)){
            log.error("网页地址不能为空");
            return null;
        }
        try{
            WebDriver driver = getWebDriver();

            // 创建临时目录
            String tempDir = System.getProperty("user.dir") + File.separator + "tmp" + File.separator+"screenshot" + File.separator + UUID.randomUUID().toString().substring(0,8);
            FileUtil.mkdir(tempDir);
            final String IMAGE_SUFFIX = ".png";
            String imageSavePath = tempDir + File.separator + RandomUtil.randomNumbers(5) + IMAGE_SUFFIX;
            // 访问网页
            driver.get(webUrl);
            // 等待网页加载
            waitForPageLoad(driver);
            // 截图
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            // 保存原始图片
            saveImage(screenshotBytes,imageSavePath);
            log.info("原始图片保存成功:{}",imageSavePath);
            // 压缩图片
            final String COMPRESS_IMAGE_SUFFIX = "_compress.jpg";
            String compressImageSavePath = tempDir + File.separator + RandomUtil.randomNumbers(5) + COMPRESS_IMAGE_SUFFIX;
            compressImage(imageSavePath,compressImageSavePath);
            log.info("图片压缩成功:{}",compressImageSavePath);
            // 删除原始图片
            FileUtil.del(imageSavePath);
            // 返回压缩后的图片路径
            return compressImageSavePath;
        }catch (Exception e){
            log.error("网页截图失败{}",webUrl, e);
            return null;
        }

    }

    /**
     *  初始化 Chrome 浏览器驱动
     * @param width 浏览器宽
     * @param height 浏览器高
     * @return
     */
    private static WebDriver initChromeDriver(int width, int height) {
        try {
            // 自动管理 ChromeDriver
            WebDriverManager.chromedriver().setup();
            // 配置 Chrome 选项
            ChromeOptions options = new ChromeOptions();
            // 无头模式
            options.addArguments("--headless");
            // 禁用GPU（在某些环境下避免问题）
            options.addArguments("--disable-gpu");
            // 禁用沙盒模式（Docker环境需要）
            options.addArguments("--no-sandbox");
            // 禁用开发者shm使用
            options.addArguments("--disable-dev-shm-usage");
            // 设置窗口大小
            options.addArguments(String.format("--window-size=%d,%d", width, height));
            // 禁用扩展
            options.addArguments("--disable-extensions");
            // 设置用户代理
            options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            // 创建驱动
            WebDriver driver = new ChromeDriver(options);
            // 设置页面加载超时
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            // 设置隐式等待
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (Exception e) {
            log.error("初始化 Chrome 浏览器失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "初始化 Chrome 浏览器失败");
        }
    }

    /**
     * 保存图片
     * @param imageBytes
     * @param imagePath
     */
    private static void saveImage(byte[] imageBytes,String imagePath){
        try {
            FileUtil.writeBytes(imageBytes, imagePath);
        } catch (Exception e) {
            log.error("保存图片失败：{}",imagePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存图片失败");
        }
    }

    /**
     * 压缩图片
     * @param sourceImagePath 源图片地址
     * @param compressImagePath 保存压缩后的图片地址
     */
    private static void compressImage(String sourceImagePath,String compressImagePath){
        // 压缩图片质量参数
        final float COMPRESS_QUALITY = 0.3f;
        try {
            ImgUtil.compress(FileUtil.file(sourceImagePath),FileUtil.file(compressImagePath),COMPRESS_QUALITY);
        } catch (Exception e) {
            log.error("压缩图片失败：{}",sourceImagePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "压缩图片失败");
        }
    }

    /**
     * 等待页面加载完成
     * @param driver web 驱动
     */
    private static void waitForPageLoad(WebDriver driver) {
        try {
            // 创建等待页面加载对象
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // 等待 document.readyState 为complete
            wait.until(webDriver ->
                    ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
                            .equals("complete")
            );
            // 额外等待一段时间，确保动态内容加载完成
            Thread.sleep(2000);
            log.info("页面加载完成");
        } catch (Exception e) {
            log.error("等待页面加载时出现异常，继续执行截图", e);
        }
    }


}
