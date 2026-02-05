package com.dbts.dreambacktosong_backend.config;

import com.dbts.dreambacktosong_backend.security.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 统一配置
 *
 * <p>主要职责：
 * 1. 注册登录鉴权拦截器；
 * 2. 简单配置 CORS，方便小程序联调。
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 鉴权拦截器配置：
        // - 默认拦截所有请求
        // - 放行不需要登录的接口（如发送验证码、登录、部分发现/商品/反馈等）
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 认证模块
                        "/auth/send-code",
                        "/auth/login",
                        // 发现与内容 - 公开
                        "/discover/carousel",
                        "/discover/topics",
                        "/discover/feed",
                        // 商品与集市 - 公开浏览
                        "/products/**",
                        // 反馈 - 可匿名
                        "/feedback",
                        // 健康检查等
                        "/actuator/health",
                        "/error"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 简单跨域配置：根据实际部署环境可进一步收紧

        //就是去掉/api(yml格式里配置了), 后的请求url随意
        registry.addMapping("/**")  // 1. 匹配所有接口路径
                .allowedOrigins("*")  // 2. 允许所有来源的跨域请求
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")  // 3. 允许的HTTP请求方法
                .allowedHeaders("*")  // 4. 允许请求中携带所有请求头
                .allowCredentials(false)  // 5. 不允许携带Cookie等凭证信息
                .maxAge(3600);  // 6. 预检请求（OPTIONS）的缓存时间，单位秒
    }
}

