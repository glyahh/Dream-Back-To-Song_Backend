package com.dbts.dreambacktosong_backend;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 梦回宋朝后端应用主类
 *
 * <p>说明：
 * <ul>
 *   <li>应用启动时会自动加载项目根目录下的 .env 文件</li>
 *   <li>.env 文件中的环境变量会被注入到系统属性中</li>
 *   <li>application-dev.yml 和 application-build.yml 中的 ${VAR_NAME:default} 语法会优先读取系统属性</li>
 * </ul>
 *
 * @author Dream-Back-To-Song
 */
@Slf4j
@SpringBootApplication
public class DreamBackToSongBackendApplication {

    public static void main(String[] args) {
        // 加载 .env 文件到系统属性（如果文件存在）
        // 这样 application.yml 中的 ${VAR_NAME:default} 就能读取到 .env 中的值
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")  // 从项目根目录查找 .env 文件
                    .ignoreIfMissing() // 如果文件不存在也不报错
                    .load();
            
            // 将 .env 中的变量添加到系统属性中（System.setProperty）
            // Spring Boot 的 ${VAR_NAME} 会优先读取系统属性，然后是环境变量，最后是默认值
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                // 只有当系统属性中不存在该 key 时才设置（避免覆盖已存在的系统属性）
                if (System.getProperty(key) == null) {
                    System.setProperty(key, value);
                }
            });
        } catch (Exception e) {
            // .env 文件不存在或读取失败时，使用默认配置
            log.info("未找到 .env 文件，将使用 application.yml 中的默认配置");
        }
        
        SpringApplication.run(DreamBackToSongBackendApplication.class, args);
    }

}
